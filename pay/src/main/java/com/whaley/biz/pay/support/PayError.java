package com.whaley.biz.pay.support;

import com.whaley.biz.playerui.exception.PlayerException;

/**
 * Created by YangZhi on 2017/8/18 19:54.
 */

public class PayError extends PlayerException{

    public PayError() {
        super(-5000, "");
    }
}
