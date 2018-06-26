package com.whaley.biz.common.exception;

/**
 * Author: qxw
 * Date: 2017/7/14
 */

public class StatusErrorThrowable extends Exception {

    private int status;
    private Object data;
    private String subCode;

//    public StatusErrorThrowable(int status, String message) {
//        super(message);
//        this.status = status;
//    }

    public StatusErrorThrowable(int status, String msg, Object data, String subCode) {
        super(msg);
        this.status = status;
        this.data = data;
        this.subCode = subCode;
    }

    public StatusErrorThrowable(int status, String msg, Object data) {
        this(status, msg, data, null);
    }

    public StatusErrorThrowable(int status, String msg) {
        this(status, msg, null, null);
    }

    public Object getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getSubCode() {
        return subCode;
    }
}
