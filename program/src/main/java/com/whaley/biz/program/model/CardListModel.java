package com.whaley.biz.program.model;

import java.io.Serializable;

/**
 * Created by dell on 2018/1/23.
 */

public class CardListModel implements Serializable {

    private String code;
    private String name;
    private int count;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
