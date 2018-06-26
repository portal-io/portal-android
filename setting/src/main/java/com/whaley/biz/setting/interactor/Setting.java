package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.ui.repository.SettingRepository;
import com.whaley.biz.setting.ui.viewmodel.SettingViewModel;
import com.whaley.biz.setting.util.SharedPreferenceSettingUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.bi.BILogServiceManager;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.CacheCleanManager;
import com.whaleyvr.core.network.http.HttpManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Author: qxw
 * Date:2017/7/31
 * Introduction:
 */

public class Setting extends BaseUseCase {

    protected static String TAG = "SettingUseCase";

    public Setting(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable buildUseCaseObservable(Object o) {
        return Observable.just(setSettingViewModel())
                .doOnNext(new Consumer<List<SettingViewModel>>() {
                    @Override
                    public void accept(@NonNull List<SettingViewModel> settingViewModels) throws Exception {
                        getRepositoryManager().obtainMemoryService(SettingRepository.class).setSettingViewModels(settingViewModels);
                    }
                });
    }

    private List<SettingViewModel> setSettingViewModel() {
        List<SettingViewModel> settingViewModels = new ArrayList<>();
        addSettingViewModel(settingViewModels, R.string.setting_clear_cache, getCache(), SettingViewModel.SETTING_CLEAR_CACHE);
        addWifiModel(settingViewModels);
        addSettingViewModel(settingViewModels, R.string.setting_player_detector, true, SettingViewModel.SETTING_PLAYER_DETECTOR);
        addLeveModel(settingViewModels);
        addSettingViewModel(settingViewModels, R.string.setting_version_update, true, SettingViewModel.SETTING_UPDATE);
        if (Debug.isDebug()) {
            addSettingViewModel(settingViewModels, R.string.setting_test_api, HttpManager.getInstance().isTest() ? "是" : "否", SettingViewModel.SETTING_TEST_API);
            addSettingViewModel(settingViewModels, R.string.setting_test_bi, BILogServiceManager.getInstance().isTest() ? "是" : "否", SettingViewModel.SETTING_TEST_BI);
            addSettingViewModel(settingViewModels, R.string.setting_test_module, SharedPreferenceSettingUtil.getTestModel() ? "是" : "否", SettingViewModel.SETTING_TEST_MIDULE);
        }
        return settingViewModels;
    }

    private void addLeveModel(List<SettingViewModel> settingViewModels) {
        SettingViewModel settingViewModel = newSettingViewModel(R.string.setting_default_level);
        settingViewModel.setSegmentSize(2);
        String[] segment = {AppContextProvider.getInstance().getContext().getString(R.string.high_definition),
                AppContextProvider.getInstance().getContext().getString(R.string.super_definition)};
        settingViewModel.setSegmentList(segment);
        settingViewModel.setOnClick(false);
        settingViewModel.setType(SettingViewModel.SETTING_LEVE);
        settingViewModel.setSegmentIndex(SharedPreferencesUtil.getDefinitionLevel());
        settingViewModel.setText("");
        settingViewModels.add(settingViewModel);
    }

    private void addWifiModel(List<SettingViewModel> settingViewModels) {
        SettingViewModel settingViewModel = newSettingViewModel(R.string.setting_only_wifi);
        settingViewModel.setCheckboxPic(R.drawable.checkbox_wifi);
        settingViewModel.setOnClick(false);
        settingViewModel.setWifiOnly(SharedPreferencesUtil.getWifiOnly());
        settingViewModel.setType(SettingViewModel.SETTING_WIFI);
        settingViewModel.setText("");
        settingViewModels.add(settingViewModel);
    }

    private void addSettingViewModel(List<SettingViewModel> settingViewModels, int stringID, boolean isRightPic, int type) {
        addSettingViewModel(settingViewModels, stringID, null, isRightPic, type);
    }

    private void addSettingViewModel(List<SettingViewModel> settingViewModels, int stringID, String text, int type) {
        addSettingViewModel(settingViewModels, stringID, text, false, type);
    }

    private void addSettingViewModel(List<SettingViewModel> settingViewModels, int stringID, String text, boolean isRightPic, int type) {
        SettingViewModel settingViewModel = newSettingViewModel(stringID);
        if (isRightPic) {
            settingViewModel.setRightPic(R.mipmap.setting_special_into);
        }
        if (text != null) {
            settingViewModel.setText(text);
        }
        settingViewModel.setType(type);
        settingViewModels.add(settingViewModel);
    }

    private SettingViewModel newSettingViewModel(int stringID) {
        SettingViewModel settingViewModel = new SettingViewModel();
        settingViewModel.setName(AppContextProvider.getInstance().getContext().getString(stringID));
        return settingViewModel;
    }

    private String getCache() {
        String result = "";
        try {
            result = CacheCleanManager.getTotalCacheSize(AppContextProvider.getInstance().getContext());
        } catch (Exception e) {
            Log.e(e, TAG + "onCreate");
        }
        return result;
    }


}
