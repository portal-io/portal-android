package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by LiuZhixiang on 3/15/17.
 */

public class CpDetailModel implements Serializable, Parcelable {
    private CpModel cp;
    private int follow;

    public CpModel getCp() {
        return cp;
    }

    public void setCp(CpModel cp) {
        this.cp = cp;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.cp, flags);
        dest.writeInt(this.follow);
    }

    public CpDetailModel() {
    }

    protected CpDetailModel(Parcel in) {
        this.cp = in.readParcelable(CpModel.class.getClassLoader());
        this.follow = in.readInt();
    }

    public static final Parcelable.Creator<CpDetailModel> CREATOR = new Parcelable.Creator<CpDetailModel>() {
        @Override
        public CpDetailModel createFromParcel(Parcel source) {
            return new CpDetailModel(source);
        }

        @Override
        public CpDetailModel[] newArray(int size) {
            return new CpDetailModel[size];
        }
    };
}
