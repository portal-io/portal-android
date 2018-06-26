package com.whaley.biz.program.playersupport.component.splitplayer.playpause;

import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PausedEvent;
import com.whaley.biz.playerui.event.StartedEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitPlayPauseController extends ControlController<SplitPlayPauseUIAdapter> {


    @Subscribe(sticky = true)
    public void onStarted(StartedEvent startedEvent) {
        getUIAdapter().changeStartPauseBtn(true);
    }

    @Subscribe(sticky = true)
    public void onPaused(PausedEvent pausedEvent) {
        getUIAdapter().changeStartPauseBtn(false);
    }

    public void onStartPauseClick() {
        if (getPlayerController().isStarted()) {
            getPlayerController().pause();
        } else {
            getPlayerController().start();
        }
    }

    @Subscribe
    public void onModelEvent(ModuleEvent event) {
        if ("/program/local/mobilemodel".equals(event.getEventName())) {
            if ((boolean) event.getParam()) {
                getUIAdapter().hidePause();
            } else {
                getUIAdapter().showPause();
            }
        }
    }

}
