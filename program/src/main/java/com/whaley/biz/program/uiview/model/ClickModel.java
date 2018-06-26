package com.whaley.biz.program.uiview.model;

/**
 * Author: qxw
 * Date:2017/8/18
 * Introduction:
 */

public class ClickModel {
    private String code;

    private Object mode;

    public <M> M getMode() {
        return (M) mode;
    }

    public void setMode(Object mode) {
        this.mode = mode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
