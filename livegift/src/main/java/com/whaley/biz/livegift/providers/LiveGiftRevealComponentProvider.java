package com.whaley.biz.livegift.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.livegift.support.LiveGiftRevealComponent;
import com.whaley.core.router.Executor;

/**
 * Author: qxw
 * Date:2017/10/20
 * Introduction:
 */
@Route(path = "/livegift/service/livegiftrevealcomponentprovider")
public class LiveGiftRevealComponentProvider implements Executor, IProvider {
    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(new LiveGiftRevealComponent());
    }

    @Override
    public void init(Context context) {

    }
}
