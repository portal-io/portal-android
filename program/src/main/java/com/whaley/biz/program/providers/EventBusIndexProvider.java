package com.whaley.biz.program.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.program.EventBusIndex;
import com.whaley.core.router.Executor;

/**
 * Created by yangzhi on 2017/9/9.
 */

@Route(path = "/program/service/eventbusindexprovider")
public class EventBusIndexProvider implements Executor{
    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(new EventBusIndex());
    }
}