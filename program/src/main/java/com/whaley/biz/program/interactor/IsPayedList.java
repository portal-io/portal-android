package com.whaley.biz.program.interactor;

import com.whaley.biz.program.model.pay.GoodsModel;
import com.whaley.biz.program.model.pay.PayResultModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

import java.util.ArrayList;
import java.util.List;

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

public class IsPayedList extends UseCase<List<PayResultModel>, IsPayedList.Param> {

    public IsPayedList() {
    }

    public IsPayedList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<List<PayResultModel>> buildUseCaseObservable(final Param param) {
        return Observable.create(new ObservableOnSubscribe<List<PayResultModel>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<PayResultModel>> e) throws Exception {
                Router.getInstance().buildExecutor("/pay/service/ispayedlist").putObjParam(param).callback(new Executor.Callback<List<PayResultModel>>() {
                    @Override
                    public void onCall(List<PayResultModel> resultModels) {
                        e.onNext(resultModels);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        e.onNext(new ArrayList<PayResultModel>());
                        e.onComplete();
                    }
                }).excute();
            }
        });
    }

    public static class Param {
        private String goodsNos;
        private String goodsTypes;

        public Param(String goodsNos, String goodsTypes) {
            this.goodsNos = goodsNos;
            this.goodsTypes = goodsTypes;
        }
    }
}
