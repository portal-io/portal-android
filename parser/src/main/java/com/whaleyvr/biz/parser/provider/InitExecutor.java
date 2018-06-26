package com.whaleyvr.biz.parser.provider;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.core.router.Executor;
import com.whaleyvr.biz.parser.videoparser.VideoParserInit;

/**
 * Created by dell on 2017/8/24.
 */

@Route(path = "/parser/service/init")
public class InitExecutor implements Executor<Context>{

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Context context, Callback callback) {
        VideoParserInit.init();
    }

}
