package com.whaley.biz.user.interactor;


import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.function.RetryWhenRefreshTokenFunction;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:
 */

public class ThirdUnbind extends BaseUseCase<String, String> {

    public ThirdUnbind(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }


    @Override
    public Observable<String> buildUseCaseObservable(final UseCaseParam<String> param) {
        return getRepositoryManager()
                .obtainRemoteService(UserVrApi.class)
                .unbind(UserManager.getInstance().getDeviceId(),
                        UserManager.getInstance().getAccesstoken(),
                        param.getParam())
                .map(new CommonFunction<WhaleyStringResponse, String>())
                .retryWhen(new RetryWhenRefreshTokenFunction<Observable<Throwable>, String>() {
                    @Override
                    public ObservableSource<String> retryWhenThrowable() {
                        return buildUseCaseObservable(param);
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        UserModel userModel = UserManager.getInstance().getUser();
                        switch (param.getParam()) {
                            case UserConstants.TYPE_AUTH_WX:
                                userModel.setWx(null);
                                break;
                            case UserConstants.TYPE_AUTH_WB:
                                userModel.setWb(null);
                                break;
                            case UserConstants.TYPE_AUTH_QQ:
                                userModel.setQq(null);
                                break;
                        }
                        UserManager.getInstance().saveUser(userModel);
                    }
                });
    }

    public void execute(DisposableObserver<String> observer, String type) {
        execute(observer, new UseCaseParam<>(type));
    }
}
