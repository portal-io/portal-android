package com.whaley.biz.program.ui.follow.repository;

import com.whaley.biz.common.repository.LoaderRepository;
import com.whaley.biz.program.model.user.UserModel;

import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;

/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */

public class FollowRepository extends LoaderRepository {


    private boolean isLogin;
    private UserModel userModel;


    private RecyclerViewModel recyclerViewModel;


    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public RecyclerViewModel getRecyclerViewModel() {
        return recyclerViewModel;
    }

    public void setRecyclerViewModel(RecyclerViewModel recyclerViewModel) {
        this.recyclerViewModel = recyclerViewModel;
    }
}
