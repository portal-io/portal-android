package com.whaley.biz.datastatistics;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.whaley.biz.datastatistics.model.UserInfoModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.bi.BILogServiceManager;
import com.whaley.core.bi.UserModelProvider;
import com.whaley.core.bi.model.UserModel;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.AppUtil;

/**
 * Author: qxw
 * Date:2017/9/22
 * Introduction:
 */

public class DataStatistics {

    public static void init() {
        BILogServiceManager.getInstance().initData(AnalyticsConfig.getChannel(AppContextProvider.getInstance().getContext()), new UserModelProvider() {
            UserModel userModel = new UserModel();

            @Override
            public UserModel getUserModel() {
                Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserInfoModel>() {
                    @Override
                    public void onCall(UserInfoModel userInfoModel) {
                        if (userInfoModel != null) {
                            userModel.setAccount_id(userInfoModel.getAccount_id());
                            userModel.setUsreId(userInfoModel.getDeviceId());
                        } else {
                            userModel.setUsreId(AppUtil.getDeviceId());
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        userModel.setUsreId(AppUtil.getDeviceId());
                    }
                }).excute();
                return userModel;
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void initUM(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MobclickAgent.onResume(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                MobclickAgent.onPause(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
