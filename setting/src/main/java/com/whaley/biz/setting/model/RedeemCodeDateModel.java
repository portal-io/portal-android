package com.whaley.biz.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RedeemCodeDateModel implements Parcelable {
    /**
     * "uid": "11111111", //请求的用户id
     * "redeemCodeList": [
     * {
     * "redeemCode": "ZG26WZ21", //兑换码
     * "status": 1,//兑换码状态。（1：未兑换；0：已兑换）
     * "createTime": 1496653313000 //兑换码生成时间
     * }
     * ],
     * "phoneNum": "123456789"//请求的手机号
     */

    private String uid;
    private String phoneNum;
    private List<RedeemCodeModel> redeemCodeList;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public List<RedeemCodeModel> getRedeemCodeList() {
        return redeemCodeList;
    }

    public void setRedeemCodeList(List<RedeemCodeModel> redeemCodeList) {
        this.redeemCodeList = redeemCodeList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.phoneNum);
        dest.writeTypedList(redeemCodeList);
    }

    public RedeemCodeDateModel() {
    }

    protected RedeemCodeDateModel(Parcel in) {
        this.uid = in.readString();
        this.phoneNum = in.readString();
        this.redeemCodeList = in.createTypedArrayList(RedeemCodeModel.CREATOR);
    }

    public static final Creator<RedeemCodeDateModel> CREATOR = new Creator<RedeemCodeDateModel>() {
        public RedeemCodeDateModel createFromParcel(Parcel source) {
            return new RedeemCodeDateModel(source);
        }

        public RedeemCodeDateModel[] newArray(int size) {
            return new RedeemCodeDateModel[size];
        }
    };
}
