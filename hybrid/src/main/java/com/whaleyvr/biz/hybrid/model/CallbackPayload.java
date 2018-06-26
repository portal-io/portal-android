package com.whaleyvr.biz.hybrid.model;

/**
 * Created by YangZhi on 2016/10/25 17:42.
 */
public class CallbackPayload<T> {
    String callbackId;

    T data;

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    public String getCallbackId() {
        return callbackId;
    }


    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

}
