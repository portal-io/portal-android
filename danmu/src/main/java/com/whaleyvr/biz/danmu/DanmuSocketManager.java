package com.whaleyvr.biz.danmu;

import com.whaley.core.utils.GsonUtil;
import com.whaleyvr.core.network.socketio.EventListener;
import com.whaleyvr.core.network.socketio.IScocketio;
import com.whaleyvr.core.network.socketio.SocketioManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by dell on 2017/5/8.
 */

public class DanmuSocketManager extends SocketioManager implements IScocketio {

    private DanmuLoginData danmuLoginData;

    public DanmuSocketManager(String ip, String port, DanmuLoginData danmuLoginData){
        super(String.format("http://%s:%s", ip, port));
        this.danmuLoginData = danmuLoginData;
        setIScocketio(this);
        try {
            initSocket();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        registerEvent();
    }

    private void registerEvent(){
        registerEvent("message", new EventListener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject)args[0];
            }
        });
    }

    public void login(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(GsonUtil.getGson().toJson(danmuLoginData));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonObject!=null) {
            emitMsg("login", jsonObject);
        }
    }

    @Override
    public void onConnected(Object... args) {
        login();
    }

    @Override
    public void onDisconnected(Object... args) {
        //
    }

    public DanmuLoginData getDanmuLoginData() {
        return danmuLoginData;
    }

    public void setDanmuLoginData(DanmuLoginData danmuLoginData) {
        this.danmuLoginData = danmuLoginData;
    }


}
