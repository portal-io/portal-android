package com.whaley.biz.playerui.event;

/**
 * Created by yangzhi on 2017/8/5.
 */

public class ScreenChangedEvent extends PriorityEvent{

    private int requestOrientation;

    public void setRequestOrientation(int requestOrientation) {
        this.requestOrientation = requestOrientation;
    }

    public int getRequestOrientation() {
        return requestOrientation;
    }
}
