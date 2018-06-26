package com.whaley.biz.common.interactor;

/**
 * Author: qxw
 * Date: 2017/7/14
 */

public class UseCaseParam<T> {
    boolean isCache;


    T param;

    public UseCaseParam() {

    }

    public UseCaseParam(T param) {
        this.param = param;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
