package com.whaley.biz.livegift.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaley.biz.livegift.EventBusIndex;
/**
 * Author: qxw
 * Date:2017/10/13
 * Introduction:
 */
@Route(path = "/livegift/service/eventbusindexprovider")
public class EventBusIndexProvider implements Executor {
    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(new EventBusIndex());
    }

    @Override
    public void init(Context context) {

    }
}
