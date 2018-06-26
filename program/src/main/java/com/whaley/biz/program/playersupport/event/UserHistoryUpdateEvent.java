package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.common.event.BaseEvent;

/**
 * Author: qxw
 * Date:2017/9/18
 * Introduction:
 */

public class UserHistoryUpdateEvent extends BaseEvent {

    private String code;
    private String playTime;
    private String totalPlayTime;


    public UserHistoryUpdateEvent(String code, String playTime, String totalPlayTime) {
        super("history_update");
        this.code = code;
        this.playTime = playTime;
        this.totalPlayTime = totalPlayTime;
    }
}
