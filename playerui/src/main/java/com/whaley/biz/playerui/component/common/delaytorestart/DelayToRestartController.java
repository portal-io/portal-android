package com.whaley.biz.playerui.component.common.delaytorestart;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.NetworkChangedEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.exception.NetworkPlayerException;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.core.utils.NetworkUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by YangZhi on 2017/8/7 20:52.
 */

public class DelayToRestartController extends BaseController {

    //等待5秒
    private static final int RESTART_WAIT_TIME = 5;

    int networkState;

    Disposable disposable;

    boolean isNetworkStateChanged;

    @Subscribe
    public void onVideoPreparedEvent(VideoPreparedEvent videoPreparedEvent) {
        dispose();
        isNetworkStateChanged = false;
        networkState = NetworkUtils.getNetworkState();
    }

    @Subscribe
    public void onNetworkStateChanged(NetworkChangedEvent networkChangedEvent) {
        int state = networkChangedEvent.getNetworkState();
        if (networkState != state) {
//            if (state != NetworkUtils.NONE) {
//                delayToReStartPlay();
//            }
            isNetworkStateChanged = true;
            networkState = state;
        }
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent) {
        if(errorEvent.getPlayerException() == null || !(errorEvent.getPlayerException() instanceof PlayerException)) {
            dispose();
        }
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        dispose();
    }

    @Subscribe
    public void onBufferingEvent(BufferingEvent event){
        if(isNetworkStateChanged){
            delayToReStartPlay();
        }
    }

    private void delayToReStartPlay() {
        dispose();
        disposable = Observable.timer(RESTART_WAIT_TIME, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(@NonNull Long aLong) {
                        if (NetworkUtils.getNetworkState() != NetworkUtils.NONE) {
                            getPlayerController().reStartPlay();
                        } else {
                            getPlayerController().pause();
                            getPlayerController().setError(new NetworkPlayerException());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        dispose();
    }
}
