package com.whaleyvr.biz.download.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.download.DownloadDataManager;

/**
 * Created by dell on 2017/8/23.
 */

@Route(path = "/download/service/getDownloadStatusService")
public class GetDownloadStatusService implements Executor<String> {

    @Override
    public void excute(String o, final Callback callback) {
        callback.onCall(DownloadDataManager.getInstance().getStatus(o));
    }

    @Override
    public void init(Context context) {

    }

}
