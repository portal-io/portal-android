package com.whaley.biz.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PayContentModel implements Parcelable {

    //    "accountId": "luoliang", //用户id
//            "orderId": "orderno_20170523145123055cfd1bde48da3447", //订单号
//            "merchandiseCode": "HLXTORLZ",  //商品编码
//            "merchandiseType": "coupon", //商品类型
//            "amount": "11",//商品金额
//            "currency": "RMB",//货币类型。人民币
//            "result": "1",//是否已支付，返回的类型都是已支付的
//            "updateTime": 1495522726000, //订单更新时间
//            "merchandiseName": "yangtest5 观看劵" //商品名称

    private String accountId;
    private String orderId;
    private String merchandiseCode;
    private String merchandiseType;
    private String amount;
    private String currency;
    private int result;
    private long updateTime;
    private String merchandiseName;
    private String platform;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchandiseCode() {
        return merchandiseCode;
    }

    public void setMerchandiseCode(String merchandiseCode) {
        this.merchandiseCode = merchandiseCode;
    }

    public String getMerchandiseType() {
        return merchandiseType;
    }

    public void setMerchandiseType(String merchandiseType) {
        this.merchandiseType = merchandiseType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountId);
        dest.writeString(this.orderId);
        dest.writeString(this.merchandiseCode);
        dest.writeString(this.merchandiseType);
        dest.writeString(this.amount);
        dest.writeString(this.currency);
        dest.writeInt(this.result);
        dest.writeLong(this.updateTime);
        dest.writeString(this.merchandiseName);
        dest.writeString(this.platform);
    }

    public PayContentModel() {
    }

    protected PayContentModel(Parcel in) {
        this.accountId = in.readString();
        this.orderId = in.readString();
        this.merchandiseCode = in.readString();
        this.merchandiseType = in.readString();
        this.amount = in.readString();
        this.currency = in.readString();
        this.result = in.readInt();
        this.updateTime = in.readLong();
        this.merchandiseName = in.readString();
        this.platform = in.readString();
    }

    public static final Creator<PayContentModel> CREATOR = new Creator<PayContentModel>() {
        public PayContentModel createFromParcel(Parcel source) {
            return new PayContentModel(source);
        }

        public PayContentModel[] newArray(int size) {
            return new PayContentModel[size];
        }
    };
}
