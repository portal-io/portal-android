package com.whaleyvr.biz.danmu.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaleyvr.biz.danmu.DanMu;
import com.whaleyvr.biz.danmu.DanmuSocketManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by dell on 2017/8/11.
 */

public class DanmuRepository extends MemoryRepository {

    private final LinkedBlockingQueue<DanMu> danMuQueue = new LinkedBlockingQueue<>();

    private final ConcurrentHashMap<String, DanMu> sendedDanMuMap = new ConcurrentHashMap<>();

    private String code;

    private String title;

    private boolean isSend = false;

//    private DanmuSocketManager danmuSocketManager;

    private String uid;

    private int roomid;

    private int loginStatus = NOT_LOGIN;

    public final static int NOT_LOGIN = 0;
    public final static int LOGINING = 1;
    public final static int LOGINED = 2;

    public LinkedBlockingQueue<DanMu> getDanMuQueue() {
        return danMuQueue;
    }

    public ConcurrentHashMap<String, DanMu> getSendedDanMuMap() {
        return sendedDanMuMap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

//    public DanmuSocketManager getDanmuSocketManager() {
//        return danmuSocketManager;
//    }
//
//    public void setDanmuSocketManager(DanmuSocketManager danmuSocketManager) {
//        this.danmuSocketManager = danmuSocketManager;
//    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
