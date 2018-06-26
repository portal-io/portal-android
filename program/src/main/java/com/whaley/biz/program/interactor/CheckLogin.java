package com.whaley.biz.program.interactor;

import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/14
 * Introduction:
 */

public class CheckLogin extends UseCase<UserModel, Void> {

    public CheckLogin() {
        super();
    }

    public CheckLogin(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<UserModel> buildUseCaseObservable(Void s) {
        return Observable.create(new ObservableOnSubscribe<UserModel>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<UserModel> e) throws Exception {
                Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
                    @Override
                    public void onCall(UserModel userModel) {
                        if (e.isDisposed()) {
                            return;
                        }
                        userModel.setLoginUser(true);
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
