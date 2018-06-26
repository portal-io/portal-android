package com.whaleyvr.biz.hybrid;

import java.util.Map;

/**
 * 客户端监听事件的回调(当JS send 过来时候的回调)
 * Created by YangZhi on 2016/10/8 17:43.
 */
public interface H5Callback<T> {
    Map<Object,Object> doPerform(T payload);
}
