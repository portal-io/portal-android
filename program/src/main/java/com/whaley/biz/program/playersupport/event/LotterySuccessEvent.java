package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.playerui.event.Event;
import com.whaley.biz.playerui.event.PriorityEvent;

/**
 * Created by YangZhi on 2017/8/12 20:07.
 */

public class LotterySuccessEvent extends PriorityEvent{

    private String time;
    private String name;
    private String picUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
