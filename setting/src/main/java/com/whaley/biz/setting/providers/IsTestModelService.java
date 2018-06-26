package com.whaley.biz.setting.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.setting.util.SharedPreferenceSettingUtil;
import com.whaley.core.router.Executor;

/**
 * Author: qxw
 * Date:2017/9/15
 * Introduction:
 */
@Route(path = "/setting/service/istestmodel")
public class IsTestModelService implements Executor {

    @Override
    public void excute(Object o, Callback callback) {
        if (SharedPreferenceSettingUtil.getTestModel()) {
            callback.onCall(true);
        } else {
            callback.onCall(false);
        }
    }

    @Override
    public void init(Context context) {

    }
}
