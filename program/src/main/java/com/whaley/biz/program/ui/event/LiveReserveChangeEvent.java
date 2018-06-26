package com.whaley.biz.program.ui.event;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.program.constants.ProgramConstants;

/**
 * Created by dell on 2017/11/7.
 */

public class LiveReserveChangeEvent extends BaseEvent {

    public LiveReserveChangeEvent() {
        super(ProgramConstants.EVENT_RESERVE_CHANGE);
    }

}
