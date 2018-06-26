package com.whaley.biz.playerui.component.simpleplayer.lock;

import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.LockChangeEvent;

/**
 * Created by YangZhi on 2017/8/2 16:02.
 */

public class LockController<T extends LockUIAdapter> extends ControlController<T> {

    @Override
    public void onLockChangedEvent(LockChangeEvent lockChangeEvent){
        super.onLockChangedEvent(lockChangeEvent);
        getUIAdapter().changeLockBtn(lockChangeEvent.isLocked());
    }

    public void onLockClick(){
        LockChangeEvent lockChangeEvent = new LockChangeEvent();
        lockChangeEvent.setLocked(!isLocked());
        emitStickyEvent(lockChangeEvent);
    }

    @Override
    protected boolean isShowOnLock() {
        return true;
    }

    @Override
    protected void onSwitchToLandscape() {
        super.onSwitchToLandscape();
        getUIAdapter().show();
        if(!isHide()){
            getUIAdapter().show(true);
        }
    }

    @Override
    protected void onSwitchToProtrait() {
        super.onSwitchToProtrait();
        getUIAdapter().hide();
        if(isLocked()){
            LockChangeEvent lockChangeEvent = new LockChangeEvent();
            lockChangeEvent.setLocked(false);
            emitStickyEvent(lockChangeEvent);
        }
    }

    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    protected void show() {
        getUIAdapter().show();
    }

    protected void hide() {
        getUIAdapter().hide();
    }

}
