package com.whaley.biz.playerui.component.common.init;

import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.exception.PlayerException;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/3 16:34.
 */

public class InitController<UIADAPTER extends InitUIAdapter> extends ControlController<UIADAPTER>{

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        getUIAdapter().showInit(true);
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        getUIAdapter().hideInit(true);
    }

    @Subscribe(sticky = true)
    public void onErrorEvent(ErrorEvent errorEvent) {
        onError(errorEvent.getPlayerException());
    }

    @Subscribe(sticky = true, priority = 1)
    public void onCompletedEvent(CompletedEvent completedEvent){
        getUIAdapter().changeVisibleOnComplete();
    }

    protected void onError(PlayerException e){
        getUIAdapter().changeVisibleOnError();
    }

}
