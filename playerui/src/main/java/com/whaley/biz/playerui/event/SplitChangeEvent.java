package com.whaley.biz.playerui.event;

import com.whaley.biz.playerui.event.PriorityEvent;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitChangeEvent extends PriorityEvent {

    private boolean isSplit;

    private boolean isback;

    public SplitChangeEvent(boolean isSplit){
        this.isSplit = isSplit;
    }

    public SplitChangeEvent(boolean isSplit, boolean isback){
        this.isSplit = isSplit;
        this.isback = isback;
    }

    public boolean isSplit() {
        return isSplit;
    }

    public void setSplit(boolean split) {
        isSplit = split;
    }

    public boolean isback() {
        return isback;
    }

    public void setIsback(boolean isback) {
        this.isback = isback;
    }
}
