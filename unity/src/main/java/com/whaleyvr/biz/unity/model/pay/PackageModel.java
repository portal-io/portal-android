package com.whaleyvr.biz.unity.model.pay;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;

import java.io.Serializable;

/**
 * Created by dell on 2017/8/10.
 */

public class PackageModel extends BaseModel implements Serializable, Parcelable {

    private String code;
    private String displayName;
    private int type;
    private int payType;
    private String pic;
    private String description;
    private int seatus;
    private int totalCount;
    private String price;
    private int isChargeable;
    private String currency;
    private CouponModel couponDto;
    private String discountPrice;

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

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
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

    public int getSeatus() {
        return seatus;
    }

    public void setSeatus(int seatus) {
        this.seatus = seatus;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public CouponModel getCouponDto() {
        return couponDto;
    }

    public void setCouponDto(CouponModel couponDto) {
        this.couponDto = couponDto;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
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
        dest.writeInt(this.payType);
        dest.writeString(this.pic);
        dest.writeString(this.description);
        dest.writeInt(this.seatus);
        dest.writeInt(this.totalCount);
        dest.writeString(this.price);
        dest.writeInt(this.isChargeable);
        dest.writeString(this.currency);
        dest.writeParcelable(this.couponDto, 0);
        dest.writeString(this.discountPrice);
    }

    public PackageModel() {
    }

    protected PackageModel(Parcel in) {
        super(in);
        this.code = in.readString();
        this.displayName = in.readString();
        this.type = in.readInt();
        this.payType = in.readInt();
        this.pic = in.readString();
        this.description = in.readString();
        this.seatus = in.readInt();
        this.totalCount = in.readInt();
        this.price = in.readString();
        this.isChargeable = in.readInt();
        this.currency = in.readString();
        this.couponDto = in.readParcelable(CouponModel.class.getClassLoader());
        this.discountPrice = in.readString();
    }

    public static final Creator<PackageModel> CREATOR = new Creator<PackageModel>() {
        public PackageModel createFromParcel(Parcel source) {
            return new PackageModel(source);
        }

        public PackageModel[] newArray(int size) {
            return new PackageModel[size];
        }
    };


}
