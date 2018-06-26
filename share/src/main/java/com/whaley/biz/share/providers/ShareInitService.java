package com.whaley.biz.share.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.share.ShareSettingUtil;
import com.whaley.core.router.Executor;

/**
 * Author: qxw
 * Date:2017/9/26
 * Introduction:
 */
@Route(path = "/share/service/init")
public class ShareInitService implements Executor {
    @Override
    public void excute(Object o, Callback callback) {
        ShareSettingUtil.initShare();
    }

    @Override
    public void init(Context context) {

    }
}
