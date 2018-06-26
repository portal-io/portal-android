package com.whaley.biz.setting.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.core.router.Executor;

/**
 * Author: qxw
 * Date:2017/8/28
 * Introduction:
 */
@Route(path = "/setting/service/getWifi")
public class GetWifiService implements Executor {
    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(SharedPreferencesUtil.getWifiOnly());
    }

    @Override
    public void init(Context context) {

    }
}
