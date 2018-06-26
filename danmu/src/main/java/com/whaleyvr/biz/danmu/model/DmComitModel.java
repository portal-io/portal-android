package com.whaleyvr.biz.danmu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/5/8.
 */

public class DmComitModel implements Parcelable {
    /**
     * dmid : 95996
     * roomid : 1
     * uid : 613
     * nickname : 皮皮猪
     * message : bbbbbbbbbbbb
     * dateline : 1494238389
     */

    private int dmid;
    private String roomid;
    private int uid;
    private String nickname;
    private String message;
    private int dateline;

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
    }

    public DmComitModel() {
    }

    protected DmComitModel(Parcel in) {
        this.dmid = in.readInt();
        this.roomid = in.readString();
        this.uid = in.readInt();
        this.nickname = in.readString();
        this.message = in.readString();
        this.dateline = in.readInt();
    }

    public static final Creator<DmComitModel> CREATOR = new Creator<DmComitModel>() {
        public DmComitModel createFromParcel(Parcel source) {
            return new DmComitModel(source);
        }

        public DmComitModel[] newArray(int size) {
            return new DmComitModel[size];
        }
    };
}
