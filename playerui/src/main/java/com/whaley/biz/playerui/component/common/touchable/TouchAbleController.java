package com.whaley.biz.playerui.component.common.touchable;

import android.view.MotionEvent;
import android.view.View;

import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.BlankClickEvent;
import com.whaley.biz.playerui.event.BlankShowEvent;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.HideEvent;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.RestTouchDurationEvent;
import com.whaley.biz.playerui.event.ShowEvent;
import com.whaley.biz.playerui.event.StartedEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/2 15:23.
 */

public class TouchAbleController extends ControlController<TouchAbleUIAdapter> {

    long unTouchDuration;

    boolean isRegistTouchDuration = true;
    boolean isChangingRegistTouchDuration = false;

    @Subscribe
    public void onBlankClickEvent(BlankClickEvent event) {
        if (isHide()) {
            emitShow();
            return;
        }
        emitHide();
    }

    @Subscribe
    public void onBlankClickEvent(BlankShowEvent event) {
        emitShow();
    }

    public void onLayoutClick() {
        emitEvent(new BlankClickEvent());
    }

    @Subscribe
    public void onPolling(PollingEvent pollingEvent) {
        if (isRegistTouchDuration) {
            unTouchDuration++;
            if (unTouchDuration >= 10) {
                emitHide();
            }
        }
    }

    @Subscribe
    public void onStarted(StartedEvent startedEvent) {
        emitShow();
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent errorEvent) {
        isRegistTouchDuration = false;
    }

    @Subscribe
    public void onCompletedEvent(CompletedEvent completedEvent) {
        isRegistTouchDuration = false;
    }

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        isRegistTouchDuration = false;
    }

    @Subscribe
    public void onRestTouchDurationEvent(RestTouchDurationEvent event) {
        resetDuration();
        if (!isChangingRegistTouchDuration) {
            isRegistTouchDuration = event.isRegistTouchDuration() && !isHide() && getPlayerController().isStarted();
            isChangingRegistTouchDuration = event.isChangingRegistTouchDuration();
        } else if (event.isChangingRegistTouchDuration()) {
            isRegistTouchDuration = event.isRegistTouchDuration() && !isHide() && getPlayerController().isStarted();
            isChangingRegistTouchDuration = false;
        }
    }

    public void resetDuration() {
        unTouchDuration = 0;
    }

    public void emitShow() {
        ShowEvent showEvent = new ShowEvent();
        showEvent.setAnim(true);
        emitEvent(showEvent);
        resetDuration();
        isRegistTouchDuration = getPlayerController().isStarted();
    }

    public void emitHide() {
        HideEvent hideEvent = new HideEvent();
        hideEvent.setAnim(true);
        emitEvent(hideEvent);
        isRegistTouchDuration = false;
    }

    public void dispatchTouchToPlayerView(MotionEvent event) {
        View view = getPlayerController().getPlayerView();
        if (view != null) {
            view.dispatchTouchEvent(event);
        }
    }
}

