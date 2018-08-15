package com.whaley.biz.portal.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.PortalResponse;
import com.whaley.biz.portal.api.PortalApi;
import com.whaley.biz.portal.model.PortalCollectModel;
import com.whaley.biz.portal.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2018/8/10.
 */

public class CollectPortal extends UseCase<PortalResponse, PortalCollectModel> {

    public CollectPortal(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<PortalResponse> buildUseCaseObservable(final PortalCollectModel portalCollectModel) {
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
                }).flatMap(new Function<UserModel, ObservableSource<PortalResponse>>() {
                    @Override
                    public ObservableSource<PortalResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(PortalApi.class)
                                .collect(portalCollectModel,userModel.getPortalAccessToken());
                    }
                }) .map(new ResponseFunction<PortalResponse,PortalResponse>());
    }

}

