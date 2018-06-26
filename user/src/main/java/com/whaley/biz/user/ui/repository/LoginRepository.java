package com.whaley.biz.user.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Author: qxw
 * Date: 2017/7/17
 */

public class LoginRepository extends MemoryRepository {

    private String userName;
    private String password;

    //hybrid登录需要
    String callbackId;

    String routerPath;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    public String getRouterPath() {
        return routerPath;
    }

    public void setRouterPath(String routerPath) {
        this.routerPath = routerPath;
    }
}
