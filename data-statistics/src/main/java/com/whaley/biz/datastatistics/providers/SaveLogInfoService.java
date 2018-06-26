package com.whaley.biz.datastatistics.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.bi.BILogServiceManager;
import com.whaley.core.bi.model.LogInfoParam;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.utils.GsonUtil;

/**
 * Author: qxw
 * Date:2017/9/25
 * Introduction:
 */
@Route(path = "/datastatistics/service/saveloginfo")
public class SaveLogInfoService implements Executor<LogInfoParam.Builder> {
    @Override
    public void excute(LogInfoParam.Builder logInfoParam, Callback callback) {
        Log.e("SaveLogInfoService=" + GsonUtil.getGson().toJson(logInfoParam));
        BILogServiceManager.getInstance().recordLog(logInfoParam);
    }

    @Override
    public void init(Context context) {

    }
}
