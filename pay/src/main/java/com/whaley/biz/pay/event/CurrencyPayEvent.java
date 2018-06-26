package com.whaley.biz.pay.event;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.pay.PayConstants;

/**
 * Created by dell on 2017/10/16.
 */

public class CurrencyPayEvent extends BaseEvent {

    public CurrencyPayEvent() {
        super(PayConstants.EVENT_CURRENCY_PAY);
    }

}
