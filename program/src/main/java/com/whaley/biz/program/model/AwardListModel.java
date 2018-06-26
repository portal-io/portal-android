package com.whaley.biz.program.model;

import java.io.Serializable;

/**
 * Created by dell on 2018/1/23.
 */

public class AwardListModel implements Serializable {

    private String mobile;
    private int type;
    private String desc;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
