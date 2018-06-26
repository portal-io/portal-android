package com.whaley.biz.setting.providers;

import android.app.Activity;
import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.router.Executor;

/**
 * Created by dell on 2017/9/30.
 */

@Route(path = "/setting/service/init")
public class InitExecutor implements Executor {

    @Override
    public void excute(Object o, Callback callback) {
        ZXingLibrary.initDisplayOpinion(AppContextProvider.getInstance().getContext());
    }

    @Override
    public void init(Context context) {

    }

}
