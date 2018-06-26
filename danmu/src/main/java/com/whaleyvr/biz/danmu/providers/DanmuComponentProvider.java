package com.whaleyvr.biz.danmu.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.danmu.support.DanmuComponent;

/**
 * Created by yangzhi on 2017/9/5.
 */

@Route(path = "/danmu/service/danmucomponentprovider")
public class DanmuComponentProvider implements Executor,IProvider{

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(new DanmuComponent());
    }
}
