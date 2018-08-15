package com.whaley.biz.playerui.component.simpleplayer.bottomcontrol;

import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.BufferingOffEvent;
import com.whaley.biz.playerui.event.BufferingUpdateEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PausedEvent;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.SeekUpdateEvent;
import com.whaley.biz.playerui.event.StartedEvent;
import com.whaley.biz.playerui.event.SwitchScreenEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.State;
import com.whaley.biz.playerui.utils.StringFormatUtil;
import com.whaley.wvrplayer.vrplayer.external.event.AbsVRPlayer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by YangZhi on 2017/8/2 14:23.
 */

public class BottomControlController<UIADAPTER extends BottomControlUIAdapter> extends ControlController<UIADAPTER> {

    private boolean isSeekTouched;

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        long duration = getPlayerController().getRepository().getDuration();
        getUIAdapter().changeSeekMax(duration);
        getUIAdapter().updateTotalTimeText(StringFormatUtil.formatTime(duration));

        duration = getPlayerController().getCurrentProgress();
        getUIAdapter().changeSeekProgress(duration);
        getUIAdapter().updateCurrentTimeText(StringFormatUtil.formatTime(duration));
    }

//    @Subscribe
//    public void onSeekUpdate(SeekUpdateEvent seekUpdateEvent) {
//        getUIAdapter().changeSeekProgress(seekUpdateEvent.getCurrentProgress());
//    }

    @Subscribe
    public void onBufferingUpdate(BufferingUpdateEvent bufferingUpdateEvent) {
        getUIAdapter().changeSeekSecondProgress(bufferingUpdateEvent.getBufferProgress());
    }
    @Subscribe
    public void onModuleEvent(ModuleEvent moduleEvent) {
        if ("event/chunjie/activity".equals(moduleEvent.getEventName())) {
            if ((boolean) moduleEvent.getParam()){
                getUIAdapter().showActivityIcon();
            }
            else {
                getUIAdapter().hideActivityIcon();
            }
        }
    }

    @Subscribe
    public void onPolling(PollingEvent pollingEvent) {
        if (getPlayerController().isStarted() && !isSeekTouched()) {
            long duration = getPlayerController().getCurrentProgress();
            getUIAdapter().changeSeekProgress(duration);
            getUIAdapter().updateCurrentTimeText(StringFormatUtil.formatTime(duration));
        }
    }

    @Subscribe(sticky = true)
    public void onStarted(StartedEvent startedEvent) {
        getUIAdapter().changeStartPauseBtn(true);
    }

    @Subscribe(sticky = true)
    public void onPaused(PausedEvent pausedEvent) {
        getUIAdapter().changeStartPauseBtn(false);
    }

    public void onStartPauseClick() {
        if (isForbidClick()) {
            return;
        }
        if (getPlayerController().isStarted()) {
            getPlayerController().pause();
        } else {
            getPlayerController().start();
        }
    }

    public void onSwitchScreenClick() {
        SwitchScreenEvent switchScreenEvent = new SwitchScreenEvent();
        emitEvent(switchScreenEvent);
    }

    public void onSeekChanging(long progress) {
        if(getUIAdapter()!=null) {
            getUIAdapter().updateCurrentTimeText(StringFormatUtil.formatTime(progress));
        }
    }

    protected boolean isForbidClick(){
        return getPlayerController().getRepository().isOnBuffering()||getPlayerController().getState().getCurrentState()==State.STATE_INIT||getPlayerController().getState().getCurrentState()==State.STATE_PREPARING;
    }

    public void onStartSeekTouch() {
        isSeekTouched = true;
    }

    public void onSeekChanged(long progress) {
        getPlayerController().changeCurentProgress(progress);
        isSeekTouched = false;
    }

    public boolean isSeekTouched() {
        return isSeekTouched;
    }

}
