package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/9/6 20:09.
 */

public class KeyboardVisibleChangeEvent {
    private boolean visible;
    private int visibleHeight;

    public KeyboardVisibleChangeEvent(boolean visible, int visibleHeight) {
        this.visible = visible;
        this.visibleHeight = visibleHeight;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisibleHeight(int visibleHeight) {
        this.visibleHeight = visibleHeight;
    }

    public int getVisibleHeight() {
        return visibleHeight;
    }
}
