package com.whaley.biz.setting.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.router.Executor;

/**
 * Author: qxw
 * Date:2017/8/7
 * Introduction:
 */
@Route(path = "/setting/service/getprioritydefinition")
public class GetPriorityDefinitionService implements Executor {


    @Override
    public void excute(Object o, Callback callback) {
        callback.onCall(SharedPreferencesUtil.getDefinitionLevel());
    }

    @Override
    public void init(Context context) {

    }
}
