package com.whaley.biz.setting.event;

import com.whaley.biz.common.event.BaseEvent;

/**
 * Created by dell on 2017/9/4.
 */

public class RedemptionSuccessEvent extends BaseEvent {

    public RedemptionSuccessEvent(String eventType, String code) {
        super(eventType, code);
    }

}
