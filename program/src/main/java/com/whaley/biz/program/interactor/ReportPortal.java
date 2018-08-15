package com.whaley.biz.program.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.PortalResponse;
import com.whaley.biz.program.api.PortalApi;
import com.whaley.biz.program.model.portal.PortalVideoModel;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2018/8/9.
 */

public class ReportPortal extends UseCase<PortalResponse, PortalVideoModel> {

    public ReportPortal() {
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    public ReportPortal(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<PortalResponse> buildUseCaseObservable(final PortalVideoModel portalVideoModel) {
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
                        portalVideoModel.setAccountId(userModel.getAccount_id());
                        return getRepositoryManager().obtainRemoteService(PortalApi.class)
                                .play(portalVideoModel,userModel.getPortalAccessToken());
                    }
                }).map(new ResponseFunction<PortalResponse, PortalResponse>());
    }

}
