package com.whaleyvr.biz.danmu;

/**
 * Created by dell on 2017/5/8.
 */

public class DanmuLoginData {

    private String uid;
    private String roomid;
    private String auth;

    public DanmuLoginData(String uid, String roomid, String auth){
        this.uid = uid;
        this.roomid = roomid;
        this.auth = auth;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
