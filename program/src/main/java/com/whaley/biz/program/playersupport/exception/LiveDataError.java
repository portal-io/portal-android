package com.whaley.biz.program.playersupport.exception;

import com.whaley.biz.playerui.exception.ErrorConstants;
import com.whaley.biz.playerui.exception.PlayerException;

/**
 * Created by YangZhi on 2017/8/17 17:33.
 */

public class LiveDataError extends PlayerException{

    public LiveDataError(String message) {
        super(ErrorConstants.STR_ERROR_ORIGIN_URL, message);
    }
}
