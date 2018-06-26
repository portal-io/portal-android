package com.whaley.biz.pay.model.program;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: qxw
 * Date:2017/7/20
 * Introduction:
 */

public class PayResultModel implements Parcelable {

    private String uid;
    private boolean result;
    private String goodsNo;
    private String goodsType;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeByte(this.result ? (byte) 1 : (byte) 0);
        dest.writeString(this.goodsNo);
        dest.writeString(this.goodsType);
    }

    public PayResultModel() {
    }

    protected PayResultModel(Parcel in) {
        this.uid = in.readString();
        this.result = in.readByte() != 0;
        this.goodsNo = in.readString();
        this.goodsType = in.readString();
    }

    public static final Creator<PayResultModel> CREATOR = new Creator<PayResultModel>() {
        @Override
        public PayResultModel createFromParcel(Parcel source) {
            return new PayResultModel(source);
        }

        @Override
        public PayResultModel[] newArray(int size) {
            return new PayResultModel[size];
        }
    };
}
