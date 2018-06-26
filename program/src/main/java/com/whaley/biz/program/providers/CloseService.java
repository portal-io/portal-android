package com.whaley.biz.program.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.program.uiview.viewpool.CommonViewPool;
import com.whaley.core.router.Executor;


/**
 * Created by yangzhi on 2017/9/30.
 */

@Route(path = "/program/service/close")
public class CloseService implements Executor{

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Object o, Callback callback) {
        CommonViewPool.getInstance().clear();
    }
}
