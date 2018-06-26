package com.whaleyvr.biz.download.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.download.DownloadClient;

/**
 * Created by dell on 2017/8/24.
 */

@Route(path = "/download/service/getDownloadClient")
public class GetDownloadClient implements Executor {

    @Override
    public void excute(Object o, final Callback callback) {
        callback.onCall(new DownloadClient());
    }

    @Override
    public void init(Context context) {

    }

}
