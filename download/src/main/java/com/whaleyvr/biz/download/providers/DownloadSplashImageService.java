package com.whaleyvr.biz.download.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;

/**
 * Created by dell on 2017/9/5.
 */

@Route(path = "/download/service/downloadSplashImage")
public class DownloadSplashImageService implements Executor<String> {

    @Override
    public void excute(String s, final Callback callback) {
        DownloadEventController.getInstance().downloadSplashImage(s);
    }

    @Override
    public void init(Context context) {

    }

}
