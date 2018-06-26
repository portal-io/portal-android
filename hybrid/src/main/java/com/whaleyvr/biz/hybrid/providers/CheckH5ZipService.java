package com.whaleyvr.biz.hybrid.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.hybrid.event.HybridInitController;

/**
 * Created by dell on 2017/9/7.
 */

@Route(path = "/hybrid/service/checkH5Zip")
public class CheckH5ZipService implements Executor<Object> {

    @Override
    public void excute(Object o, final Callback callback) {
        HybridInitController.init().checkH5Zip();
    }

    @Override
    public void init(Context context) {

    }

}
