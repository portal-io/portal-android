package com.whaley.biz.playerui.exception;

/**
 * Created by YangZhi on 2017/4/19 17:18.
 */

public class NetworkPlayerException extends PlayerException{

    public NetworkPlayerException(){
        super(ErrorConstants.STR_ERROR_NETWORK,"当前网络不可用,请检查网络后重试");
    }

    public NetworkPlayerException(int errorCode, String message) {
        super(errorCode, message);
    }
}
