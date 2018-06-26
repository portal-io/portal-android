package com.whaley.biz.common.exception;

/**
 * Created by dell on 2017/9/27.
 */

public class NoNetworkErrorException extends Throwable {

    public NoNetworkErrorException() {
    }

    public NoNetworkErrorException(String message) {
        super(message);
    }

    public NoNetworkErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoNetworkErrorException(Throwable cause) {
        super(cause);
    }

}
