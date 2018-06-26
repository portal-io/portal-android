package com.whaley.biz.pay.currency.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.currency.model.CurrencyModel;

/**
 * Created by dell on 2017/8/21.
 */

public class CurrencyPayRepository extends MemoryRepository {

    private CurrencyModel currencyModel;
    private boolean isAlipay;
    private boolean isWechat;
    private boolean isPay;
    private String realPrice;

    public CurrencyModel getCurrencyModel() {
        return currencyModel;
    }

    public void setCurrencyModel(CurrencyModel currencyModel) {
        this.currencyModel = currencyModel;
        if(currencyModel.getPreferPrice() > 0){
            realPrice = "" + currencyModel.getPreferPrice();
        }else{
            realPrice = "" + currencyModel.getPrice();
        }
    }

    public boolean isAlipay() {
        return isAlipay;
    }

    public void setAlipay(boolean alipay) {
        isAlipay = alipay;
    }

    public boolean isWechat() {
        return isWechat;
    }

    public void setWechat(boolean wechat) {
        isWechat = wechat;
    }


    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

}
