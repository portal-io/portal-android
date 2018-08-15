package com.whaleyvr.biz.unity.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.unity.MessageControl;

/**
 * Created by dell on 2017/8/14.
 */

@Route(path = "/unity/service/startLauncher")
public class StartLauncherService implements Executor<Object> {

    @Override
    public void excute(Object o, final Callback callback) {
        MessageControl.getInstance().startUnityLauncher();
    }

    @Override
    public void init(Context context) {

    }

}
