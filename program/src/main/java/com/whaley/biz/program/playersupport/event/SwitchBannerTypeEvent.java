package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.playerui.event.Event;
import com.whaley.biz.playerui.event.PriorityEvent;

/**
 * Created by YangZhi on 2017/8/28 17:05.
 */

public class SwitchBannerTypeEvent extends PriorityEvent{
    public static final int TYPE_VR = 0;
    public static final int TYPE_LIVE = 1;
    public static final int TYPE_MOVIE = 2;


    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
