package com.whaley.biz.setting.ui.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.interactor.CleanCache;
import com.whaley.biz.setting.interactor.Setting;
import com.whaley.biz.setting.interactor.Update;
import com.whaley.biz.setting.model.UpdateModel;
import com.whaley.biz.setting.router.GoPageUtil;
import com.whaley.biz.setting.router.PageModel;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.ui.repository.SettingRepository;
import com.whaley.biz.setting.ui.view.SettingView;
import com.whaley.biz.setting.ui.viewmodel.SettingViewModel;
import com.whaley.biz.setting.update.UpdateDialog;
import com.whaley.biz.setting.update.UpdateHelper;
import com.whaley.biz.setting.update.UpdatingException;
import com.whaley.biz.setting.util.SharedPreferenceSettingUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.bi.BILogServiceManager;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.NetworkUtils;
import com.whaley.core.utils.WifiCacheObserver;
import com.whaleyvr.core.network.http.HttpManager;

import java.util.Iterator;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date:2017/7/26
 * Introduction:
 */

public class SettingPresenter extends BasePagePresenter<SettingView> {


    @Repository
    SettingRepository settingRepository;

    @UseCase
    Setting settingUseCase;

    @UseCase
    CleanCache cleanCache;

    private Disposable disposable;

    public SettingPresenter(SettingView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        settingUseCase.execute(new DisposableObserver() {
            @Override
            public void onNext(@NonNull Object o) {
                getUIView().updateList();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public SettingRepository getSettingRepository() {
        return settingRepository;
    }

    /**
     * @param isChecked
     */
    public void onCheckedChanged(boolean isChecked, int position) {
        if (isChecked && !NetworkUtils.isWiFiActive()) {
            WifiCacheObserver.callCacheDisabled();
        } else {
            WifiCacheObserver.callCacheEnabled();
        }
        List<SettingViewModel> settingViewModels = settingRepository.getSettingViewModels();
        SettingViewModel settingViewModel = settingViewModels.get(position);
        if (settingViewModel != null) {
            settingViewModel.setWifiOnly(!isChecked);
        }
        SharedPreferencesUtil.setWifiOnly(!isChecked);
    }

    public void onSegmentCheckedChanged(int index, int position) {
        List<SettingViewModel> settingViewModels = settingRepository.getSettingViewModels();
        SettingViewModel settingViewModel = settingViewModels.get(position);
        if (settingViewModel != null) {
            settingViewModel.setSegmentIndex(index);
        }
        SharedPreferencesUtil.setDefinitionLevel(index);
    }

    /**
     * 清理缓存
     */
    public void onCleanCacheClick(final SettingViewModel settingViewModel, final int position) {
        getUIView().showLoading("正在清理缓存中");
        cleanCache.execute(new UpdateUIObserver<String>(getUIView()) {
            @Override
            public void onNext(@NonNull String s) {
                settingViewModel.setText(s);
                getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.clear_done));
                getUIView().updateList(position);
            }
        });

    }

    public void onItemClick(int position) {
        SettingViewModel settingViewModel = settingRepository.getSettingViewModels().get(position);
        switch (settingViewModel.getType()) {
            case SettingViewModel.SETTING_CLEAR_CACHE:
                onCleanCacheClick(settingViewModel, position);
                break;
            case SettingViewModel.SETTING_UPDATE:
                update();
                break;
            case SettingViewModel.SETTING_TEST_API:
                boolean isHttpTest = HttpManager.getInstance().isTest();
                HttpManager.getInstance().setTest(!isHttpTest);
                settingViewModel.setText(HttpManager.getInstance().isTest() ? "是" : "否");
                getUIView().updateList(position);
                break;
            case SettingViewModel.SETTING_TEST_BI:
                boolean isBITest = BILogServiceManager.getInstance().isTest();
                BILogServiceManager.getInstance().setIsTest(!isBITest);
                settingViewModel.setText(BILogServiceManager.getInstance().isTest() ? "是" : "否");
                getUIView().updateList(position);
                break;
            case SettingViewModel.SETTING_PLAYER_DETECTOR:
                GoPageUtil.goPage(getStater(), PageModel.obtain(SettingRouterPath.PLAYER_DETECT));
                break;
            case SettingViewModel.SETTING_TEST_MIDULE:
                boolean isTestModel = SharedPreferenceSettingUtil.getTestModel();
                SharedPreferenceSettingUtil.setTestModel(!isTestModel);
                settingViewModel.setText(!isTestModel ? "是" : "否");
                getUIView().updateList(position);
                break;
        }

    }

    private void update() {
        dispose();
        disposable = new UpdateUIObserver<String>(getUIView(), true) {
            @Override
            public void onNext(String updateModelString) {
                UpdateModel updateModel = GsonUtil.getGson().fromJson(updateModelString, UpdateModel.class);
                UpdateDialog.showDialog(getUIView().getAttachActivity(), updateModel.getFilePath(), updateModel.getVersionName(),
                        updateModel.getDescription(), updateModel.getUpdateType());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                removeLoading();
                if (getUIView() != null)
                    if (e instanceof UpdatingException) {
                        getUIView().showToast("新版本正在下载中，请稍候");
                    } else if (e instanceof StatusErrorThrowable) {
                        getUIView().showToast("当前已是最新版本，无需更新");
                    } else {
                        getUIView().showToast("检查版本更新失败");
                    }
            }
        };
        UpdateHelper.getInstance().checkIfOrNotUpdating(true, (UpdateUIObserver) disposable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<SettingViewModel> settingViewModels = settingRepository.getSettingViewModels();
        Iterator iter = settingViewModels.iterator();
        int index = 0;
        while (iter.hasNext()) {
            SettingViewModel settingViewModel = (SettingViewModel) iter.next();
            if (settingViewModel != null && settingViewModel.getType() == SettingViewModel.SETTING_LEVE) {
                if (settingViewModel.getSegmentIndex() != SharedPreferencesUtil.getDefinitionLevel()) {
                    settingViewModel.setSegmentIndex(SharedPreferencesUtil.getDefinitionLevel());
                    if (getUIView() != null) {
                        getUIView().updateList(index);
                    }
                }
                break;
            }
            index++;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose();
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

}
