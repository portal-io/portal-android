package com.whaley.biz.program.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;

/**
 * Created by yangzhi on 2017/9/28.
 */

@Route(path = "/program/service/init")
public class InitService implements Executor{

    Context context;

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void excute(Object o, Callback callback) {
    }
}
