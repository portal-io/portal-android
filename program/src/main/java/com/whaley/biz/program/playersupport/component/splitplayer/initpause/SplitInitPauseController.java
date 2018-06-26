package com.whaley.biz.program.playersupport.component.splitplayer.initpause;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PausedEvent;
import com.whaley.biz.playerui.event.StartedEvent;
import com.whaley.biz.playerui.model.State;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dell on 2017/11/9.
 */

public class SplitInitPauseController extends BaseController<SplitInitPauseUIAdapter> {

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        if(getPlayerController()!=null&&getPlayerController().getState().getCurrentState()== State.STATE_PAUSED) {
            if(getUIAdapter()!=null) {
                getUIAdapter().show();
            }
        }
    }

    @Subscribe(sticky = true)
    public void onStarted(StartedEvent startedEvent) {
        if(getUIAdapter()!=null) {
            getUIAdapter().hide();
        }
    }

    @Subscribe(sticky = true)
    public void onPaused(PausedEvent pausedEvent) {
        if(getUIAdapter()!=null) {
            getUIAdapter().show();
        }
    }

    @Subscribe
    public void onBuffering(BufferingEvent bufferingEvent){
        if(getUIAdapter()!=null) {
            getUIAdapter().hide();
        }
    }

    @Subscribe
    public void onModelEvent(ModuleEvent event) {
        if ("/program/local/mobilemodel".equals(event.getEventName())) {
            if ((boolean) event.getParam()) {
                if(getUIAdapter()!=null) {
                    getUIAdapter().hidePause();
                }
            } else {
                if(getUIAdapter()!=null) {
                    getUIAdapter().showPause();
                }
            }
        }
    }

}
