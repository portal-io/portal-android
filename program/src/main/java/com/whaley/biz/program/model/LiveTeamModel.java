package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by dell on 2017/8/17.
 */

public class LiveTeamModel implements Serializable, Parcelable {

    private String picUrl;
    private String name;

    public LiveTeamModel(String picUrl, String name) {
        this.picUrl = picUrl;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picUrl);
        dest.writeString(this.name);
    }

    public LiveTeamModel() {
    }

    protected LiveTeamModel(Parcel in) {
        this.picUrl = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<LiveTeamModel> CREATOR = new Parcelable.Creator<LiveTeamModel>() {
        @Override
        public LiveTeamModel createFromParcel(Parcel source) {
            return new LiveTeamModel(source);
        }

        @Override
        public LiveTeamModel[] newArray(int size) {
            return new LiveTeamModel[size];
        }
    };
}
