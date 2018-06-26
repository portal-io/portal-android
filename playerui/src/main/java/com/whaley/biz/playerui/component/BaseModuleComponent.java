package com.whaley.biz.playerui.component;

/**
 * Created by YangZhi on 2017/8/22 19:16.
 */

public abstract class BaseModuleComponent<T> extends BaseComponent{

    private T data;

    public void setData(T data){
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
