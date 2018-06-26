package com.whaley.biz.pay.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;
import com.whaley.core.utils.StrUtil;

/**
 * Created by dell on 2017/8/21.
 */

public class CouponModel extends BaseModel implements Parcelable {

    public final static int NOT_PAY = 0;
    public final static int PAY_SUCCESS =1;
    public final static int PAY_FAIL = 2;
    private String code;
    private String displayName;
    private int type;
    private String pic;
    private String description;
    private int status;
    private String price;
    private int jingbiPrice;
    private String currency;
    private String discount;
    private String relatedCode;
    private String relatedType;
    private String enableTime;
    private String disableTime;
    private String discountPrice;
    public boolean isSelect;
    public boolean isTopic;
    public String viewName;
    public boolean isShowPayTips;
    public int showPayType;
    public boolean isShowWhaleyPrice;

    public int getJingbiPrice() {
        return jingbiPrice;
    }

    public void setJingbiPrice(int jingbiPrice) {
        this.jingbiPrice = jingbiPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPrice() {
        if (StrUtil.isEmpty(getDiscountPrice())) {
            return price;
        } else {
            return getDiscountPrice();
        }
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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

    public String getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(String enableTime) {
        this.enableTime = enableTime;
    }

    public String getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(String disableTime) {
        this.disableTime = disableTime;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public CouponModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.code);
        dest.writeString(this.displayName);
        dest.writeInt(this.type);
        dest.writeString(this.pic);
        dest.writeString(this.description);
        dest.writeInt(this.status);
        dest.writeString(this.price);
        dest.writeInt(this.jingbiPrice);
        dest.writeString(this.currency);
        dest.writeString(this.discount);
        dest.writeString(this.relatedCode);
        dest.writeString(this.relatedType);
        dest.writeString(this.enableTime);
        dest.writeString(this.disableTime);
        dest.writeString(this.discountPrice);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTopic ? (byte) 1 : (byte) 0);
        dest.writeString(this.viewName);
    }

    protected CouponModel(Parcel in) {
        super(in);
        this.code = in.readString();
        this.displayName = in.readString();
        this.type = in.readInt();
        this.pic = in.readString();
        this.description = in.readString();
        this.status = in.readInt();
        this.price = in.readString();
        this.jingbiPrice = in.readInt();
        this.currency = in.readString();
        this.discount = in.readString();
        this.relatedCode = in.readString();
        this.relatedType = in.readString();
        this.enableTime = in.readString();
        this.disableTime = in.readString();
        this.discountPrice = in.readString();
        this.isSelect = in.readByte() != 0;
        this.isTopic = in.readByte() != 0;
        this.viewName = in.readString();
    }

    public static final Creator<CouponModel> CREATOR = new Creator<CouponModel>() {
        @Override
        public CouponModel createFromParcel(Parcel source) {
            return new CouponModel(source);
        }

        @Override
        public CouponModel[] newArray(int size) {
            return new CouponModel[size];
        }
    };
}

