package com.whaley.biz.pay.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.pay.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;

/**
 * Author: qxw
 * Date:2017/8/14
 * Introduction:
 */

public class CheckLogin extends UseCase<UserModel, Void> {

    public CheckLogin() {

    }

    public CheckLogin(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<UserModel> buildUseCaseObservable(Void v) {
        return Observable.create(new ObservableOnSubscribe<UserModel>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<UserModel> e) throws Exception {
                Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
                    @Override
                    public void onCall(UserModel userModel) {
                        userModel.setLoginUser(true);
                        e.onNext(userModel);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        e.onError(new NotLoggedInErrorException("未登录"));
                    }
                }).excute();
            }
        });
    }

}
