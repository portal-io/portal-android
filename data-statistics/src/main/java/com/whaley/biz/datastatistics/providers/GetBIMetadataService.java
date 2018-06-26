package com.whaley.biz.datastatistics.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.bi.BILogServiceManager;
import com.whaley.core.router.Executor;


/**
 * Author: qxw
 * Date:2017/9/25
 * Introduction:
 */
@Route(path = "/datastatistics/service/getmetadata")
public class GetBIMetadataService implements Executor {

    @Override
    public void excute(Object o, Callback callback) {
        String json = BILogServiceManager.getInstance().getMetadata();
        callback.onCall(json);
    }

    @Override
    public void init(Context context) {

    }
}
