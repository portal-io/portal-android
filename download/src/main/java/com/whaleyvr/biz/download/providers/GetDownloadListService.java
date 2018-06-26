package com.whaleyvr.biz.download.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.download.DownloadDataManager;

/**
 * Created by dell on 2017/8/23.
 */

@Route(path = "/download/service/getDownloadListService")
public class GetDownloadListService implements Executor {

    @Override
    public void excute(Object o, final Callback callback) {
        callback.onCall(DownloadDataManager.getInstance().getDownloadBeanList());
    }

    @Override
    public void init(Context context) {

    }

}
