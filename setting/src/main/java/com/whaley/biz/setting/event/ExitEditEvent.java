package com.whaley.biz.setting.event;

/**
 * Created by dell on 2017/8/4.
 */

public class ExitEditEvent {

    int type;

    public ExitEditEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
