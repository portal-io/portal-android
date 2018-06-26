package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/1 13:57.
 */

public  class BufferingUpdateEvent extends PriorityEvent{

    private long bufferProgress;


    public void setBufferProgress(long bufferProgress) {
        this.bufferProgress = bufferProgress;
    }

    public long getBufferProgress() {
        return bufferProgress;
    }
}
