package com.whaley.biz.jpush.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.jpush.PushStartController;
import com.whaley.core.debug.Debug;
import com.whaley.core.router.Executor;

/**
 * Created by YangZhi on 2017/10/12 19:10.
 */

@Route(path = "/push/service/init")
public class InitService implements Executor{

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Object o, Callback callback) {
        PushStartController.setDebugMode(Debug.isDebug());
        PushStartController.initPush();
        PushStartController.setPushTags();
    }
}
