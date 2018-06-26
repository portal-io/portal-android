package com.whaley.biz.program.uiview.model;

import android.os.Bundle;

import com.whaley.biz.common.constants.RouterConstants;

/**
 * Author: qxw
 * Date:2017/8/15
 * Introduction:
 */

public class PageModel {

    private Bundle bundle;
    private String routerPath;
    private int activityType;
    private boolean isStartUnity;
    private Object unityObject;
    private String msg;
    private boolean isCanJump = true;
    private int requestCode;

    public boolean isStartUnity() {
        return isStartUnity;
    }

    public void setStartUnity(boolean startUnity) {
        isStartUnity = startUnity;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public boolean isCanJump() {
        return isCanJump;
    }

    public void setCanJump(boolean canJump) {
        isCanJump = canJump;
    }

    public String getRouterPath() {
        return routerPath;
    }

    public void setRouterPath(String routerPath) {
        this.routerPath = routerPath;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getUnityObject() {
        return unityObject;
    }

    public void setUnityObject(Object unityObject) {
        this.unityObject = unityObject;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
