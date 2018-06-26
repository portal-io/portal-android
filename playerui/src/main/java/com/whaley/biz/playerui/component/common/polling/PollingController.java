package com.whaley.biz.playerui.component.common.polling;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.PreparingEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import retrofit2.http.POST;

/**
 * Created by YangZhi on 2017/8/2 17:35.
 */

public class PollingController extends BaseController{

    Disposable disposable;

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
//        super.onPreparing(preparingEvent);
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        stopPolling();
    }


    @Override
    protected void onDispose() {
        super.onDispose();
        stopPolling();
    }

    @Override
    public void registEvents() {
        super.registEvents();
        startPolling();
    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        stopPolling();
    }

    private void startPolling(){
        stopPolling();
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(@NonNull Long aLong) throws Exception {
                        return getEventBus().hasSubscriberForEvent(PollingEvent.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(@NonNull Long aLong) {
                        PollingEvent pollingEvent = new PollingEvent();
                        pollingEvent.setTotalPolling(aLong);
                        emitEvent(pollingEvent);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void stopPolling(){
        if(disposable!=null){
            disposable.dispose();
            disposable = null;
        }
    }
}
