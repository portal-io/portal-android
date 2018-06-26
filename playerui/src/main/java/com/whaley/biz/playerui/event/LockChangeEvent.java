package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/2 14:35.
 */

public class LockChangeEvent extends PriorityEvent{

    boolean isLocked;

    boolean ishideUI;

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean ishideUI() {
        return ishideUI;
    }

    public void setIshideUI(boolean ishideUI) {
        this.ishideUI = ishideUI;
    }
}
