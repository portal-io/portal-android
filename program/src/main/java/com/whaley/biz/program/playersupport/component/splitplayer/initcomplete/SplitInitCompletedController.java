package com.whaley.biz.program.playersupport.component.splitplayer.initcomplete;

import android.util.Log;

import com.whaley.biz.playerui.component.simpleplayer.initCompleted.InitCompletedController;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.model.SeriesModel;
import com.whaley.biz.program.playersupport.event.ChangeSerieEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by dell on 2017/10/31.
 */

public class SplitInitCompletedController extends InitCompletedController<SplitInitCompletedUIAdapter> {

    protected String getStrNext() {
        return "%d秒后即将播放";
    }

    protected String getStrReplay() {
        return "%d秒后重播";
    }

    boolean isLock;
    boolean isSplitScreen;

    public SplitInitCompletedController(boolean isLock) {
        this.isLock = isLock;
    }

    @Subscribe
    public void onModelEvent(ModuleEvent event) {
        if ("/program/local/mobilemodel".equals(event.getEventName())) {
            if ((boolean) event.getParam()) {
                isSplitScreen = false;
            } else {
                isSplitScreen = true;
            }
        }
    }

    @Override
    protected String getNextPlayDataTitle(PlayData nextPlayData) {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData != null && playData.getType() == PlayerType.TYPE_MORETV_TV) {
            return ((SeriesModel) playData.getCustomData(PlayerDataConstants.TV_NEXT_PLAY_SERIES)).getDisplayName();
        }
        return super.getNextPlayDataTitle(nextPlayData);
    }

    @Override
    protected boolean hasNext() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData != null && playData.getType() == PlayerType.TYPE_MORETV_TV) {
            return playData.getCustomData(PlayerDataConstants.TV_NEXT_PLAY_SERIES) != null;
        }
        return super.hasNext();
    }

    @Override
    protected void playNext() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData != null && playData.getType() == PlayerType.TYPE_MORETV_TV) {
            ChangeSerieEvent changeSerieEvent = new ChangeSerieEvent();
            changeSerieEvent.setChangeToNext(true);
            emitEvent(changeSerieEvent);
            return;
        }
        super.playNext();
    }

    protected void startCountDown(final String str, final PlayData playData, final Runnable runnable) {
        stopCountDown();
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        String formatStr = String.format(str, 5);
                        getUIAdapter().updateCompleteText(formatStr, getNextPlayDataTitle(playData));
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        String formatStr = String.format(str, 4 - aLong);
                        getUIAdapter().updateCompleteText(formatStr, getNextPlayDataTitle(playData));
                        if (aLong >= 4) {
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

}
