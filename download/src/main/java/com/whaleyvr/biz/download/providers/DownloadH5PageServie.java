package com.whaleyvr.biz.download.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;

@Route(path = "/download/service/downloadH5Page")
public class DownloadH5PageServie implements Executor<DownloadH5PageModel> {

    @Override
    public void excute(DownloadH5PageModel downloadH5PageModel, final Callback callback) {
        DownloadEventController.getInstance().downloadH5Page(downloadH5PageModel,
                new DownloadEventController.downloadH5PageCallback(){
                    @Override
                    public void onResult(DownloadH5PageResultModel model) {
                        if(callback!=null) {
                            callback.onCall(model);
                        }
                    }
                });
    }

    @Override
    public void init(Context context) {

    }

}
