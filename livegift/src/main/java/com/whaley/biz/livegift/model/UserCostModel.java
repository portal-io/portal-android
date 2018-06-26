package com.whaley.biz.livegift.model;

/**
 * Author: qxw
 * Date:2017/10/18
 * Introduction:
 */

public class UserCostModel {

    private String buyType="REDUCE_GIFT";
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
