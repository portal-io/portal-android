package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/1 13:58.
 */

public class SeekUpdateEvent extends PriorityEvent{

    private long currentProgress;


    public void setCurrentProgress(long currentProgress) {
        this.currentProgress = currentProgress;
    }

    public long getCurrentProgress() {
        return currentProgress;
    }
}
