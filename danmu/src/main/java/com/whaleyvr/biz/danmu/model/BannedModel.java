package com.whaleyvr.biz.danmu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/5/22.
 */

public class BannedModel implements Parcelable {
    /**
     * uid : 583
     * nickname : erha
     * type : 1
     * roomid : 13
     * duration : 600
     * message : @erha 被禁言10分钟
     * response_dateline : 1494567504
     */

    private String uid;
    private String nickname;
    private String type;
    private String roomid;
    private int duration;
    private String message;
    private int response_dateline;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResponse_dateline(int response_dateline) {
        this.response_dateline = response_dateline;
    }

    public String getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getType() {
        return type;
    }

    public String getRoomid() {
        return roomid;
    }

    public int getDuration() {
        return duration;
    }

    public String getMessage() {
        return message;
    }

    public int getResponse_dateline() {
        return response_dateline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(this.type);
        dest.writeString(this.roomid);
        dest.writeInt(this.duration);
        dest.writeString(this.message);
        dest.writeInt(this.response_dateline);
    }

    public BannedModel() {
    }

    protected BannedModel(Parcel in) {
        this.uid = in.readString();
        this.nickname = in.readString();
        this.type = in.readString();
        this.roomid = in.readString();
        this.duration = in.readInt();
        this.message = in.readString();
        this.response_dateline = in.readInt();
    }

    public static final Creator<BannedModel> CREATOR = new Creator<BannedModel>() {
        public BannedModel createFromParcel(Parcel source) {
            return new BannedModel(source);
        }

        public BannedModel[] newArray(int size) {
            return new BannedModel[size];
        }
    };
}
