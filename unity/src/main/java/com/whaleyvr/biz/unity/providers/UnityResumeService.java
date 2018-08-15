package com.whaleyvr.biz.unity.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.unity.util.HermesConstants;
import com.whaleyvr.biz.unity.MessageControl;

/**
 * Created by dell on 2017/8/14.
 */

@Route(path = "/unity/service/resume")
public class UnityResumeService implements Executor<String> {

    @Override
    public void excute(String o, final Callback callback) {
        MessageControl.getInstance().sendEvent(HermesConstants.EVENT_RESUME);
    }

    @Override
    public void init(Context context) {

    }

}
