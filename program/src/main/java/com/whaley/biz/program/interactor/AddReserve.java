package com.whaley.biz.program.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.CommonCMSFunction;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.program.api.LiveAPI;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/8/17.
 */

public class AddReserve extends UseCase<CMSResponse, String> {

    public AddReserve(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<CMSResponse> buildUseCaseObservable(final String code) {
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
                }).flatMap(new Function<UserModel, ObservableSource<CMSResponse>>() {
                    @Override
                    public ObservableSource<CMSResponse> apply(@NonNull UserModel userModel) throws Exception {
                        UserModel a = userModel;
                        return getRepositoryManager().obtainRemoteService(LiveAPI.class)
                                .add(userModel.getAccount_id(),userModel.getAccessTokenModel().getAccesstoken(),userModel.getDeviceId(), code);
                    }
                }).map(new ResponseFunction<CMSResponse, CMSResponse>());
    }

}

