package com.whaley.biz.common.response;

/**
 * Author: qxw
 * Date: 2016/10/13
 */

public class CMSResponse<T> extends Response<T> {
    private T data;
    private int code;
    private String subCode;


    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    @Override
    public int getStatus() {
        return getCode();
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public boolean checkStatus() {
        return getCode() == 200;
    }
}
