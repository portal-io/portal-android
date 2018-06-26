package com.whaleyvr.biz.download.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.download.DownloadDataManager;

import java.util.List;

/**
 * Created by dell on 2017/8/23.
 */

@Route(path = "/download/service/removeDownloadService")
public class RemoveDownloadService implements Executor<List<String>>{

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(List<String> strings, Executor.Callback callback) {
        callback.onCall(DownloadDataManager.getInstance().removeDownloadListByIds(strings));
    }

}
