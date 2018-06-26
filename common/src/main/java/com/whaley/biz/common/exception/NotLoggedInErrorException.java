package com.whaley.biz.common.exception;

/**
 * Author: qxw
 * Date:2017/8/14
 * Introduction:
 */

public class NotLoggedInErrorException extends Exception {

    public NotLoggedInErrorException(String msg) {
        super(msg);
    }
}
