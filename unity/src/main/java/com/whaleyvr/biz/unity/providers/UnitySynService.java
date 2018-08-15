package com.whaleyvr.biz.unity.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.unity.MessageControl;

import static com.whaleyvr.biz.unity.util.HermesConstants.EVENT_SYN;

/**
 * Created by dell on 2017/8/14.
 */

@Route(path = "/unity/service/syn")
public class UnitySynService implements Executor<String> {

    @Override
    public void excute(String o, final Callback callback) {
        MessageControl.getInstance().sendEvent(EVENT_SYN);
    }

    @Override
    public void init(Context context) {

    }

}
