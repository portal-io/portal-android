package com.whaley.biz.common.response;

/**
 * Created by dell on 2018/8/8.
 */

public class PortalResponse extends Response<PortalResponse> {

    private String code;
    private String memo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public boolean checkStatus() {
        return "00000000".equals(code);
    }

    @Override
    public int getStatus() {
        return Integer.valueOf(code);
    }

    @Override
    public PortalResponse getData() {
        return this;
    }

    @Override
    public String getMsg() {
        return memo;
    }

}
