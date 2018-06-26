package com.whaley.biz.launcher.presenter;

import android.os.Bundle;
import android.text.TextUtils;

//import com.umeng.analytics.AnalyticsConfig;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.launcher.repository.SplashRepository;
import com.whaley.biz.launcher.view.SplashView;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;

/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */

public class SplashPresenter extends BaseSplashPresenter<SplashView> {

    public SplashPresenter(SplashView view) {
        super(view);
    }

    @Repository
    SplashRepository repository;

    public SplashRepository getRepository() {
        return repository;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        initChannelLogo();
    }

    public void onAmintionEnd() {
        if (getUIView() != null && getUIView().getAttachActivity()!=null) {
            startActivity();
        }
    }

    private void initChannelLogo() {
//        String channelName = AnalyticsConfig.getChannel(AppContextProvider.getInstance().getContext());
//        if (!TextUtils.isEmpty(channelName)) {
//            getRepository().setChannelName(channelName);
//            if (getUIView() != null)
//                getUIView().loadLogoImage();
//        }
    }

}
