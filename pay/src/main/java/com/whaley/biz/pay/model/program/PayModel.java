package com.whaley.biz.pay.model.program;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/4/13.
 */

public class PayModel implements Parcelable {
    /**
     * accountId : luoliang
     * orderId : normal_sales_0868125d3bd14c908aacf0d25228588f
     * merchandiseCode : ef4f252610df4a3bb458851640ba728e
     * merchandiseType : recorded
     * amount : 1
     * currency : RMB
     * result : 1
     * merchandiseName : aaa
     * merchandiseImage :
     * merchandiseStatus : 1
     */

    private String accountId;
    private String orderId;
    private String merchandiseCode;
    private String merchandiseType;
    private String amount;
    private String currency;
    private String result;
    private String merchandiseName;
    private String merchandiseImage;
    private int merchandiseStatus;
    /**
     * updateTime : 1491815017000
     * merchandiseContentCount : 2
     * liveStatus : 1
     */

    private long updateTime;
    private int merchandiseContentCount;
    private int liveStatus;
    private String merchandisePrice;

//    public LivePlayerInitModel getLivePlayerInitModel() {
//        LivePlayerInitModel livePlayerInitModel = new LivePlayerInitModel();
//        livePlayerInitModel.setSid(getMerchandiseCode())
//                .setPoster(getMerchandiseImage())
//                .setIsChargeable(1)
//                .setPrice(getMerchandisePrice())
//                .setTitle(getMerchandiseName())
//                .setVrPanPlayerBean(getPlayModel().convertToVRPanPlayerBean());
//        return livePlayerInitModel;
//    }

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

    public int getMerchandiseStatus() {
        return merchandiseStatus;
    }

    public void setMerchandiseStatus(int merchandiseStatus) {
        this.merchandiseStatus = merchandiseStatus;
    }

    public int getMerchandiseContentCount() {
        return merchandiseContentCount;
    }

    public void setMerchandiseContentCount(int merchandiseContentCount) {
        this.merchandiseContentCount = merchandiseContentCount;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
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
        dest.writeInt(this.merchandiseStatus);
        dest.writeLong(this.updateTime);
        dest.writeInt(this.merchandiseContentCount);
        dest.writeInt(this.liveStatus);
        dest.writeString(this.merchandisePrice);
    }

    public PayModel() {
    }

    protected PayModel(Parcel in) {
        this.accountId = in.readString();
        this.orderId = in.readString();
        this.merchandiseCode = in.readString();
        this.merchandiseType = in.readString();
        this.amount = in.readString();
        this.currency = in.readString();
        this.result = in.readString();
        this.merchandiseName = in.readString();
        this.merchandiseImage = in.readString();
        this.merchandiseStatus = in.readInt();
        this.updateTime = in.readLong();
        this.merchandiseContentCount = in.readInt();
        this.liveStatus = in.readInt();
        this.merchandisePrice = in.readString();
    }

    public static final Creator<PayModel> CREATOR = new Creator<PayModel>() {
        public PayModel createFromParcel(Parcel source) {
            return new PayModel(source);
        }

        public PayModel[] newArray(int size) {
            return new PayModel[size];
        }
    };

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getMerchandisePrice() {
        return merchandisePrice;
    }

    public void setMerchandisePrice(String merchandisePrice) {
        this.merchandisePrice = merchandisePrice;
    }

//    @Override
//    public PlayModel getPlayModel() {
//        PlayModel playModel = new PlayModel();
//        playModel.setType(Constants.TYPE_PLAY_LIVE);
//        playModel.setPic(getMerchandiseImage());
//        playModel.setCode(getMerchandiseCode());
//        playModel.setName(getMerchandiseName());
//        playModel.setIsChargeable(1);
//        playModel.setPrice(getMerchandisePrice());
//        return playModel;
//    }

}
