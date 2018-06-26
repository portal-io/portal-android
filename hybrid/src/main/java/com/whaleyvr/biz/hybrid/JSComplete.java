package com.whaleyvr.biz.hybrid;

import com.whaleyvr.biz.hybrid.model.CallbackPayload;

import java.util.Map;

/**
 * Created by YangZhi on 2016/10/27 11:12.
 */
public class JSComplete {
    private final H5Controller h5Controller;
    private final CallbackPayload callbackPayload;

    public JSComplete(H5Controller h5Controller,CallbackPayload callbackPayload){
        this.h5Controller=h5Controller;
        this.callbackPayload=callbackPayload;
    }


    public void sendCallback(Map<Object,Object> payload){
        h5Controller.sendEvent(callbackPayload.getCallbackId(),payload);
    }


    public String getCallbackId() {
        return callbackPayload.getCallbackId();
    }


}
