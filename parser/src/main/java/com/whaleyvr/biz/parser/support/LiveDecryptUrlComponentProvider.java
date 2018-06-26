package com.whaleyvr.biz.parser.support;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;

/**
 * Created by YangZhi on 2017/8/29 21:33.
 */

@Route(path = "/parser/service/livedecrypturlcomponentprovider")
public class LiveDecryptUrlComponentProvider implements Executor{

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(new LiveDecryptUrlComponent());
    }


}
