package com.whaley.biz.pay.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.pay.interactor.ReportDevice;
import com.whaley.biz.pay.response.DeviceResponse;
import com.whaley.core.router.Executor;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by dell on 2017/8/28.
 */

@Route(path = "/pay/usecase/reportdevice")
public class ReportDeviceService implements Executor{
    @Override
    public void excute(Object o, final Callback callback) {
        ReportDevice reportDevice = new ReportDevice();
        reportDevice.execute(new ErrorHandleObserver<DeviceResponse>() {

            @Override
            public void onNext(@NonNull DeviceResponse deviceResponse) {

            }

            @Override
            public void onComplete() {
                callback.onCall(true);
            }

            @Override
            public void onFinalError(Throwable e) {
                callback.onCall(false);
            }

            @Override
            public void onStatusError(int status, String message) {
                callback.onCall(true);
            }

            @Override
            public void onNoDataError() {
                callback.onCall(false);
            }
        });
    }

    @Override
    public void init(Context context) {

    }
}
