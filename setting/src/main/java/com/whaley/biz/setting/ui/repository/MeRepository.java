package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.ui.viewmodel.MeViewModel;

import java.util.List;

/**
 * Created by dell on 2017/8/4.
 */

public class MeRepository extends MemoryRepository {

    private boolean isLogin;

    private UserModel userModel;

    private List<MeViewModel> meViewModels;

    private List<MeViewModel> meCardViewModels;

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

    public List<MeViewModel> getMeViewModels() {
        return meViewModels;
    }

    public void setMeViewModels(List<MeViewModel> meViewModels) {
        this.meViewModels = meViewModels;
    }

    public List<MeViewModel> getMeCardViewModels() {
        return meCardViewModels;
    }

    public void setMeCardViewModels(List<MeViewModel> meCardViewModels) {
        this.meCardViewModels = meCardViewModels;
    }
}
