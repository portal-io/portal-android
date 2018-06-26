package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.setting.model.user.UserModel;
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

public class CheckLogin extends UseCase<UserModel, String> {

    public CheckLogin() {

    }

    public CheckLogin(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<UserModel> buildUseCaseObservable(String s) {
        return Observable.create(new ObservableOnSubscribe<UserModel>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<UserModel> e) throws Exception {
                Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
                    @Override
                    public void onCall(UserModel userModel) {
                        userModel.setLoginUser(true);
                        if (e.isDisposed())
                            return;
                        e.onNext(userModel);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        if (e.isDisposed())
                            return;
                        e.onNext(new UserModel());
                        e.onComplete();
                    }
                }).excute();
            }
        });
    }
}
