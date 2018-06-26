package com.whaleyvr.biz.hybrid.model.user;

/**
 * Author: qxw
 * Date:2018/1/26
 * Introduction:
 */

public class ActivityShareUserModel {
    public String uid;
    public String avatar;
    public String mobile;
    public String uname;

    public ActivityShareUserModel(UserModel userModel) {
        uid = userModel.getAccount_id();
        avatar = userModel.getAvatar();
        mobile = userModel.getMobile();
        uname = userModel.getNickname();
    }
}
