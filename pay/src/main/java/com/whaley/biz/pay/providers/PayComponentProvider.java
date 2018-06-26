package com.whaley.biz.pay.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.pay.support.PayComponent;
import com.whaley.core.router.Executor;

/**
 * Created by yangzhi on 2017/8/29.
 */

@Route(path = "/pay/service/paycomponentprovider")
public class PayComponentProvider implements Executor{

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(new PayComponent());
    }
}
