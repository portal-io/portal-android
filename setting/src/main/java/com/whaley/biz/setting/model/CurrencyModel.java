package com.whaley.biz.setting.model;

/**
 * Created by dell on 2017/10/13.
 */

public class CurrencyModel {
    /**
     * code : 20170718105332427fa81cbf2bbfa438
     * whaleyCurrencyNumber : 100
     * whaleyCurrencyGiveNumber : 10
     * whaleyCurrencyAmount : 110
     * price : 100
     * preferPrice : 60
     */

    private String code;
    private int whaleyCurrencyNumber;
    private int whaleyCurrencyGiveNumber;
    private int whaleyCurrencyAmount;
    private int price;
    private int preferPrice;

    public void setCode(String code) {
        this.code = code;
    }

    public void setWhaleyCurrencyNumber(int whaleyCurrencyNumber) {
        this.whaleyCurrencyNumber = whaleyCurrencyNumber;
    }

    public void setWhaleyCurrencyGiveNumber(int whaleyCurrencyGiveNumber) {
        this.whaleyCurrencyGiveNumber = whaleyCurrencyGiveNumber;
    }

    public void setWhaleyCurrencyAmount(int whaleyCurrencyAmount) {
        this.whaleyCurrencyAmount = whaleyCurrencyAmount;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPreferPrice(int preferPrice) {
        this.preferPrice = preferPrice;
    }

    public String getCode() {
        return code;
    }

    public int getWhaleyCurrencyNumber() {
        return whaleyCurrencyNumber;
    }

    public int getWhaleyCurrencyGiveNumber() {
        return whaleyCurrencyGiveNumber;
    }

    public int getWhaleyCurrencyAmount() {
        return whaleyCurrencyAmount;
    }

    public int getPrice() {
        return price;
    }

    public int getPreferPrice() {
        return preferPrice;
    }
}
