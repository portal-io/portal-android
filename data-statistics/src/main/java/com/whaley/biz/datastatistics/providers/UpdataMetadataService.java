package com.whaley.biz.datastatistics.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.bi.BILogServiceManager;
import com.whaley.core.router.Executor;

/**
 * Author: qxw
 * Date:2017/9/29
 * Introduction:
 */
@Route(path = "/datastatistics/service/updatametadata")
public class UpdataMetadataService  implements Executor {
    @Override
    public void excute(Object o, Callback callback) {
        BILogServiceManager.getInstance().updataMetadata();
    }

    @Override
    public void init(Context context) {

    }
}
