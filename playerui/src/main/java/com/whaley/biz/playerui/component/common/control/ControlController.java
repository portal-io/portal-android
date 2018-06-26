package com.whaley.biz.playerui.component.common.control;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.HideEvent;
import com.whaley.biz.playerui.event.LockChangeEvent;
import com.whaley.biz.playerui.event.OtherPlayerEnableEvent;
import com.whaley.biz.playerui.event.ShowEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/2 14:33.
 */

public abstract class ControlController<UIADAPTER extends ControlUIAdapter> extends BaseController<UIADAPTER> {

    private boolean isLocked;

    private boolean isHide;

    @Override
    protected boolean isHasUI() {
        return true;
    }

    @Subscribe
    public void onShowEvent(ShowEvent showEvent) {
        isHide = false;
        if (isLocked && !isShowOnLock())
            return;
        showUI(true);
    }


    @Subscribe
    public void onHideEvent(HideEvent hideEvent) {
        isHide = true;
        hideUI(true);
    }

    @Subscribe
    public void onLockChangedEvent(LockChangeEvent lockChangeEvent) {
        isLocked = lockChangeEvent.isLocked();
        if(!lockChangeEvent.ishideUI()) {
            if (isLocked && !isShowOnLock()) {
                hideUI(true);
            } else {
                showUI(true);
            }
        }else{
            isHide = true;
        }
    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        isHide = true;
        hideUI(true);
    }

    protected void showUI(boolean anim) {
        getUIAdapter().show(anim);
    }

    protected void hideUI(boolean anim) {
        getUIAdapter().hide(anim);
    }

    protected boolean isShowOnLock() {
        return false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isHide() {
        return isHide;
    }

}
