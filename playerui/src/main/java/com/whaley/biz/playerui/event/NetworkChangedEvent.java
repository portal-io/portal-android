package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/7 20:44.
 */

public class NetworkChangedEvent extends PriorityEvent{

    private int networkState;

    public void setNetworkState(int networkState) {
        this.networkState = networkState;
    }

    public int getNetworkState() {
        return networkState;
    }
}
