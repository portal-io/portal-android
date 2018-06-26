package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.function.RetryWhenRefreshTokenFunction;
import com.whaley.biz.user.api.UserInfoApi;
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
 * Introduction:修改密码
 */

public class UpdatePassword extends BaseUseCase<String, UpdatePassword.UpdatePasswordParam> {


    public UpdatePassword(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(final UseCaseParam<UpdatePasswordParam> param) {
        return getRepositoryManager()
                .obtainRemoteService(UserInfoApi.class)
                .updatePWD(UserManager.getInstance().getDeviceId(),
                        UserManager.getInstance().getAccesstoken(),
                        param.getParam().oldPwd,
                        param.getParam().newPwd)
                .map(new CommonFunction<WhaleyStringResponse, String>())
                .retryWhen(new RetryWhenRefreshTokenFunction<Observable<Throwable>, String>() {
                               @Override
                               public ObservableSource<String> retryWhenThrowable() {
                                   return buildUseCaseObservable(param);
                               }
                           }
                ).doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        UserManager.getInstance().removeUser();
                    }
                });
    }


    public void execute(DisposableObserver<String> observer, String oldPwd, String newPwd) {
        UpdatePasswordParam param = new UpdatePasswordParam(oldPwd, newPwd);
        execute(observer, new UseCaseParam<>(param));
    }

    public static class UpdatePasswordParam {
        private String oldPwd;
        private String newPwd;

        UpdatePasswordParam(String oldPwd, String newPwd) {
            this.oldPwd = oldPwd;
            this.newPwd = newPwd;
        }

        public String getOldPwd() {
            return oldPwd;
        }

        public void setOldPwd(String oldPwd) {
            this.oldPwd = oldPwd;
        }

        public String getNewPwd() {
            return newPwd;
        }

        public void setNewPwd(String newPwd) {
            this.newPwd = newPwd;
        }
    }
}
