package com.whaley.biz.program.model.pay;

/**
 * Author: qxw
 * Date:2017/8/31
 * Introduction:
 */

public class PayModel {
    private boolean isPay;
    private String code;

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}