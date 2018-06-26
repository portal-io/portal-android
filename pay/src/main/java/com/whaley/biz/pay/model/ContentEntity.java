package com.whaley.biz.pay.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: qxw
 * Date:2017/7/20
 * Introduction:
 */

public class ContentEntity implements Parcelable {

    private String accountId;
    private String orderId;
    private String merchandiseCode;
    private String merchandiseType;
    private String amount;
    private String currency;
    private String result;
    private String merchandiseName;
    private String merchandiseImage;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }

    public String getMerchandiseImage() {
        return merchandiseImage;
    }

    public void setMerchandiseImage(String merchandiseImage) {
        this.merchandiseImage = merchandiseImage;
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
        dest.writeString(this.result);
        dest.writeString(this.merchandiseName);
        dest.writeString(this.merchandiseImage);
    }

    public ContentEntity() {
    }

    protected ContentEntity(Parcel in) {
        this.accountId = in.readString();
        this.orderId = in.readString();
        this.merchandiseCode = in.readString();
        this.merchandiseType = in.readString();
        this.amount = in.readString();
        this.currency = in.readString();
        this.result = in.readString();
        this.merchandiseName = in.readString();
        this.merchandiseImage = in.readString();
    }

    public static final Creator<ContentEntity> CREATOR = new Creator<ContentEntity>() {
        @Override
        public ContentEntity createFromParcel(Parcel source) {
            return new ContentEntity(source);
        }

        @Override
        public ContentEntity[] newArray(int size) {
            return new ContentEntity[size];
        }
    };
}
