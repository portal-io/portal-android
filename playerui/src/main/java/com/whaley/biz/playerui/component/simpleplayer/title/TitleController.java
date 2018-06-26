package com.whaley.biz.playerui.component.simpleplayer.title;

import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.StartedEvent;
import com.whaley.biz.playerui.event.SwicthInitBgVisibleEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/3 19:20.
 */

public class TitleController<T extends TitleUIAdapter> extends ControlController<T> {

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        getUIAdapter().show(true);
        getUIAdapter().updateTitleText(preparingEvent.getPlayData().getTitle());
    }

    @Subscribe(sticky = true)
    public void onStartEvent(StartedEvent startedEvent){
        getUIAdapter().updateTitleText(startedEvent.getPlayData().getTitle());
    }

    @Subscribe(sticky = true)
    public void onVideoPreparedEvent(VideoPreparedEvent videoPreparedEvent){
        getUIAdapter().updateTitleText(videoPreparedEvent.getPlayData().getTitle());
    }

    @Subscribe(sticky = true)
    public void onErrorEvent(ErrorEvent errorEvent){
        getUIAdapter().show(true);
    }

    @Subscribe
    public void onSwitchInitBgVisibleEvent(SwicthInitBgVisibleEvent event){
        if(event.isVisible()) {
            getUIAdapter().show(true);
        }
    }

    @Subscribe(sticky = true)
    public void onCompeletedEvent(CompletedEvent completedEvent){
        getUIAdapter().show(true);
    }

    public void onBackClick(){
        BackPressEvent backPressEvent = new BackPressEvent();
        emitEvent(backPressEvent);
    }

    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    @Override
    protected void onSwitchToLandscape() {
        super.onSwitchToLandscape();
        getUIAdapter().showTitleText();
    }

    @Override
    protected void onSwitchToProtrait() {
        super.onSwitchToProtrait();
        getUIAdapter().hideTitleText();
    }
}
