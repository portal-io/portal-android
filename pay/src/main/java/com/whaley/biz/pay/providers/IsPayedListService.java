package com.whaley.biz.pay.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.pay.interactor.IsPayed;
import com.whaley.biz.pay.interactor.IsPayedList;
import com.whaley.biz.pay.model.GoodsModel;
import com.whaley.biz.pay.model.program.PayResultModel;
import com.whaley.core.repository.RemoteRepository;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.utils.GsonUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/9/7.
 */

@Route(path = "/pay/service/ispayedlist")
public class IsPayedListService implements Executor<IsPayedList.IsPayedListModel> {

    @Override
    public void init(Context context) {
    }

    @Override
    public void excute(IsPayedList.IsPayedListModel o, final Callback callback) {
        IsPayedList isPayedList = new IsPayedList(RepositoryManager.create(RemoteRepository.getInstance()), Schedulers.io(), AndroidSchedulers.mainThread());
        isPayedList.execute(new DisposableObserver<List<PayResultModel>>() {
            @Override
            public void onNext(@NonNull List<PayResultModel> payResultModels) {
                callback.onCall(payResultModels);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                callback.onFail(new ExecutionError(e.getMessage(), e) {
                });
            }

            @Override
            public void onComplete() {

            }
        }, o);
    }
}
