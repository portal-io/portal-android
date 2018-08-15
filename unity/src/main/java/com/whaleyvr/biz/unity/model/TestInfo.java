package com.whaleyvr.biz.unity.model;

/**
 * Created by dell on 2017/5/2.
 */

public class TestInfo {

    private boolean isTest;
    private String versionName;
    private int versionCode;
    private String deviceId;

    public TestInfo(boolean isTest, String versionName, int versionCode, String deviceId){
        this.isTest = isTest;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.deviceId = deviceId;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
