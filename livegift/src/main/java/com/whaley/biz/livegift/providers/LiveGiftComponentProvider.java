package com.whaley.biz.livegift.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.livegift.support.LiveGiftSendComponent;
import com.whaley.core.router.Executor;

/**
 * Author: qxw
 * Date:2017/10/13
 * Introduction:
 */
@Route(path = "/livegift/service/livegiftcomponentprovider")
public class LiveGiftComponentProvider implements Executor, IProvider {
    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(new LiveGiftSendComponent());
    }

    @Override
    public void init(Context context) {

    }
}
