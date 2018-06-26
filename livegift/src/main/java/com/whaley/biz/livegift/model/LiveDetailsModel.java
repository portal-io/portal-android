package com.whaley.biz.livegift.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: qxw
 * Date:2017/10/26
 * Introduction:
 */

public class LiveDetailsModel implements Parcelable {
    private String code;
    private int isGift;

    private int isTip;

    private String giftTemplate;
    private String memberTemplate;

    public String getMemberTemplate() {
        return memberTemplate;
    }

    public void setMemberTemplate(String memberTemplate) {
        this.memberTemplate = memberTemplate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public int getIsTip() {
        return isTip;
    }

    public void setIsTip(int isTip) {
        this.isTip = isTip;
    }

    public String getGiftTemplate() {
        return giftTemplate;
    }

    public void setGiftTemplate(String giftTemplate) {
        this.giftTemplate = giftTemplate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeInt(this.isGift);
        dest.writeInt(this.isTip);
        dest.writeString(this.giftTemplate);
    }

    public LiveDetailsModel() {
    }

    protected LiveDetailsModel(Parcel in) {
        this.code = in.readString();
        this.isGift = in.readInt();
        this.isTip = in.readInt();
        this.giftTemplate = in.readString();
    }

    public static final Parcelable.Creator<LiveDetailsModel> CREATOR = new Parcelable.Creator<LiveDetailsModel>() {
        @Override
        public LiveDetailsModel createFromParcel(Parcel source) {
            return new LiveDetailsModel(source);
        }

        @Override
        public LiveDetailsModel[] newArray(int size) {
            return new LiveDetailsModel[size];
        }
    };
}
