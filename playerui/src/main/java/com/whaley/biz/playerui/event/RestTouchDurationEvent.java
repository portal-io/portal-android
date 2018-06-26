package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/9/13 17:27.
 */

public class RestTouchDurationEvent {
    private boolean isRegistTouchDuration;

    private boolean isChangingRegistTouchDuration;

    public boolean isRegistTouchDuration() {
        return isRegistTouchDuration;
    }

    public void setRegistTouchDuration(boolean registTouchDuration) {
        isRegistTouchDuration = registTouchDuration;
    }

    public boolean isChangingRegistTouchDuration() {
        return isChangingRegistTouchDuration;
    }

    public void setChangingRegistTouchDuration(boolean changingRegistTouchDuration) {
        isChangingRegistTouchDuration = changingRegistTouchDuration;
    }
}
