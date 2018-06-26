package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.playerui.event.Event;
import com.whaley.biz.playerui.event.PriorityEvent;

/**
 * Created by YangZhi on 2017/8/12 20:07.
 */

public class LotteryFailEvent extends PriorityEvent{

    private String time;

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
