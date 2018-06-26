package com.whaleyvr.biz.hybrid;

import java.util.Map;

/**
 * Js 回调事件监听器(当客户端send后js的回调)
 */
public interface OnCompletedListener {
    void onCompleted(Map<Object, Object> payload);
}
