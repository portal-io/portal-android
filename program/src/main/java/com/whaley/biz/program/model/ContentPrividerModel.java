package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by dell on 2017/11/14.
 */

public class ContentPrividerModel implements Parcelable, Serializable {

    private String cpCode;
    private int isFollow;
    private String name;
    private int fansCount;
    private String headPic;

    public String getCpCode() {
        return cpCode;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cpCode);
        dest.writeInt(this.isFollow);
        dest.writeString(this.name);
        dest.writeInt(this.fansCount);
        dest.writeString(this.headPic);
    }

    public ContentPrividerModel() {
    }

    protected ContentPrividerModel(Parcel in) {
        this.cpCode = in.readString();
        this.isFollow = in.readInt();
        this.name = in.readString();
        this.fansCount = in.readInt();
        this.headPic = in.readString();
    }

    public static final Creator<ContentPrividerModel> CREATOR = new Creator<ContentPrividerModel>() {
        public ContentPrividerModel createFromParcel(Parcel source) {
            return new ContentPrividerModel(source);
        }

        public ContentPrividerModel[] newArray(int size) {
            return new ContentPrividerModel[size];
        }
    };
}
