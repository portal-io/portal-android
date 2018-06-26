package com.whaley.biz.pay.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.pay.interactor.IsPayed;
import com.whaley.biz.pay.interactor.PayMethodList;
import com.whaley.biz.pay.model.GoodsModel;
import com.whaley.biz.pay.model.program.PayMethodModel;
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

@Route(path = "/pay/service/checkpaymethod")
public class CheckPayMethodService implements Executor {

    @Override
    public void init(Context context) {
    }

    @Override
    public void excute(Object o, final Callback callback) {
        PayMethodList payMethodList = new PayMethodList(RepositoryManager.create(RemoteRepository.getInstance()), Schedulers.io(), AndroidSchedulers.mainThread());
        payMethodList.execute(new DisposableObserver<PayMethodModel>() {
            @Override
            public void onNext(@NonNull PayMethodModel payMethodModel) {
                callback.onCall("");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                callback.onCall("");
            }

            @Override
            public void onComplete() {
                //
            }
        }, null);
    }
}
