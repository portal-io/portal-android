package com.whaley.biz.user.model.portal;

import com.whaley.biz.common.response.PortalResponse;

/**
 * Created by dell on 2018/8/8.
 */

public class SyncResponse extends PortalResponse{

    private AccessTokenModel accessToken;
    private UserInfoModel userInfo;

    public AccessTokenModel getAccessTokenModel() {
        return accessToken;
    }

    public void setAccessTokenModel(AccessTokenModel accessTokenModel) {
        this.accessToken = accessTokenModel;
    }

    public UserInfoModel getUserInfoModel() {
        return userInfo;
    }

    public void setUserInfoModel(UserInfoModel userInfoModel) {
        this.userInfo = userInfoModel;
    }
}
