package com.whaley.biz.common.event;

import com.whaley.core.utils.GsonUtil;

/**
 * Author: qxw
 * Date:2017/8/8
 * Introduction:
 */

public class BaseEvent{

    private String eventType;

    private Object object;

    public BaseEvent(String eventType) {
        this(eventType, null);
    }

    public BaseEvent(String eventType, Object object) {
        this.eventType = eventType;
        this.object = object;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public <T> T getObject(Class<T> clazz){
        if(object==null)
            return null;
        String json = GsonUtil.getGson().toJson(object);
        return GsonUtil.getGson().fromJson(json, clazz);
    }

    public <T> T getObject(){
        if(object==null)
            return null;
        return (T)object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
