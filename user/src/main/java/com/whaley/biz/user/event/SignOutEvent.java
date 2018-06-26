package com.whaley.biz.user.event;

import com.whaley.biz.common.event.BaseEvent;

/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */

public class SignOutEvent extends BaseEvent {

    public SignOutEvent(String eventType) {
        super(eventType);
    }
}
