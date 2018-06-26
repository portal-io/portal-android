package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.ui.viewmodel.SettingViewModel;

import java.util.List;

/**
 * Author: qxw
 * Date:2017/7/31
 * Introduction:
 */

public class SettingRepository extends MemoryRepository {

    private List<SettingViewModel> settingViewModels;

    public List<SettingViewModel> getSettingViewModels() {
        return settingViewModels;
    }

    public void setSettingViewModels(List<SettingViewModel> settingViewModels) {
        this.settingViewModels = settingViewModels;
    }
}
