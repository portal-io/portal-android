package com.whaley.biz.program.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.router.Executor;
import com.whaley.core.utils.GsonUtil;

import java.util.Map;

/**
 * Created by YangZhi on 2017/10/10 10:06.
 */

@Route(path = ProgramRouterPath.SERVICE_ROUTER_RECOMMEND)
public class RecommendRouterExecutor implements Executor<Map<String,Object>>{
    static final String PARAM_STARTER = "param_starter";
    static final String PARAM_DATA = "param_data";

    @Override
    public void init(Context context) {
    }

    @Override
    public void excute(Map<String,Object> map, Callback callback) {
        Starter starter = (Starter) map.get(PARAM_STARTER);
        String data = (String) map.get(PARAM_DATA);
        RecommendModel recommendModel = GsonUtil.getGson().fromJson(data,RecommendModel.class);
        GoPageUtil.goPage(starter, FormatPageModel.getPageModel(recommendModel));
    }


}
