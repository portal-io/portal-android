package com.whaley.biz.launcher.activitys;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.umeng.analytics.AnalyticsConfig;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.launcher.fragment.SplashFragment;
import com.whaley.biz.launcher.fragment.SplashLoginFragment;
import com.whaley.biz.launcher.model.UserModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DevelopmentDeviceUtil;

/**
 * Created by YangZhi on 2017/7/25 20:50.
 */

public class LauncherActivity extends CommonActivity {

    private boolean isLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (DevelopmentDeviceUtil.isDevelopmentDevice()) {
            showToast("该设备为开发设备...渠道号：" + AnalyticsConfig.getChannel(AppContextProvider.getInstance().getContext()));
//            showToast("测试热更新");
        }
    }

    @Override
    protected void addFragment() {
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                isLogin = true;
                LauncherActivity.super.addFragment();
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                isLogin = false;
                LauncherActivity.super.addFragment();
            }
        }).excute();
    }

    @Override
    protected Class getFragmentClazz() {
        if (isLogin) {
            return SplashFragment.class;
        } else {
            return SplashLoginFragment.class;
        }
    }

}
