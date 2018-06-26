package com.whaley.biz.user.event;

import com.whaley.biz.common.event.BaseEvent;

/**
 * Author: qxw
 * Date:2017/8/28
 * Introduction:
 */

public class UpdateNameEvent extends BaseEvent{

    public UpdateNameEvent(String eventType) {
        super(eventType);
    }
}
