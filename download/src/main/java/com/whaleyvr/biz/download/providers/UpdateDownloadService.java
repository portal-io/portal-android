package com.whaleyvr.biz.download.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.download.DownloadDataManager;
import com.whaleyvr.biz.download.db.DownloadBean;

/**
 * Created by dell on 2017/8/23.
 */

@Route(path = "/download/service/updateDownloadService")
public class UpdateDownloadService implements Executor<DownloadBean> {

    @Override
    public void excute(DownloadBean o, final Callback callback) {
        DownloadDataManager.getInstance().updateNewDownload(o);
    }

    @Override
    public void init(Context context) {

    }

}
