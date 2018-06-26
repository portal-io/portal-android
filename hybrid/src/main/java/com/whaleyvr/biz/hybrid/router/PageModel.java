package com.whaleyvr.biz.hybrid.router;

import android.os.Bundle;

/**
 * Created by dell on 2017/8/25.
 */

public class PageModel {

    private Bundle bundle;
    private String routerPath;
    private int activityType;
    private boolean isStartUnity;
    private String unityJson;
    private String msg;
    private boolean isCanJump = true;
    private int requsetCode;

    public static PageModel obtain(String routerPath){
        PageModel pageModel = new PageModel();
        pageModel.setRouterPath(routerPath);
        return pageModel;
    }

    public String getUnityJson() {
        return unityJson;
    }

    public void setUnityJson(String unityJson) {
        this.unityJson = unityJson;
    }

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

    public int getRequsetCode() {
        return requsetCode;
    }

    public void setRequsetCode(int requsetCode) {
        this.requsetCode = requsetCode;
    }
}
