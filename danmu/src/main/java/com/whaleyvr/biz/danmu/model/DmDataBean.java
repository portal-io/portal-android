package com.whaleyvr.biz.danmu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/5/9.
 */

public class DmDataBean implements Parcelable {

    private CommentDataBean danmakudata;

    public CommentDataBean getDanmakudata() {
        return danmakudata;
    }

    public void setDanmakudata(CommentDataBean danmakudata) {
        this.danmakudata = danmakudata;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.danmakudata, 0);
    }

    public DmDataBean() {
    }

    protected DmDataBean(Parcel in) {
        this.danmakudata = in.readParcelable(CommentDataBean.class.getClassLoader());
    }

    public static final Creator<DmDataBean> CREATOR = new Creator<DmDataBean>() {
        public DmDataBean createFromParcel(Parcel source) {
            return new DmDataBean(source);
        }

        public DmDataBean[] newArray(int size) {
            return new DmDataBean[size];
        }
    };
}
