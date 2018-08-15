package com.whaley.biz.portal.ui.viewmodel;

/**
 * Created by dell on 2018/8/2.
 */

public class PortalViewModel {

    private String name;
    private String code;
    private String value;
    private String time;
    private int type; //0:SELF_PROPAGATION 1:PLAY_VIDEO

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
