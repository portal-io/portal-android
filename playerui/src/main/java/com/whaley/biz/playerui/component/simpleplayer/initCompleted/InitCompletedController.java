package com.whaley.biz.playerui.component.simpleplayer.initCompleted;

import com.whaley.biz.playerui.component.common.init.InitController;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.model.PlayData;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by YangZhi on 2017/8/7 22:08.
 */

public class InitCompletedController<T extends InitCompletedUIAdapter> extends InitController<T> {

    protected Disposable disposable;

    @Override
    public void onCompletedEvent(CompletedEvent completedEvent) {
        super.onCompletedEvent(completedEvent);
        getUIAdapter().showOrHideLayoutNext(hasNext());
        PlayData playData;
        String str;
        Runnable runnable;
        if(hasNext()){
            playData = getPlayerController().getRepository().getNextPlayData();
            str = getStrNext();
            runnable = new Runnable() {
                @Override
                public void run() {
                    onPlayNextClick();
                }
            };
            startCountDown(str,playData,runnable);
        }else{
            playData = getPlayerController().getRepository().getCurrentPlayData();
            getUIAdapter().updateCompleteText(getNextPlayDataTitle(playData));
        }
//        else {
//            playData = getPlayerController().getRepository().getCurrentPlayData();
//            str = getStrReplay();
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//                    onPlayReturnClick();
//                }
//            };
//        }
//        startCountDown(str,playData,runnable);
    }

    protected String getStrNext(){
        return "%d秒后即将播放:%s";
    }

    protected String getStrReplay(){
        return "%d秒后重播:%s";
    }

    protected boolean hasNext(){
        return getPlayerController().getRepository().hasNextPlayData();
    }

    protected void startCountDown(final String str, final PlayData playData, final Runnable runnable){
        stopCountDown();
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        String formatStr = String.format(str,5,getNextPlayDataTitle(playData));
                        getUIAdapter().updateCompleteText(formatStr);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        String formatStr = String.format(str,4-aLong,getNextPlayDataTitle(playData));
                        getUIAdapter().updateCompleteText(formatStr);
                        if(aLong >=4){
                            runnable.run();
                            stopCountDown();
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

    protected String getNextPlayDataTitle(PlayData playData){
        return playData.getTitle();
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        stopCountDown();
    }

    protected void stopCountDown(){
        if(disposable != null){
            disposable.dispose();
            disposable = null;
        }
    }

    public void onPlayNextClick(){
        stopCountDown();
        playNext();
    }

    protected void playNext(){
        getPlayerController().playNext();
    }

    public void onPlayReturnClick(){
        stopCountDown();
        getPlayerController().replay();
    }

    @Override
    protected void onDestory() {
        onDispose();
        super.onDestory();
    }
}
