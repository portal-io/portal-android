package com.whaley.biz.longconnection.model;

import com.whaley.biz.common.response.BaseResponse;

/**
 * Created by dell on 2017/5/8.
 */

public class ComeInReponse implements BaseResponse {

    private int status;
    private String msg;
    private SocketModel msgservice;
    private String roomauth;
    private RoomModel roomdata;
    private NoticeModel noticedata;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public boolean checkStatus() {
        return status == 1 || status == 200;
    }

    @Override
    public void setStatus(int status) {
        this.status=status;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public void setCache(boolean cache) {

    }

    @Override
    public boolean isCache() {
        return false;
    }

    @Override
    public Object getData() {
        return null;
    }

    public SocketModel getMsgservice() {
        return msgservice;
    }

    public void setMsgservice(SocketModel msgservice) {
        this.msgservice = msgservice;
    }

    public String getRoomauth() {
        return roomauth;
    }

    public void setRoomauth(String roomauth) {
        this.roomauth = roomauth;
    }

    public RoomModel getRoomdata() {
        return roomdata;
    }

    public void setRoomdata(RoomModel roomdata) {
        this.roomdata = roomdata;
    }

    public NoticeModel getNoticedata() {
        return noticedata;
    }

    public void setNoticedata(NoticeModel noticedata) {
        this.noticedata = noticedata;
    }
}
