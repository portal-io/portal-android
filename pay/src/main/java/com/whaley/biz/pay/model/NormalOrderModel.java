package com.whaley.biz.pay.model;

/**
 * Created by mafei on 2017/4/6.
 */

public class NormalOrderModel {

    /**
     * orderNo : orderno_20170425172120106ba3d0b79d08d480
     * orderToPayStr : appId=1488357918048444&mhtCharset=UTF-8&mhtCurrencyType=156&mhtOrderAmt=1&mhtOrderDetail=新码率测试-球体1920×960&mhtOrderName=新码率测试-球体1920×960&mhtOrderNo=orderno_20170425172120106ba3d0b79d08d480&mhtOrderStartTime=20170425172659&mhtOrderTimeOut=3600&mhtOrderType=01&notifyUrl=https://vrtest-api.aginomoto.com/newVR-report-service/thirdPartyPay/nowpayCallBack&payChannelType=12&mhtSignature=e2e8a1d59fa928f092725f0ba83468a8&mhtSignType=MD5
     */

    private String orderNo;
    private String orderToPayStr;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderToPayStr() {
        return orderToPayStr;
    }

    public void setOrderToPayStr(String orderToPayStr) {
        this.orderToPayStr = orderToPayStr;
    }

}
