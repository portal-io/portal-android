package com.whaley.biz.pay.event;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.pay.PayConstants;
import com.whaley.biz.pay.model.PayEventModel;

/**
 * Created by dell on 2017/8/22.
 */

public class PayEvent extends BaseEvent {

    public PayEvent(PayEventModel payEventModel) {
        super(PayConstants.EVENT_PAY, payEventModel);
    }

}
