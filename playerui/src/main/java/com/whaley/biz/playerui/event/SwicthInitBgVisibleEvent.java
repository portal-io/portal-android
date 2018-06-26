package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/18 20:14.
 */

public class SwicthInitBgVisibleEvent extends PriorityEvent{

    private boolean isVisible;

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

}
