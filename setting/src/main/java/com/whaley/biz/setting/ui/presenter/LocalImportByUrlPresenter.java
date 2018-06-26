package com.whaley.biz.setting.ui.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.utils.SpaceUtil;
import com.whaley.biz.setting.constant.SettingConstants;
import com.whaley.biz.setting.util.QRcodeUtil;
import com.whaley.biz.setting.ui.repository.LocalImportByUrlRepository;
import com.whaley.biz.setting.ui.view.LocalImportByUrlView;
import com.whaley.core.inject.annotation.Repository;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dell on 2017/8/7.
 */

public class LocalImportByUrlPresenter extends BasePagePresenter<LocalImportByUrlView> {

    @Repository
    LocalImportByUrlRepository repository;

    public LocalImportByUrlPresenter(LocalImportByUrlView view) {
        super(view);
    }

    public LocalImportByUrlRepository getLocalImportByUrlRepository(){
        return repository;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        getLocalImportByUrlRepository().setTotalMemory(SpaceUtil.getTotalSize());
        getLocalImportByUrlRepository().setLeaveMemory(SpaceUtil.getAvailableSize());
        getUIView().setSize(getLocalImportByUrlRepository().getTotalMemory(), getLocalImportByUrlRepository().getLeaveMemory());
    }

    public void importByUrl() {
        try {
            URL url = new URL(getLocalImportByUrlRepository().getUrl());
        } catch (MalformedURLException e) {
            getUIView().showToast("请输入正确的网址格式");
            return;
        }
        if (TextUtils.isEmpty(getLocalImportByUrlRepository().getUrl())) {
            getUIView().showToast("请输入视频链接");
        } else {
            try {
                URL url = new URL(getLocalImportByUrlRepository().getUrl());
            } catch (MalformedURLException e) {
                getUIView().showToast("请输入正确的网址格式");
                return;
            }
            Intent intent = new Intent();
            Bundle data = new Bundle();
            data.putInt(CodeUtils.RESULT_TYPE, QRcodeUtil.RESULT_SUCCESS);
            data.putString(CodeUtils.RESULT_STRING, getLocalImportByUrlRepository().getUrl());
            intent.putExtras(data);
            getUIView().getAttachActivity().setResult(SettingConstants.CODE_RESULT_FOR_URL, intent);
            getUIView().finishView();
        }
    }

    public void onUrlChanged(String s) {
        getLocalImportByUrlRepository().setUrl(s);
    }

}
