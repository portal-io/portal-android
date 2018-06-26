package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.CommonCMSFunction;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.setting.api.CurrencyAPI;
import com.whaley.biz.setting.api.PayAPI;
import com.whaley.biz.setting.model.RedemptionCodeModel;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.response.UserAmountResponse;
import com.whaley.biz.setting.util.RequestUtils;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/10/12.
 */

public class GetUserAmount extends UseCase<Integer, String> {

    public GetUserAmount(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<Integer> buildUseCaseObservable(final String param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .map(new Function<UserModel, UserModel>() {
                    @Override
                    public UserModel apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return userModel;
                        }
                        throw new NotLoggedInErrorException("未登录");
                    }
                }).flatMap(new Function<UserModel, ObservableSource<UserAmountResponse>>() {
                    @Override
                    public ObservableSource<UserAmountResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(CurrencyAPI.class)
                                .userAmount(userModel.getAccount_id());
                    }
                }).map(new CommonCMSFunction<UserAmountResponse, Integer>());
    }

}
