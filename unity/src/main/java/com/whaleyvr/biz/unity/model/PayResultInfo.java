package com.whaleyvr.biz.unity.model;

/**
 * Created by dell on 2017/4/7.
 */

public class PayResultInfo {

    private String code;
    private boolean result;
    private String errorCode;
    private String errorMsg;

    public PayResultInfo(String code, boolean isPayed, boolean isHasBeenPaid) {
        this.code = code;
        this.result = isPayed;
        this.errorCode = "";
        if (isHasBeenPaid) {
            this.errorMsg = "isPayed";
        } else {
            this.errorMsg = "";
        }
    }

    public PayResultInfo(String code, boolean result, String errorCode, String errorMsg) {
        this.code = code;
        this.result = result;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
