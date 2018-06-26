package com.whaley.biz.user.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.ui.view.CompleteUserInfoView;

/**
 * Author: qxw
 * Date:2017/8/7
 * Introduction:
 */

public class CompleteUserInfoRepository extends MemoryRepository {
    private String nickname;
    private String password;

    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
