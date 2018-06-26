package com.whaley.biz.pay.model;

/**
 * Created by dell on 2017/10/16.
 */

public class UserCostResultModel {

    private boolean result;
    private String msg;
    private int code;
    private String subCode;

    public UserCostResultModel(boolean result) {
        this.result = result;
    }


    public UserCostResultModel(boolean result, String msg, int code, String subCode) {
        this.result = result;
        this.msg = msg;
        this.code = code;
        this.subCode = subCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
