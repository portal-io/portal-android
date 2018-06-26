package com.whaley.biz.launcher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;

import java.io.Serializable;

/**
 * Author: qxw
 * Date: 2017/3/20
 */

public class CpInfoModel extends BaseModel implements Serializable, Parcelable {
    private String code;
    private String name;
    private int available;
    private String headPic;
    private String backgroundPic;
    private String info;
    private int fansCount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getBackgroundPic() {
        return backgroundPic;
    }

    public void setBackgroundPic(String backgroundPic) {
        this.backgroundPic = backgroundPic;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }


    public CpInfoModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeInt(this.available);
        dest.writeString(this.headPic);
        dest.writeString(this.backgroundPic);
        dest.writeString(this.info);
        dest.writeInt(this.fansCount);
    }

    protected CpInfoModel(Parcel in) {
        super(in);
        this.code = in.readString();
        this.name = in.readString();
        this.available = in.readInt();
        this.headPic = in.readString();
        this.backgroundPic = in.readString();
        this.info = in.readString();
        this.fansCount = in.readInt();
    }

    public static final Creator<CpInfoModel> CREATOR = new Creator<CpInfoModel>() {
        public CpInfoModel createFromParcel(Parcel source) {
            return new CpInfoModel(source);
        }

        public CpInfoModel[] newArray(int size) {
            return new CpInfoModel[size];
        }
    };
}
