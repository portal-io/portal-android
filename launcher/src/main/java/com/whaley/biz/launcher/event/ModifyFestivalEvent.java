package com.whaley.biz.launcher.event;

/**
 * Created by dell on 2018/1/30.
 */

public class ModifyFestivalEvent {

    public ModifyFestivalEvent() {

    }

    public ModifyFestivalEvent(boolean isShow) {
        this.isShow = isShow;
    }

    boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
