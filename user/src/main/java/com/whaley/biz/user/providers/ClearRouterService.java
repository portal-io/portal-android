package com.whaley.biz.user.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.user.interceptor.RouterInterceptor;
import com.whaley.core.router.Executor;

/**
 * Created by dell on 2017/10/10.
 */

@Route(path = "/user/service/clearRouter")
public class ClearRouterService implements Executor {


    @Override
    public void excute(Object o, Callback callback) {
        if(RouterInterceptor.routerMap != null) {
            RouterInterceptor.routerMap.clear();
        }
    }

    @Override
    public void init(Context context) {

    }

}

