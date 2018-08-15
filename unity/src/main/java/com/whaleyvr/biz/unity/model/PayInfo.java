package com.whaleyvr.biz.unity.model;

/**
 * Created by dell on 2017/4/7.
 */

public class PayInfo {

    private String code;
    private String price;
    private String name;
    private String type; //live：直播；recorded：录播；content_packge：节目包
    private String payChannelType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayChannelType() {
        return payChannelType;
    }

    public void setPayChannelType(String payChannelType) {
        this.payChannelType = payChannelType;
    }
}
