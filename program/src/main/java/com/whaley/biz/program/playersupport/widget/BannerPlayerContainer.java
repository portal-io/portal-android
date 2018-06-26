package com.whaley.biz.program.playersupport.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.whaley.biz.playerui.event.BufferingOffEvent;
import com.whaley.biz.playerui.event.PausedEvent;
import com.whaley.biz.playerui.event.StartedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.playercontroller.IPlayerController;
import com.whaley.core.debug.logger.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/29 16:57.
 */

public class BannerPlayerContainer extends FrameLayout {

    private BannerPlayerView bannerPlayerView;

    private IPlayerController playerController;

    private Rect rect = new Rect();

    private PlayData lastSetPlayData;

    private Listener listener;

    private boolean isShowed;

    public BannerPlayerContainer(Context context) {
        this(context, null);
    }

    public BannerPlayerContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerPlayerContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public void regist() {
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void unRegist(){
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(sticky = true)
    public void onSetBannerPlayerView(BannerPlayerView bannerPlayerView) {
        this.bannerPlayerView = bannerPlayerView;
        this.playerController = bannerPlayerView.getPlayerController();
        if (!playerController.getEventBus().isRegistered(this)) {
            playerController.getEventBus().register(this);
        }
    }

    @Subscribe(sticky = true)
    public void onStartedEvent(StartedEvent startedEvent) {
        if (getListener() != null) {
            getListener().hideCover();
        }
    }

    @Subscribe(sticky = true)
    public void onPausedEvent(PausedEvent pausedEvent) {
        if (getListener() != null) {
            getListener().showCover();
        }
    }

    @Subscribe(sticky = true)
    public void onBufferingOffEvent(BufferingOffEvent event){
        if (getListener() != null) {
            getListener().hideCover();
        }
    }


    public void setNewPlayData(PlayData playData) {
        if (playerController != null) {
            if (isShowed) {
                PlayData currentPlayData = playerController.getRepository().getCurrentPlayData();
                if (currentPlayData == null || !playerController.getRepository().isVideoPrepared() || !currentPlayData.equals(playData)) {
                    if (getListener() != null) {
                        getListener().showCover();
                    }
                } else {
                    if (getListener() != null) {
                        getListener().hideCover();
                    }
                }
                playerController.setNewPlayDataContinue(playData);
            }
            lastSetPlayData = playData;
        }
    }

    public void show() {
        if (isShowed) {
            return;
        }
        Log.d("BannerPlayerContainer", "show this= "+this);
        isShowed = true;
        if (!playerController.getEventBus().isRegistered(this)) {
            playerController.getEventBus().register(this);
        }
//        ViewGroup parent = (ViewGroup) bannerPlayerView.getParent();
//        if (parent != this) {
//            if (parent != null) {
//                parent.removeView(bannerPlayerView);
//            }
//            addView(bannerPlayerView);
//        }


        bannerPlayerView.setTag(this);
        bannerPlayerView.setPlayerView();
        bannerPlayerView.registControllers();
        if (lastSetPlayData != null) {
            setNewPlayData(lastSetPlayData);
        }
    }

    public void hide() {
        if (!isShowed) {
            return;
        }
        Log.d("BannerPlayerContainer", "hide this= "+this);
        isShowed = false;
        bannerPlayerView.setTag(null);
        if(!playerController.getRepository().isVideoPrepared()) {
            playerController.pause();
        }else {
            playerController.release();
        }
        bannerPlayerView.unRegistControllers();
        if (playerController.getPlayerView() == bannerPlayerView) {
            playerController.setPlayerView(null);
        }
        if (playerController.getEventBus().isRegistered(this)) {
            playerController.getEventBus().unregister(this);
        }
        if (getListener() != null) {
            getListener().showCover();
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Listener getListener() {
        return listener;
    }

    public interface Listener {

        void hideCover();

        void showCover();

    }
}
