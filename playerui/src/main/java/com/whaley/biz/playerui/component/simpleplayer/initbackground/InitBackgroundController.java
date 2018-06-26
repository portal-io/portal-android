package com.whaley.biz.playerui.component.simpleplayer.initbackground;

import com.whaley.biz.playerui.component.common.init.InitController;
import com.whaley.biz.playerui.event.SwicthInitBgVisibleEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.State;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/2 20:03.
 */

public class InitBackgroundController<UIADAPTER extends InitBackgroundUIAdapter> extends InitController<UIADAPTER> {

    @Override
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        if(getPlayerController().getState().getCurrentState() != State.STATE_COMPLETED && getPlayerController().getState().getCurrentState() != State.STATE_ERROR) {
            super.onVideoPrepared(videoPreparedEvent);
        }
    }

    @Subscribe
    public void onSwitchInitBgVisibleEvent(SwicthInitBgVisibleEvent swicthInitBgVisibleEvent){
        if(swicthInitBgVisibleEvent.isVisible()){
            getUIAdapter().show();
        }else {
            getUIAdapter().hide();
        }
    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        getUIAdapter().hide();
    }
}
