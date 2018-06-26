package com.whaleyvr.biz.download.providers;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.download.BGService;
import com.whaleyvr.biz.download.DownloadDataManager;

/**
 * Created by dell on 2017/8/23.
 */

@Route(path = "/download/service/init")
public class InitExecutor implements Executor {

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Object o, Callback callback) {
        Intent intent = new Intent(AppContextProvider.getInstance().getContext(), BGService.class);
        AppContextProvider.getInstance().getContext().startService(intent);
    }

}
