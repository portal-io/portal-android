package com.whaley.biz.pay.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.pay.PayPlugin;
import com.whaley.core.router.Executor;

/**
 * Created by dell on 2017/8/14.
 */

@Route(path = "/pay/service/init")
public class PayInitService implements Executor<Object> {

    @Override
    public void excute(Object o, final Callback callback) {
        PayPlugin.getInstance();
    }

    @Override
    public void init(Context context) {

    }

}

