package com.whaley.biz.program.playersupport.component.bannerplayer;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PausedEvent;
import com.whaley.biz.playerui.event.PlayerViewProviderEvent;
import com.whaley.biz.playerui.event.ScreenChangedEvent;
import com.whaley.biz.playerui.event.StartedEvent;
import com.whaley.biz.playerui.event.SwitchScreenEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.playercontroller.IPlayerController;
import com.whaley.biz.program.playersupport.widget.BannerPlayerView;
import com.whaley.core.debug.logger.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by yangzhi on 2017/9/4.
 */

public class BannerTargetController {

    public static final String EVENT_UPDATE_PLAYER_POSITION = "event_update_player_position";

    private View target;

    private CoverListener coverListener;

    private boolean isVisible;

    private boolean isAttached;

    private PlayerViewProviderEvent.PlayerViewProvider playerViewProvider;

    private BannerPlayerView playerView;

    private IPlayerController playerController;

    private PlayData playData;

    private int width = -1;

    private int height = -1;

    private boolean isEnable;

    private Disposable disposable;

    private Runnable runnable;

    private ViewTreeObserver.OnScrollChangedListener onScrollChangeListener = new ViewTreeObserver.OnScrollChangedListener() {
        @Override
        public void onScrollChanged() {
            onScroll();
        }
    };

    public BannerTargetController(View target, CoverListener listener) {
        this.target = target;
        this.coverListener = listener;

    }

    public BannerTargetController(View target, int width, int height, CoverListener listener) {
        this.width = width;
        this.height = height;
        this.target = target;
        this.coverListener = listener;
        this.target.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (playerView != null) {
                    playerView.dispatchTouchEvent(event);
                }
                return true;
            }
        });

    }


    @Subscribe(sticky = true)
    public void onSetBannerPlayerViewProvider(PlayerViewProviderEvent playerViewProviderEvent) {
        this.playerViewProvider = playerViewProviderEvent.getPlayerViewProvider();
    }

    public void attach() {
        if (isAttached())
            return;
        isAttached = true;
        checkEnablePlayerView();
    }

    public void dettach() {
        if (!isAttached())
            return;
        isAttached = false;
        disablePlayerView();
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void destory() {
        dettach();
    }

    public void changeVisible(boolean isVisible) {
        if (isVisible) {
            show();
        } else {
            hide();
        }
    }


    @Subscribe(sticky = true)
    public void onStartedEvent(StartedEvent startedEvent) {
        EventBus.getDefault().post(new ModuleEvent(EVENT_UPDATE_PLAYER_POSITION, null));
        if (getCoverListener() != null) {
            getCoverListener().hideCover();
        }
    }

    @Subscribe(sticky = true)
    public void onPausedEvent(PausedEvent pausedEvent) {

    }


    protected BannerPlayerView onCreatePlayerView() {
        this.playerView = (BannerPlayerView) playerViewProvider.getPlayerView(width, height);
        this.playerController = playerView.getPlayerController();
        return playerView;
    }


    public void setPlayData(PlayData playData) {
        this.playData = playData;
        checkEnablePlayerView();
    }

    private void play() {
        Log.d("BannerPlayerContainer", "play this = " + this);
        PlayData currentPlayData = playerController.getRepository().getCurrentPlayData();
        if (currentPlayData == null
                || !playerController.getRepository().isVideoPrepared()
                || !currentPlayData.equals(playData)) {
            if (getCoverListener() != null) {
                getCoverListener().showCover(false);
            }
        } else {
//            if (getCoverListener() != null) {
//                getCoverListener().hideCover();
//            }
        }
        playerController.setNewPlayDataContinue(playData);
    }

    private void show() {
        if (isVisible()) {
            return;
        }
        isVisible = true;
        checkEnablePlayerView();
    }

    private void checkEnablePlayerView() {
        if (!isVisible() || !isAttached() || playData == null) {
            return;
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                startPlay();
            }
        };
        target.getViewTreeObserver().addOnScrollChangedListener(onScrollChangeListener);
        startDelayToStart();
    }

    private void startPlay() {
        dispose();
        if (!isVisible() || !isAttached() || playData == null) {
            return;
        }
        runnable = null;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        playerView = onCreatePlayerView();
        playerView.setTag(target);
        EventBus.getDefault().post(new ModuleEvent(EVENT_UPDATE_PLAYER_POSITION, null));
        playerView.setPlayerView();
        if (!playerController.getEventBus().isRegistered(this)) {
            playerController.getEventBus().register(this);
        }
        playerView.getBannerComponentsSwitcher().switchByPlayData(playData);
        playerView.registControllers();
        play();
        isEnable = true;
    }

    private void disablePlayerView() {
        if (!isEnable)
            return;
        target.getViewTreeObserver().removeOnScrollChangedListener(onScrollChangeListener);
        runnable = null;
        dispose();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (playerView == null)
            return;
        Log.d("BannerPlayerContainer", "disablePlayerView this = " + this);
        playerView.setTag(null);
        EventBus.getDefault().post(new ModuleEvent(EVENT_UPDATE_PLAYER_POSITION, null));
        playerView.unRegistControllers();
        if (!playerController.getRepository().isVideoPrepared()) {
            playerController.release();
        } else {
            playerController.pause();
        }
        if (playerController.getPlayerView() == playerView) {
            playerController.setPlayerView(null);
        }
        if (playerController.getEventBus().isRegistered(this)) {
            playerController.getEventBus().unregister(this);
        }
        if (getCoverListener() != null) {
            getCoverListener().showCover(false);
        }
        playerViewProvider = null;
        playerView = null;
        playerController = null;
        isEnable = false;
    }

    private void hide() {
        if (!isVisible()) {
            return;
        }
        Log.d("BannerPlayerContainer", "hide this = " + this);
        isVisible = false;
        disablePlayerView();
    }

    public boolean isVisible() {
        return isVisible;
    }


    private void startDelayToStart() {
        if (runnable == null)
            return;
        dispose();
        disposable = Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }


    private void onScroll() {
        startDelayToStart();
    }

    public CoverListener getCoverListener() {
        return coverListener;
    }

    public interface CoverListener {
        void showCover(boolean isAnim);

        void hideCover();
    }
}
