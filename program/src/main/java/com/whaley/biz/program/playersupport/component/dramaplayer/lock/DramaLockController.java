package com.whaley.biz.program.playersupport.component.dramaplayer.lock;

import com.whaley.biz.playerui.component.simpleplayer.lock.LockController;
import com.whaley.biz.playerui.event.LockChangeEvent;
import com.whaley.biz.playerui.event.RestTouchDurationEvent;
import com.whaley.biz.program.playersupport.event.ShowDramaSelectEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dell on 2017/11/15.
 */

public class DramaLockController extends LockController<DramaLockUIAdapter> {

    boolean isDramaShow;

    @Subscribe
    public void onShowDramaSelectEvent(ShowDramaSelectEvent showDramaSelectEvent) {
        if(isDramaShow == showDramaSelectEvent.isVisible())
            return;
        isDramaShow = showDramaSelectEvent.isVisible();
            if (isDramaShow) {
                getUIAdapter().hide();
                if (isLocked()) {
                    LockChangeEvent lockChangeEvent = new LockChangeEvent();
                    lockChangeEvent.setLocked(!isLocked());
                    lockChangeEvent.setIshideUI(true);
                    emitStickyEvent(lockChangeEvent);
                }
            } else {
                if (isLandScape()) {
//                    getUIAdapter().hide(false);
                    getUIAdapter().show();
                    if(!isHide()){
                        getUIAdapter().show(false);
                    }
                }
            }
    }

    @Override
    protected void show() {
        if(isDramaShow)
            return;
        getUIAdapter().show();
    }

}
