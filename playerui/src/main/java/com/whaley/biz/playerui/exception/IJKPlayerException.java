package com.whaley.biz.playerui.exception;

/**
 * Created by YangZhi on 2017/4/19 18:40.
 */

public class IJKPlayerException extends PlayerException{

    public IJKPlayerException(String msg){
        super(ErrorConstants.STR_ERROR_PLAYER,msg);
    }
}
