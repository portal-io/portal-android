package com.whaley.biz.launcher.event;

/**
 * Author: qxw
 * Date:2017/9/18
 * Introduction:
 */

public class ConnectEvent {
    private String msg;

    public ConnectEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
