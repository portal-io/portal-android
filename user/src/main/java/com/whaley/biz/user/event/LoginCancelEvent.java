package com.whaley.biz.user.event;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.user.UserConstants;

/**
 * Created by dell on 2017/10/26.
 */

public class LoginCancelEvent extends BaseEvent {

    public LoginCancelEvent() {
        super(UserConstants.EVENT_LOGIN_CANCEL);
    }

}
