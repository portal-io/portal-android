package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/1 13:50.
 */

public abstract class VideoPreparingEvent implements Event<String>{

    @Override
    public boolean emit(String s) {
        return true;
    }

    @Override
    public int priority() {
        return 0;
    }
}
