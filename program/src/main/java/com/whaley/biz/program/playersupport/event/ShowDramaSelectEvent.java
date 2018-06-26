package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.playerui.event.PriorityEvent;

/**
 * Created by dell on 2017/11/15.
 */

public class ShowDramaSelectEvent extends PriorityEvent {

    boolean isVisible;

    public ShowDramaSelectEvent(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
