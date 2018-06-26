package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/1 13:43.
 */

public interface Event<PARAM> {
    boolean emit(PARAM param);
    int priority();
}
