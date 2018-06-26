package com.whaley.biz.launcher.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.launcher.festival.FestivalManager;
import com.whaley.core.router.Executor;

/**
 * Created by dell on 2018/1/26.
 */

@Route(path = "/launch/service/getfestival")
public class GetFestivalService implements Executor<Object> {

    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(FestivalManager.getInstance().isFestivalAvailable());
    }

    @Override
    public void init(Context context) {

    }
}
