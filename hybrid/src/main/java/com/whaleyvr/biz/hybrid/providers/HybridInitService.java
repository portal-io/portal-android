package com.whaleyvr.biz.hybrid.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.tencent.smtt.sdk.QbSdk;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.hybrid.event.HybridInitController;

/**
 * Created by dell on 2017/8/10.
 */

@Route(path = "/hybrid/service/init")
public class HybridInitService implements Executor<Object> {

    @Override
    public void excute(Object o, final Callback callback) {
        QbSdk.initX5Environment(AppContextProvider.getInstance().getContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.d("X5", "onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.d("X5", "onViewInitFinished result=" + b);
            }
        });
    }

    @Override
    public void init(Context context) {

    }

}
