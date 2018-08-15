package com.whaleyvr.biz.unity.model;

/**
 * Created by dell on 2017/1/6.
 */

public class LiveVRInfo {

    private String access_token;
    private String device_id; //设备id
    private String room_id; //房间id

    public LiveVRInfo(String access_token, String device_id, String room_id) {
        this.access_token = access_token;
        this.device_id = device_id;
        this.room_id = room_id;
    }


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
}
