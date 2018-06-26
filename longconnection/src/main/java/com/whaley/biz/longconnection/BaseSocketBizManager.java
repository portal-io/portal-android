package com.whaley.biz.longconnection;


/**
 * Author: qxw
 * Date:2017/10/18
 * Introduction:
 */

public abstract class BaseSocketBizManager implements ISocketBizManager, LongConnectionController.LongConnectionListener {

    private String code;
    private String title;
    LongConnectionController connectionController;

    public BaseSocketBizManager(String code, String title) {
        this.code = code;
        this.title = title;
    }


    public int getRoomId() {
        return getConnectionController().getRoomID();
    }

    @Override
    public final void join() {
        getConnectionController().addListener(this);
    }

    protected void onJoin() {

    }

    @Override
    public void quit() {
        getConnectionController().removeListener(this);
    }

    public LongConnectionController getConnectionController() {
        if (connectionController == null) {
            connectionController = CacheManager.lruCache.get(code);
        }
        if (connectionController == null) {
            connectionController = new LongConnectionController(code, title);
            CacheManager.lruCache.put(code, connectionController);
        }
        return connectionController;
    }

    @Override
    public void verifyTokened(String uid) {

    }

    @Override
    public void onLogined(String notice) {

    }

    @Override
    public void onLoginFailed() {
    }

    @Override
    public void verifyToken() {

    }
}
