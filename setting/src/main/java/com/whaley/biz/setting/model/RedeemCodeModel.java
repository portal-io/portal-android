package com.whaley.biz.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;

public class RedeemCodeModel extends BaseModel implements Parcelable {
    /**
     * "redeemCode": "ZG26WZ21", //兑换码
     * "status": 1,//兑换码状态。（1：未兑换；0：已兑换）
     * "createTime": 1496653313000 //兑换码生成时间
     */

    private String redeemCode;
    private int status;
    private String redeemCodeShowType;

    public String getRedeemCodeShowType() {
        return redeemCodeShowType;
    }

    public void setRedeemCodeShowType(String redeemCodeShowType) {
        this.redeemCodeShowType = redeemCodeShowType;
    }

    public String getRedeemCode() {
        return redeemCode;
    }

    public void setRedeemCode(String redeemCode) {
        this.redeemCode = redeemCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.redeemCode);
        dest.writeInt(this.status);
        dest.writeString(this.redeemCodeShowType);
    }

    public RedeemCodeModel() {
    }

    protected RedeemCodeModel(Parcel in) {
        super(in);
        this.redeemCode = in.readString();
        this.status = in.readInt();
        this.redeemCodeShowType = in.readString();
    }

    public static final Creator<RedeemCodeModel> CREATOR = new Creator<RedeemCodeModel>() {
        @Override
        public RedeemCodeModel createFromParcel(Parcel source) {
            return new RedeemCodeModel(source);
        }

        @Override
        public RedeemCodeModel[] newArray(int size) {
            return new RedeemCodeModel[size];
        }
    };
}
