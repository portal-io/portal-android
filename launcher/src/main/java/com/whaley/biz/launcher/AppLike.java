package com.whaley.biz.launcher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.mw.persistent.connect.info.UserInfo;
import com.snailvr.manager.BuildConfig;
import com.snailvr.manager.R;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.whaley.biz.common.http.HttpProvider;
import com.whaley.biz.common.utils.LeakCanaryUtil;
import com.whaley.biz.launcher.event.ConnectEvent;
import com.whaley.biz.launcher.model.UserModel;
import com.whaley.biz.launcher.util.ImageProvider;
import com.whaley.biz.launcher.util.SharedPreferencesUtil;
import com.whaley.biz.launcher.util.UserUtil;
import com.whaley.biz.playerui.playercontroller.PlayerController;
import com.whaley.core.appcontext.AppContextInit;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.crash.CrashHandler;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.debug.logger.LogInterceptor;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.core.network.http.HttpManager;
import com.whaleyvr.core.network.longconnection.ConnectService;
import com.whaleyvr.core.network.longconnection.Connection;
import com.whaleyvr.core.network.longconnection.ConnectionListener;
import com.whaleyvr.core.network.longconnection.ConnectionStatusListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

import java.io.File;

/**
 * Created by YangZhi on 2017/9/26 21:16.
 */

public class AppLike extends DefaultApplicationLike {

    private final static String BUGLY_APP_ID = "318344312b";

    private final static String UMENG_APP_ID = "5768e8b767e58e9de40011e1";
    private final static String PKG_NAME = "com.snailvr.manager";


    public AppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String dirPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "whaleyvr";
        AppContextInit.appContextInit(getApplicationContext(), dirPath);
        DisplayUtil.getDpi();
        Debug.setDebug(BuildConfig.DEBUG);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.setIsDevelopmentDevice(getApplicationContext(), Debug.isDebug());
        Bugly.init(getApplication(), BUGLY_APP_ID, Debug.isDebug());
        String currentProgress = getCurrentProcess();
        if (currentProgress != null && PKG_NAME.equals(currentProgress)) {
            // Application onCreate 中初始化 CrashHandler
            // 当 Crash 发生时能在本地输出日志
            if (Debug.isDebug()) {
                CrashHandler.getInstance().init();
                Debug.buildLog()
                        .tag("WhaleyVR")
                        .methodCount(2)
                        .logDisk(dirPath + File.separator + "debug")
                        .intercept(new LogInterceptor() {
                            @Override
                            public boolean log(int i, String s, String s1) {
                                return false;
                            }
                        })
                        .showThreadInfo(true)
                        .build();
                Router.openDebug();
                Router.openLog();
            }
            Router.init(getApplication());
            initEventBus();
            HttpManager.getInstance().setBuilder(HttpProvider.createRetrofitBuilder())
                    .setTestUrlProvider(HttpProvider.getTestUrlProvider());
            initModules();
            initImageLoader();
            ConnectService.start(getApplicationContext());
            Application.ActivityLifecycleCallbacks callback = Connection.getInstance().build();
            getApplication().registerActivityLifecycleCallbacks(callback);
            setConnectionListener();
            if (!SharedPreferencesUtil.getUpdate()) {
                Router.getInstance().buildExecutor("/user/service/signout").callback(new Executor.Callback<Boolean>() {
                    @Override
                    public void onCall(Boolean aBoolean) {

                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                    }
                }).excute();
                SharedPreferencesUtil.setUpdate(true);
            }
            LeakCanaryUtil.install(getApplication());


        }
    }


    private void initModules() {
        Router.getInstance().buildExecutor("/datastatistics/service/biinit").putObjParam(getApplication()).notTransParam().excute();
        Router.getInstance().buildExecutor("/push/service/init").excute();
        Router.getInstance().buildExecutor("/download/service/init").excute();
        Router.getInstance().buildExecutor("/parser/service/init").excute();
        Router.getInstance().buildExecutor("/hybrid/service/init").excute();
        Router.getInstance().buildExecutor("/playerui/service/init").excute();
        Router.getInstance().buildExecutor("/unity/service/init").excute();
        Router.getInstance().buildExecutor("/pay/service/init").excute();
        Router.getInstance().buildExecutor("/share/service/init").excute();
        Router.getInstance().buildExecutor("/program/service/init").excute();
        Router.getInstance().buildExecutor("/setting/service/init").excute();
    }


    private void initImageLoader() {
        ImageRequest.DEFUALT_PLACEHOLDER_RESID = ImageProvider.getPlacHolderResId();
        ImageRequest.DEFUALT_ERRPR_RESID = ImageProvider.getErrorResId();
        ImageRequest.DEFAULT_IMAGE_SIZE_GETTER = ImageProvider.createImageSizeGetter();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    private Context getApplicationContext() {
        return getApplication().getApplicationContext();
    }


    private void setConnectionListener() {
        Connection.getInstance().setConnectionStatusListener(new ConnectionStatusListener() {
            @Override
            public void onServiceConnected(ConnectService.ConnectBinder connectBinder) {
                Connection.getInstance().connect(getApplicationContext(), AppUtil.getDeviceToken(), new ConnectionListener() {
                    @Override
                    public void onReceive(String s) {
                        Log.d("PersistentConnect", "onReceive");
                        if (!StrUtil.isEmpty(UserUtil.accountId)) {
                            SharedPreferencesUtil.setUnreadData(UserUtil.accountId);
                            EventBus.getDefault().post(new ConnectEvent(s));
                        }
                    }

                    @Override
                    public void onDeviceLogin(String s) {
                        Log.d("PersistentConnect", "onDeviceLogin");
                        UserUtil.loginUser();
                    }

                    @Override
                    public void onUserLogin(UserInfo userInfo) {
                        //
                    }

                    @Override
                    public void onUserLogout(UserInfo userInfo) {
                        //
                    }

                    @Override
                    public void unAvailable(int i, String s) {
                        //
                    }

                    @Override
                    public void available() {
                        //
                    }
                });
            }

            @Override
            public void onServiceDisconnected() {
                //
            }
        });
    }


    private void initEventBus() {
        final EventBusBuilder eventBusBuilder = EventBus.builder();
        eventBusBuilder.logNoSubscriberMessages(Debug.isDebug());
        eventBusBuilder.logSubscriberExceptions(Debug.isDebug());
        eventBusBuilder.addIndex(new EventBusIndex());
        addEventBusIndex(eventBusBuilder, "/parser/service/eventbusindexprovider");
        addEventBusIndex(eventBusBuilder, "/danmu/service/eventbusindexprovider");
        addEventBusIndex(eventBusBuilder, "/program/service/eventbusindexprovider");
        addEventBusIndex(eventBusBuilder, "/pay/service/eventbusindexprovider");
        addEventBusIndex(eventBusBuilder, "/playerui/service/eventbusindexprovider");
        addEventBusIndex(eventBusBuilder, "/livegift/service/eventbusindexprovider");

        eventBusBuilder.installDefaultEventBus();

        PlayerController.getInstance(getApplicationContext())
                .setEventBus(eventBusBuilder.build());
    }


    private void addEventBusIndex(final EventBusBuilder eventBusBuilder, String routerPath) {
        Router.getInstance().buildExecutor(routerPath)
                .callback(new Executor.Callback<SubscriberInfoIndex>() {
                    @Override
                    public void onCall(SubscriberInfoIndex subscriberInfoIndex) {
                        eventBusBuilder.addIndex(subscriberInfoIndex);
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                });
    }


    private String getCurrentProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (mActivityManager.getRunningAppProcesses() == null)
            return null;
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (AppContextProvider.getInstance().getContext() == null) {
            return;
        }
        String currentProgress = getCurrentProcess();
        if (currentProgress != null && PKG_NAME.equals(currentProgress)) {
            ImageLoader.onTrimMemory(level);
        }
    }
}
