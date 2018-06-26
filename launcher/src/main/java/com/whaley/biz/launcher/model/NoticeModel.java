package com.whaley.biz.launcher.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: qxw
 * Date:2017/9/4
 * Introduction:
 */

public class NoticeModel implements Parcelable {
    private String msg_param;
    private int msg_type;//1=主页；2=专题列表；3=H5；4=直播；5=详情页；

    public String getMsg_param() {
        return msg_param;
    }

    public void setMsg_param(String msg_param) {
        this.msg_param = msg_param;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.msg_param);
        dest.writeInt(this.msg_type);
    }

    public NoticeModel() {
    }

    protected NoticeModel(Parcel in) {
        this.msg_param = in.readString();
        this.msg_type = in.readInt();
    }

    public static final Parcelable.Creator<NoticeModel> CREATOR = new Parcelable.Creator<NoticeModel>() {
        @Override
        public NoticeModel createFromParcel(Parcel source) {
            return new NoticeModel(source);
        }

        @Override
        public NoticeModel[] newArray(int size) {
            return new NoticeModel[size];
        }
    };
}
