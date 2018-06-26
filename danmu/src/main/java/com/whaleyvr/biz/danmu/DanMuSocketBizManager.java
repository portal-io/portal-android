package com.whaleyvr.biz.danmu;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.longconnection.BaseSocketBizManager;
import com.whaleyvr.core.network.socketio.EventListener;

import org.json.JSONObject;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.whaleyvr.biz.danmu.Constants.EVENT_DANMAKU;
import static com.whaleyvr.biz.danmu.Constants.EVENT_DISNOTICE;
import static com.whaleyvr.biz.danmu.Constants.EVENT_NOTICE;
import static com.whaleyvr.biz.danmu.Constants.EVENT_USERBANNED;

/**
 * Author: qxw
 * Date:2017/10/18
 * Introduction:
 */

public class DanMuSocketBizManager extends BaseSocketBizManager implements CommonConstants {

    DanMuSocketListener danMuSocketListener;

    public DanMuSocketBizManager(String code, String title) {
        super(code, title);
    }

    public void setDanMuSocketListener(DanMuSocketListener danMuSocketListener) {
        this.danMuSocketListener = danMuSocketListener;
    }

    public void onAllQuit() {
        getConnectionController().quit(EVENT_DANMAKU);
        getConnectionController().quit(EVENT_USERBANNED);
        getConnectionController().quit(EVENT_NOTICE);
        getConnectionController().quit(EVENT_DISNOTICE);
    }


    @Override
    protected void onJoin() {
        getConnectionController().quit(EVENT_DANMAKU);
        getConnectionController().join(EVENT_DANMAKU, new EventListener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject) args[0];
                if (jsonObject != null && danMuSocketListener != null) {
                    danMuSocketListener.onDanmaku(jsonObject.toString());
                }
            }
        });
        getConnectionController().quit(EVENT_USERBANNED);
        getConnectionController().join(EVENT_USERBANNED, new EventListener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject) args[0];
                if (jsonObject != null && danMuSocketListener != null) {
                    danMuSocketListener.onUserBanned(jsonObject.toString());
                }
            }
        });
        getConnectionController().quit(EVENT_NOTICE);
        getConnectionController().join(EVENT_NOTICE, new EventListener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject) args[0];
                if (jsonObject != null && danMuSocketListener != null) {
                    danMuSocketListener.onNotice(jsonObject.toString());
                }
            }
        });
        getConnectionController().quit(EVENT_DISNOTICE);
        getConnectionController().join(EVENT_DISNOTICE, new EventListener() {
            @Override
            public void call(Object... args) {
                Observable.create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                        if (danMuSocketListener != null) {
                            danMuSocketListener.onDisNotice();
                        }
                        emitter.onComplete();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
            }
        });

    }

    public void refreshToken(){
        getConnectionController().refreshToken();
    }

    @Override
    public void quit() {
        super.quit();
    }

    @Override
    public void verifyToken() {
        if (danMuSocketListener != null) {
            danMuSocketListener.verifyToken();
        }
    }

    @Override
    public void verifyTokened(String uid) {
        if (danMuSocketListener != null) {
            danMuSocketListener.verifyTokened(uid);
        }
    }

    @Override
    public void onLogined(String notice) {
        if (danMuSocketListener != null) {
            danMuSocketListener.onNotice(notice);
            danMuSocketListener.onLogined();
        }
    }

    @Override
    public void onLoginFailed() {
        if (danMuSocketListener != null) {
            danMuSocketListener.onLoginFailed();
        }
    }

    public interface DanMuSocketListener {
        void verifyToken();

        void verifyTokened(String uid);

        void onLogined();

        void onLoginFailed();

        void onDanmaku(String danmaku);

        void onUserBanned(String userBanned);

        void onNotice(String notice);

        void onDisNotice();
    }

}
