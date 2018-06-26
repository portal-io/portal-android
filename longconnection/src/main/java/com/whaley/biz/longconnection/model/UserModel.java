package com.whaley.biz.longconnection.model;

/**
 * Author: qxw
 * Date:2017/10/18
 * Introduction:
 */

public class UserModel {
    private AccessTokenBean accessTokenBean;
    private String deviceId;

    public AccessTokenBean getAccessTokenBean() {
        return accessTokenBean;
    }

    public void setAccessTokenBean(AccessTokenBean accessTokenBean) {
        this.accessTokenBean = accessTokenBean;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
