package com.whaley.biz.program.interactor;

import com.whaley.biz.program.model.pay.GoodsModel;
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
 * Date:2017/8/22
 * Introduction:
 */

public class IsPayed extends UseCase<Boolean, IsPayed.Param> {

    public IsPayed() {
    }

    public IsPayed(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<Boolean> buildUseCaseObservable(final Param param) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Boolean> e) throws Exception {
                Router.getInstance().buildExecutor("/pay/service/checkpay").putObjParam(new GoodsModel(param.code, param.type)).callback(new Executor.Callback<Boolean>() {
                    @Override
                    public void onCall(Boolean b) {
                        e.onNext(b);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        e.onNext(false);
                        e.onComplete();
                    }
                }).excute();
            }
        });
    }

    public static class Param {
        private String code;
        private String type;

        public Param(String code, String type) {
            this.code = code;
            this.type = type;
        }
    }
}
