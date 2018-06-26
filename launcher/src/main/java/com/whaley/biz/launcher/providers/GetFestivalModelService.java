package com.whaley.biz.launcher.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.launcher.festival.FestivalManager;
import com.whaley.core.router.Executor;

/**
 * Created by dell on 2018/1/29.
 */

@Route(path = "/launch/service/getfestivalmodel")
public class GetFestivalModelService implements Executor<Object> {

    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(FestivalManager.getInstance().getFestivalModel());
    }

    @Override
    public void init(Context context) {

    }
}
