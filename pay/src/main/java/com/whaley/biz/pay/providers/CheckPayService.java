package com.whaley.biz.pay.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.pay.interactor.IsPayed;
import com.whaley.biz.pay.model.GoodsModel;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RemoteRepository;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/22.
 */

@Route(path = "/pay/service/checkpay")
public class CheckPayService implements Executor<GoodsModel> {

    @Override
    public void init(Context context) {
    }

    @Override
    public void excute(GoodsModel o, final Callback callback) {
        IsPayed isPayed = new IsPayed(RepositoryManager.create(RemoteRepository.getInstance()), Schedulers.io(), AndroidSchedulers.mainThread());
        IsPayed.GoodsPayedParam payedParam = new IsPayed.GoodsPayedParam();
        payedParam.setGoodsNo(o.getGoodsNo());
        payedParam.setGoodsType(o.getGoodsType());
        isPayed.execute(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                callback.onCall(aBoolean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                callback.onFail(new ExecutionError(e.getMessage(), e) {

                });
            }

            @Override
            public void onComplete() {

            }
        }, payedParam);
    }
}
