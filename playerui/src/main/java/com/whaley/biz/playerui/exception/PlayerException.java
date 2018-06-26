package com.whaley.biz.playerui.exception;

/**
 * Created by YangZhi on 2017/4/18 23:03.
 */

public class PlayerException extends Exception{

    private int errorCode;

    public PlayerException(int errorCode, String message){
        super(message);
        this.errorCode=errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
