package com.whaley.biz.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;
import com.whaley.biz.setting.constant.ProgramConstants;
import com.whaley.biz.setting.model.player.DataBuilder;
import com.whaley.biz.setting.model.player.PlayData;

import java.io.Serializable;

public class RedemptionCodeModel extends BaseModel implements Serializable, Parcelable, ProgramConstants {

    /**
     * "uid": "91846816", //用户id
     * "couponCode": "HLXTORLZ",//券编码
     * "couponType": 1,//券类型（1：观看券）
     * "displayName": "yangtest5 观看劵",//券名称
     * "couponSource": "order",//券来源（order:订单；redeemCode:兑换码）
     * "couponSourceCode": "orderno_20170523145123055cfd1bde48da3447",//券来源相关编码
     * "createTime": 1495522726000,//创建时间
     * "couponStatus": 1,//券状态（1：有效；0：失效）
     * "liveStatus":1//直播状态（0：直播前；1：直播中；2：直播后）
     * "price": "11",//价格
     * "currency": "RMB",//货币类型（RMB：人民币）
     */

    private String uid;
    private String couponCode;
    private String displayName;
    private String couponSource;
    private String couponSourceCode;
    private String relatedCode;
    private String relatedType;
    private int couponType;
    private int couponStatus;
    private int liveStatus;
    private String price;
    private String currency;
    private int liveDisplayMode;
    private boolean isUnity;

    public int getLiveDisplayMode() {
        return liveDisplayMode;
    }

    public void setLiveDisplayMode(int liveDisplayMode) {
        this.liveDisplayMode = liveDisplayMode;
    }

    public String getRelatedCode() {
        return relatedCode;
    }

    public void setRelatedCode(String relatedCode) {
        this.relatedCode = relatedCode;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCouponSource() {
        return couponSource;
    }

    public void setCouponSource(String couponSource) {
        this.couponSource = couponSource;
    }

    public String getCouponSourceCode() {
        return couponSourceCode;
    }

    public void setCouponSourceCode(String couponSourceCode) {
        this.couponSourceCode = couponSourceCode;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public int getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(int couponStatus) {
        this.couponStatus = couponStatus;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public RedemptionCodeModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.uid);
        dest.writeString(this.couponCode);
        dest.writeString(this.displayName);
        dest.writeString(this.couponSource);
        dest.writeString(this.couponSourceCode);
        dest.writeString(this.relatedCode);
        dest.writeString(this.relatedType);
        dest.writeInt(this.couponType);
        dest.writeInt(this.couponStatus);
        dest.writeInt(this.liveStatus);
        dest.writeString(this.price);
        dest.writeString(this.currency);
        dest.writeByte((byte) (isUnity ? 1 : 0));
    }

    protected RedemptionCodeModel(Parcel in) {
        super(in);
        this.uid = in.readString();
        this.couponCode = in.readString();
        this.displayName = in.readString();
        this.couponSource = in.readString();
        this.couponSourceCode = in.readString();
        this.relatedCode = in.readString();
        this.relatedType = in.readString();
        this.couponType = in.readInt();
        this.couponStatus = in.readInt();
        this.liveStatus = in.readInt();
        this.price = in.readString();
        this.currency = in.readString();
        this.isUnity = in.readByte() != 0;
    }

    public static final Creator<RedemptionCodeModel> CREATOR = new Creator<RedemptionCodeModel>() {
        @Override
        public RedemptionCodeModel createFromParcel(Parcel source) {
            return new RedemptionCodeModel(source);
        }

        @Override
        public RedemptionCodeModel[] newArray(int size) {
            return new RedemptionCodeModel[size];
        }
    };

    public PlayData getPlayData() {
        int type = TYPE_PLAY_PANO;
        if (VIDEO_TYPE_VR.equals(relatedType)) {
            type = TYPE_PLAY_PANO;
        } else if (TYPE_LIVE.equals(relatedType)) {
            type = TYPE_PLAY_LIVE;;
        }
        return DataBuilder.createBuilder("", type)
                .setId(getRelatedCode())
                .setTitle("")
                .setMonocular(true)
                .build();
    }

    public boolean isUnity() {
        return isUnity;
    }

    public void setUnity(boolean unity) {
        isUnity = unity;
    }
}
