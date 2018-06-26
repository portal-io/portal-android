package com.whaley.biz.pay.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.pay.interactor.GetUserAmount;
import com.whaley.biz.pay.model.UserCostModel;
import com.whaley.core.router.Executor;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by dell on 2017/10/18.
 */

@Route(path = "/pay/service/useramount")
public class UserAmountService implements Executor<UserCostModel> {



    @Override
    public void excute(UserCostModel userCostModel, final Callback callback) {
        GetUserAmount getUserAmount = new GetUserAmount();
        getUserAmount.execute(new DisposableObserver<Integer>() {
            @Override
            public void onNext(@NonNull Integer integer) {
                if(callback!=null){
                    callback.onCall(integer.intValue());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if(callback!=null){
                    callback.onFail(new ExecutionError("", e) {
                    });
                }
            }

            @Override
            public void onComplete() {
                //
            }
        }, null);
    }

    @Override
    public void init(Context context) {

    }

}

