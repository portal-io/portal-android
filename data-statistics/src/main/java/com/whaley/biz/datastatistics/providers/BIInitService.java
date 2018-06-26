package com.whaley.biz.datastatistics.providers;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.whaley.biz.datastatistics.DataStatistics;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.bi.BILogServiceManager;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.utils.ChannelUtil;

/**
 * Author: qxw
 * Date:2017/9/25
 * Introduction:
 */
@Route(path = "/datastatistics/service/biinit")
public class BIInitService implements Executor<Application> {

    private final static String UMENG_APP_ID = "5768e8b767e58e9de40011e1";

    @Override
    public void excute(Application application, Callback callback) {
        MobclickAgent.setDebugMode(Debug.isDebug());
        MobclickAgent.enableEncrypt(false);
//        String channel = ChannelUtil.getChannel(AppContextProvider.getInstance().getContext());
//        Log.d("channel1=" + channel);
//        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(AppContextProvider.getInstance().getContext(), UMENG_APP_ID, channel));
        Log.d("channel2=" + AnalyticsConfig.getChannel(AppContextProvider.getInstance().getContext()));
        DataStatistics.initUM(application);
        DataStatistics.init();
        BILogServiceManager.getInstance().updataMetadata();
    }

    @Override
    public void init(Context context) {

    }
}
