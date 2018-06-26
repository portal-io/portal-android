package com.whaley.biz.user.event;

import com.whaley.biz.common.event.BaseEvent;

/**
 * Author: qxw
 * Date: 2017/3/23
 */

public class LoginSuccessEvent extends BaseEvent {

    public LoginSuccessEvent(String eventType) {
        super(eventType);
    }

    public LoginSuccessEvent(String eventType, Object object) {
        super(eventType, object);
    }

}
