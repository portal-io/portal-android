package com.whaley.biz.setting.event;

/**
 * Created by dell on 2017/8/7.
 */

public class PermissionGrantedEvent {

    private String type;

    public PermissionGrantedEvent(String type) {
        // TODO Auto-generated constructor stub
        this.type = type;
    }

    public String getType(){
        return type;
    }

}
