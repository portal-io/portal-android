package com.whaley.biz.common.response;

/**
 * Author: qxw
 * Date: 2016/11/1
 */

public interface BaseResponse<T> {
    int getStatus();

    boolean checkStatus();

    void setStatus(int status);

    String getMsg();

    void setMsg(String msg);

    void setData(T data);

    void setCache(boolean cache);

    boolean isCache();

    T getData();

}
