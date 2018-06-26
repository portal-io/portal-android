package com.whaley.biz.playerui.event;

/**
 * Created by yangzhi on 2017/8/29.
 */

public class ModuleEvent extends PriorityEvent{

    private String eventName;

    private Object param;


    public ModuleEvent(String eventName, Object param) {
        this.eventName = eventName;
        this.param = param;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getParam() {
        return param;
    }
}
