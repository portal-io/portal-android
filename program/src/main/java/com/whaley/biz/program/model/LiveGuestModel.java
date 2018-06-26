package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by dell on 2017/8/14.
 */

public class LiveGuestModel implements Parcelable,Serializable {

    private String guestName;

    private String guestPic;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestPic() {
        return guestPic;
    }

    public void setGuestPic(String guestPic) {
        this.guestPic = guestPic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.guestName);
        dest.writeString(this.guestPic);
    }

    public LiveGuestModel() {
    }

    protected LiveGuestModel(Parcel in) {
        this.guestName = in.readString();
        this.guestPic = in.readString();
    }

    public static final Parcelable.Creator<LiveGuestModel> CREATOR = new Parcelable.Creator<LiveGuestModel>() {
        @Override
        public LiveGuestModel createFromParcel(Parcel source) {
            return new LiveGuestModel(source);
        }

        @Override
        public LiveGuestModel[] newArray(int size) {
            return new LiveGuestModel[size];
        }
    };
}
