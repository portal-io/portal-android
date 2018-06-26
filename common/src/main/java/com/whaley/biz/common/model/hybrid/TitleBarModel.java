package com.whaley.biz.common.model.hybrid;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by dell on 2017/8/9.
 */

public class TitleBarModel implements Serializable, Parcelable {

    public static TitleBarModel createTitleBarModel(String title){
        TitleBarModel titleBarModel=new TitleBarModel();
        titleBarModel.setTitle(title);
        return titleBarModel;
    }

    public static TitleBarModel createTitleBarModel(int type){
        TitleBarModel titleBarModel=new TitleBarModel();
        titleBarModel.setType(type);
        return titleBarModel;
    }

    private int type=-1;

    private String title;

    private boolean isBelow;

    private int scrollDistance;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isBelow() {
        return isBelow;
    }

    public void setBelow(boolean below) {
        isBelow = below;
    }

    public int getScrollDistance() {
        return scrollDistance;
    }

    public void setScrollDistance(int scrollDistance) {
        this.scrollDistance = scrollDistance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.title);
        dest.writeByte(this.isBelow ? (byte) 1 : (byte) 0);
        dest.writeInt(this.scrollDistance);
    }

    public TitleBarModel() {
    }

    protected TitleBarModel(Parcel in) {
        this.type = in.readInt();
        this.title = in.readString();
        this.isBelow = in.readByte() != 0;
        this.scrollDistance = in.readInt();
    }

    public static final Parcelable.Creator<TitleBarModel> CREATOR = new Parcelable.Creator<TitleBarModel>() {
        @Override
        public TitleBarModel createFromParcel(Parcel source) {
            return new TitleBarModel(source);
        }

        @Override
        public TitleBarModel[] newArray(int size) {
            return new TitleBarModel[size];
        }
    };
}
