package com.whaley.biz.jpush.model;

/**
 * Author: qxw
 * Date:2017/9/4
 * Introduction:
 */

public class PlayData {
    /**
     * 播放器类型
     */
    private int type;

    /**
     * 播放的资源id
     */
    private String id;

    private boolean isMonocular;

    public boolean isMonocular() {
        return isMonocular;
    }

    public void setMonocular(boolean monocular) {
        isMonocular = monocular;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
