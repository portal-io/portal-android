package com.whaleyvr.biz.danmu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/5/9.
 */

public class CommentDataBean implements Parcelable {
    /**
     * dmid : 1025
     * roomid : 1
     * uid : 600
     * nickname : 游客600
     * message : aaa
     * dateline : 1494317566
     */

    private int dmid;
    private String roomid;
    private int uid;
    private String nickname;
    private String message;
    private int dateline;
    private String type;
    private String color;

    public void setDmid(int dmid) {
        this.dmid = dmid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateline(int dateline) {
        this.dateline = dateline;
    }

    public int getDmid() {
        return dmid;
    }

    public String getRoomid() {
        return roomid;
    }

    public int getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMessage() {
        return message;
    }

    public int getDateline() {
        return dateline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dmid);
        dest.writeString(this.roomid);
        dest.writeInt(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(this.message);
        dest.writeInt(this.dateline);
        dest.writeString(this.type);
        dest.writeString(this.color);
    }

    public CommentDataBean() {
    }

    protected CommentDataBean(Parcel in) {
        this.dmid = in.readInt();
        this.roomid = in.readString();
        this.uid = in.readInt();
        this.nickname = in.readString();
        this.message = in.readString();
        this.dateline = in.readInt();
        this.type = in.readString();
        this.color = in.readString();
    }

    public static final Creator<CommentDataBean> CREATOR = new Creator<CommentDataBean>() {
        public CommentDataBean createFromParcel(Parcel source) {
            return new CommentDataBean(source);
        }

        public CommentDataBean[] newArray(int size) {
            return new CommentDataBean[size];
        }
    };

}
