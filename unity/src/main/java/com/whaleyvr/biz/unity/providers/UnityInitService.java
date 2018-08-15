package com.whaleyvr.biz.unity.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.unity.MessageInit;
import com.whaleyvr.biz.unity.UnityEventController;

/**
 * Created by dell on 2017/8/14.
 */

@Route(path = "/unity/service/init")
public class UnityInitService implements Executor<Object> {

    @Override
    public void excute(Object o, final Callback callback) {
        MessageInit.init();
        UnityEventController.init();
    }

    @Override
    public void init(Context context) {

    }

}

