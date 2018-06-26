package com.whaley.biz.common.exception;

/**
 * Created by yangzhi on 16/8/5.
 */
public class ResponseErrorException extends Exception {

    public ResponseErrorException() {
    }

    public ResponseErrorException(String message) {
        super(message);
    }

    public ResponseErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseErrorException(Throwable cause) {
        super(cause);
    }

}
