package com.whaley.biz.common.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Author: qxw
 * Date: 2016/10/11
 */

public class BaseModel implements Parcelable, Serializable {
    private int id;

    private String createTime;

    private String updateTime;

    private String publishTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public BaseModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeString(this.publishTime);
    }

    protected BaseModel(Parcel in) {
        this.id = in.readInt();
        this.createTime = in.readString();
        this.updateTime = in.readString();
        this.publishTime = in.readString();
    }

}
