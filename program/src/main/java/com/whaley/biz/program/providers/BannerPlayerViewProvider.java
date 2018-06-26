package com.whaley.biz.program.providers;

import android.content.Context;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.playersupport.widget.BannerPlayerView;
import com.whaley.core.router.Executor;

import java.util.Map;

/**
 * Created by YangZhi on 2017/8/28 19:21.
 */

@Route(path = ProgramRouterPath.SERVICE_PROVIDE_BANNER_PLAYER)
public class BannerPlayerViewProvider implements Executor<Context> {

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Context context, Callback callback) {
        BannerPlayerView playerView = new BannerPlayerView(context);
        callback.onCall(playerView);
    }

}
