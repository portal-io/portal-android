package com.whaley.biz.pay.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.pay.interactor.CheckDevice;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;

import io.reactivex.annotations.NonNull;

/**
 * Created by dell on 2017/8/28.
 */

@Route(path = "/pay/usecase/checkdevice")
public class CheckDeviceService implements Executor {
    @Override
    public void excute(Object o, final Callback callback) {
        CheckDevice checkDevice = new CheckDevice();
        checkDevice.execute(new ErrorHandleObserver<Boolean>() {
            @Override
            public void onFinalError(Throwable e) {
                callback.onCall(true);
            }

            @Override
            public void onStatusError(int status, String message) {
                callback.onCall(false);
            }

            @Override
            public void onNoDataError() {
                callback.onCall(true);
            }

            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                callback.onCall(aBoolean);
            }

            @Override
            public void onComplete() {

            }
            @Override
            public void onNotLoggedInError() {
                callback.onCall(false);
            }
        });
    }

    @Override
    public void init(Context context) {

    }
}
