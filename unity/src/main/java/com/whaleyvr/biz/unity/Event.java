package com.whaleyvr.biz.unity;

/**
 * Created by dell on 2017/4/17.
 */

public class Event {

    private String eventName;
    private String json;

    public Event(String eventName, String json){
        this.eventName = eventName;
        this.json = json;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}
