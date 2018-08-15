package com.whaley.biz.portal.model;

/**
 * Created by dell on 2018/8/10.
 */

public class EarningInfo {
    /**
     * name : 冰岛
     * code : 3
     * value : 10
     * createTime : 1533868157
     */

    private String name;
    private String code;
    private float value;
    private long createTime;
    private String type;

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public float getValue() {
        return value;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
