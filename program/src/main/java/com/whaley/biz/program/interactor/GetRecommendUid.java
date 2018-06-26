package com.whaley.biz.program.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.RecommendAPI;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.model.response.RecommendResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/7/27 17:59.
 */

@Route(path = ProgramRouterPath.USECASE_RECOMMEND_UID)
public class GetRecommendUid extends UseCase<RecommendResponse, String> implements IProvider {

    public GetRecommendUid() {

    }

    public GetRecommendUid(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<RecommendResponse> buildUseCaseObservable(final String code) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<UserModel, String>() {
                    @Override
                    public String apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return userModel.getAccount_id();
                        }
                        return "";
                    }
                })
                .flatMap(new Function<String, ObservableSource<RecommendResponse>>() {
                    @Override
                    public ObservableSource<RecommendResponse> apply(@NonNull String uid) throws Exception {
                        return getRepositoryManager().obtainRemoteService(RecommendAPI.class)
                                .requestAllRecommend(code, CommonConstants.VALUE_ANDROID_VERSION_NAME, uid)
                                .map(new ResponseFunction<RecommendResponse, RecommendResponse>());
                    }
                });
    }

    @Override
    public void init(Context context) {

    }
}
