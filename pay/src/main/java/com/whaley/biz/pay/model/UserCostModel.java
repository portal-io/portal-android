package com.whaley.biz.pay.model;

/**
 * Created by dell on 2017/10/16.
 */

public class UserCostModel {

    private String buyType;
    private String buyParams;
    private String bizParams;

    public String getBuyType() {
        return buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public String getBuyParams() {
        return buyParams;
    }

    public void setBuyParams(String buyParams) {
        this.buyParams = buyParams;
    }

    public String getBizParams() {
        return bizParams;
    }

    public void setBizParams(String bizParams) {
        this.bizParams = bizParams;
    }

}
