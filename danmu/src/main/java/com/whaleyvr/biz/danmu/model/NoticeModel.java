package com.whaleyvr.biz.danmu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/5/16.
 */

public class NoticeModel implements Parcelable {

    private String dmid;
    private String roomid;
    private String nickname;
    private String message;
    private String color;
    private int duration;
    private int response_dateline;

    public String getDmid() {
        return dmid;
    }

    public void setDmid(String dmid) {
        this.dmid = dmid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getResponse_dateline() {
        return response_dateline;
    }

    public void setResponse_dateline(int response_dateline) {
        this.response_dateline = response_dateline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dmid);
        dest.writeString(this.roomid);
        dest.writeString(this.nickname);
        dest.writeString(this.message);
        dest.writeString(this.color);
        dest.writeInt(this.duration);
        dest.writeInt(this.response_dateline);
    }

    public NoticeModel() {
    }

    protected NoticeModel(Parcel in) {
        this.dmid = in.readString();
        this.roomid = in.readString();
        this.nickname = in.readString();
        this.message = in.readString();
        this.color = in.readString();
        this.duration = in.readInt();
        this.response_dateline = in.readInt();
    }

    public static final Creator<NoticeModel> CREATOR = new Creator<NoticeModel>() {
        public NoticeModel createFromParcel(Parcel source) {
            return new NoticeModel(source);
        }

        public NoticeModel[] newArray(int size) {
            return new NoticeModel[size];
        }
    };
}
