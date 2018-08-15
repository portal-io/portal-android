package com.whaley.biz.portal.model;

/**
 * Created by dell on 2018/8/9.
 */

public class PortalRecord {
    /**
     * name : 中超第15轮 山东鲁能VS上海申花 比赛集锦
     * code : f44d1188132f4b5b8717426ccf697801
     * createTime : 2
     * prePortal : 0
     */

    private String name;
    private String code;
    private long createTime;
    private float prePortal;

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setPrePortal(float prePortal) {
        this.prePortal = prePortal;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public long getCreateTime() {
        return createTime;
    }

    public float getPrePortal() {
        return prePortal;
    }
}
