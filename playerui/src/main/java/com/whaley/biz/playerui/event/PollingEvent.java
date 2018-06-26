package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/2 19:09.
 */

public class PollingEvent extends PriorityEvent{

    private long totalPolling;

    public void setTotalPolling(long totalPolling) {
        this.totalPolling = totalPolling;
    }

    public long getTotalPolling() {
        return totalPolling;
    }
}
