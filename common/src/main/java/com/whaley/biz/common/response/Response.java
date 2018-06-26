package com.whaley.biz.common.response;

import java.io.Serializable;

/**
 * Created by yangzhi on 16/8/5.
 */
public class Response<T> implements BaseResponse<T>,Serializable {
    private boolean isCache;

    protected int status;

    private String msg = "";

    public int getStatus() {
        return this.status;
    }

    public boolean checkStatus() {
        return getStatus() == 1 || getStatus() == 200 || getStatus() == 1200;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
    }


    public void setCache(boolean cache) {
        isCache = cache;
    }

    public boolean isCache() {
        return isCache;
    }

    public T getData() {
        return null;
    }
}
