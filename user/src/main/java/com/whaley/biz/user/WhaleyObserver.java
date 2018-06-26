package com.whaley.biz.user;

import com.whaley.biz.user.model.response.WhaleyResponse;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Author: qxw
 * Date: 2017/7/13
 */

public class WhaleyObserver<T> implements Observer<WhaleyResponse<T>> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull WhaleyResponse<T> tWhaleyResponse) {
        if (tWhaleyResponse.checkStatus()) {
            onSuccess(tWhaleyResponse, tWhaleyResponse.getData());
        } else {
            onStatusError(tWhaleyResponse.getStatus(), tWhaleyResponse);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    public void onSuccess(WhaleyResponse<T> response, T date) {
        onSuccess(date);
    }

    public void onSuccess(T date) {
    }

    public void onStatusError(int status, String msg) {
    }

    public void onStatusError(int status, WhaleyResponse<T> response) {
        onStatusError(status, response.getMsg());
    }
}
