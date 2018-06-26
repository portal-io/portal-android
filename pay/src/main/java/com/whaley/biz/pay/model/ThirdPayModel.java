package com.whaley.biz.pay.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by dell on 2017/8/22.
 */

public class ThirdPayModel implements Parcelable {

    private List<CouponModel> packsCoupons;
    private String content;
    private int displayMode;
    private boolean isUnity;
    private String type;
    private String code;
    private int whaleyMoney;


    public List<CouponModel> getPacksCoupons() {
        return packsCoupons;
    }

    public void setPacksCoupons(List<CouponModel> packsCoupons) {
        this.packsCoupons = packsCoupons;
    }

    public int getWhaleyMoney() {
        return whaleyMoney;
    }

    public void setWhaleyMoney(int whaleyMoney) {
        this.whaleyMoney = whaleyMoney;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public boolean isUnity() {
        return isUnity;
    }

    public void setUnity(boolean unity) {
        isUnity = unity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.packsCoupons);
        dest.writeString(this.content);
        dest.writeInt(this.displayMode);
        dest.writeByte(this.isUnity ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
        dest.writeString(this.code);
        dest.writeInt(this.whaleyMoney);
    }

    public ThirdPayModel() {
    }

    protected ThirdPayModel(Parcel in) {
        this.packsCoupons = in.createTypedArrayList(CouponModel.CREATOR);
        this.content = in.readString();
        this.displayMode = in.readInt();
        this.isUnity = in.readByte() != 0;
        this.type = in.readString();
        this.code = in.readString();
        this.whaleyMoney = in.readInt();
    }

    public static final Parcelable.Creator<ThirdPayModel> CREATOR = new Parcelable.Creator<ThirdPayModel>() {
        @Override
        public ThirdPayModel createFromParcel(Parcel source) {
            return new ThirdPayModel(source);
        }

        @Override
        public ThirdPayModel[] newArray(int size) {
            return new ThirdPayModel[size];
        }
    };
}
