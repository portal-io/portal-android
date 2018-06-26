package com.whaley.biz.playerui.event;

/**
 * Created by dell on 2017/8/29.
 */

public class OtherPlayerEnableEvent extends PriorityEvent {
    private boolean enable;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }
}
