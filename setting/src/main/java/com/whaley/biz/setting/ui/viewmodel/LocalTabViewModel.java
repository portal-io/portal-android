package com.whaley.biz.setting.ui.viewmodel;

import java.io.Serializable;

/**
 * Created by dell on 2017/8/4.
 */

public class LocalTabViewModel implements Serializable{

    private String title;

    private Object data;

    public LocalTabViewModel(String title, Object data) {
        this.title = title;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
