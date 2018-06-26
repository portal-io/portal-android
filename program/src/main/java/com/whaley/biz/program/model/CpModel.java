package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;

import java.io.Serializable;

/**
 * Created by LiuZhixiang on 3/15/17.
 */

public class CpModel extends BaseModel implements Serializable, Parcelable {
    private String code;
    private String name;
    private int available;
    private String headPic;
    private String backgroundPic;
    private String info;
    private int fansCount;
    private int fansCountFake;


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

    public int getFansCountFake() {
        return fansCountFake;
    }

    public void setFansCountFake(int fansCountFake) {
        this.fansCountFake = fansCountFake;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeInt(this.available);
        dest.writeString(this.headPic);
        dest.writeString(this.backgroundPic);
        dest.writeString(this.info);
        dest.writeInt(this.fansCount);
        dest.writeInt(this.fansCountFake);
    }

    public CpModel() {
    }

    protected CpModel(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.available = in.readInt();
        this.headPic = in.readString();
        this.backgroundPic = in.readString();
        this.info = in.readString();
        this.fansCount = in.readInt();
        this.fansCountFake = in.readInt();
    }

    public static final Parcelable.Creator<CpModel> CREATOR = new Parcelable.Creator<CpModel>() {
        @Override
        public CpModel createFromParcel(Parcel source) {
            return new CpModel(source);
        }

        @Override
        public CpModel[] newArray(int size) {
            return new CpModel[size];
        }
    };
}
