package com.whaleyvr.biz.unity.vrone;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by dell on 2017/12/5.
 */

public class PollingController {

    Disposable disposable;

    PollingCallback pollingCallback;

    public PollingController(PollingCallback pollingCallback){
        this.pollingCallback = pollingCallback;
    }

    public void startPolling(){
        stopPolling();
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(@NonNull Long aLong) throws Exception {
                        return pollingCallback != null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(@NonNull Long aLong) {
                        pollingCallback.onPolling();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void stopPolling(){
        if(disposable!=null){
            disposable.dispose();
            disposable = null;
        }
    }

    interface PollingCallback{
        void onPolling();
    }

}
