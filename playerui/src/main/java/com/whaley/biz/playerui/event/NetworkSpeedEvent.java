package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/3 21:32.
 */

public class NetworkSpeedEvent extends PriorityEvent{

    String speedStr;

    public void setSpeedStr(String speedStr) {
        this.speedStr = speedStr;
    }

    public String getSpeedStr() {
        return speedStr;
    }
}
