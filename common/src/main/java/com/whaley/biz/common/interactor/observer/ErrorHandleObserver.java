package com.whaley.biz.common.interactor.observer;

import com.whaley.biz.common.exception.NoNetworkErrorException;
import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.exception.ResponseErrorException;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.utils.ErrorHandleUtil;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.NetworkUtils;
import com.whaleyvr.core.network.http.exception.NetworkErrorException;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date: 2017/7/14
 */

public abstract class ErrorHandleObserver<T> extends DisposableObserver<T> {

    @Override
    public void onError(@NonNull Throwable e) {
        if (e == null) {
            onThrowableNull(e);
            return;
        }
        if (!NetworkUtils.isNetworkAvailable()) {
            e = new NoNetworkErrorException("没有网络信号，请检查网络配置后重试", e);
            onFinalError(e);
            return;
        }
        if (e instanceof ResponseErrorException) {
            onNoDataError();
            return;
        }
        if (e instanceof StatusErrorThrowable) {
            StatusErrorThrowable statusErrorThrowable = (StatusErrorThrowable) e;
            try {
                T data = (T) statusErrorThrowable.getData();
                onStatusError(statusErrorThrowable.getStatus(), statusErrorThrowable.getMessage(),
                        statusErrorThrowable.getSubCode(), data);
            } catch (Exception ex) {
                onStatusError(statusErrorThrowable.getStatus(), statusErrorThrowable.getMessage(),
                        statusErrorThrowable.getSubCode(), null);
            }
            return;
        }
        if (e instanceof NotLoggedInErrorException) {
            onNotLoggedInError();
            return;
        }
        Throwable t = getFormatThrowable(e);
        if (t == null) {
            onThrowableNull(e);
            return;
        }
        onFinalError(t);

    }

    private void onThrowableNull(Throwable e) {
        Log.e((e != null ? e : new Throwable("null")), "throwableNull");
    }

    protected Throwable getFormatThrowable(Throwable t) {
        return ErrorHandleUtil.getFormatThrowable(t);
    }

    public abstract void onFinalError(Throwable e);

    public abstract void onStatusError(int status, String message);

    public void onNotLoggedInError() {

    }

    public void onStatusError(int status, String message, String subCode, T data) {
        onStatusError(status, message, data);
    }

    public void onStatusError(int status, String message, T data) {
        onStatusError(status, message);
    }

    public abstract void onNoDataError();
}
