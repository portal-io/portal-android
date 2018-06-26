package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.playerui.event.Event;

/**
 * Created by dell on 2017/8/29.
 */

public abstract class GoLauncherEvent implements Event {

    @Override
    public boolean emit(Object o) {
        return true;
    }

    @Override
    public int priority() {
        return 0;
    }

}

