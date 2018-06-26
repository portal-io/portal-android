package com.whaley.biz.program.playersupport.component.liveplayer.liveclose;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.playerui.component.common.init.InitController;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.BlankShowEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.SwicthInitBgVisibleEvent;
import com.whaley.biz.program.playersupport.model.MediaResultInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yangzhi on 2017/8/9.
 */

public class LiveCloseController extends InitController<LiveCloseUIAdapter>{

    static final String SHOW_DANMU_EDIT_EVENT = "/program/event/showdanmuedit";

    static final String HIDE_DANMU_EDIT_EVENT = "/program/event/hidedanmuedit";

    boolean isDanmuEditOnShow;


    @Subscribe
    public void onDanmuEditVisibleChangeEvent(ModuleEvent event){
        String eventName = event.getEventName();
        switch (eventName) {
            case SHOW_DANMU_EDIT_EVENT:
                isDanmuEditOnShow = true;
                getUIAdapter().hide(true);
                break;
            case HIDE_DANMU_EDIT_EVENT:
                isDanmuEditOnShow = false;
                if(!isHide()){
                    getUIAdapter().show(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void showUI(boolean anim) {
        if(isDanmuEditOnShow){
            return;
        }
        super.showUI(anim);
    }

    @Subscribe(sticky = true)
    public void onExitPlay(BaseEvent baseEvent){
        if(baseEvent.getEventType().equals("exitPlay")){
            EventController.removeStickyEvent(baseEvent);
            onCloseClick();
        }
    }

    public void onCloseClick(){
        BackPressEvent backPressEvent = new BackPressEvent();
        emitEvent(backPressEvent);
    }

    @Subscribe
    public void onSwitchInitBgEvent(SwicthInitBgVisibleEvent event){
        if(event.isVisible()){
            getUIAdapter().show(true);
        }
    }

    @Override
    public void registEvents() {
        super.registEvents();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
