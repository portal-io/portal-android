package com.whaley.biz.program.playersupport.exception;

import com.whaley.biz.playerui.exception.ErrorConstants;
import com.whaley.biz.playerui.exception.PlayerException;

/**
 * Created by YangZhi on 2017/8/17 17:33.
 */

public class ProgramDataError extends PlayerException{

    public ProgramDataError(String message) {
        super(ErrorConstants.STR_ERROR_ORIGIN_URL, message);
    }
}
