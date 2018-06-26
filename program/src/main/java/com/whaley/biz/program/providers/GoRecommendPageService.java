package com.whaley.biz.program.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.router.Executor;
import com.whaley.core.utils.GsonUtil;

/**
 * Created by dell on 2017/9/5.
 */

@Route(path = ProgramRouterPath.SERVICE_GO_RECOMMEND_PAGE)
public class GoRecommendPageService implements Executor<String> {

    @Override
    public void excute(String s, final Callback callback) {
        RecommendModel recommendModel = GsonUtil.getGson().fromJson(s, RecommendModel.class);
        PageModel pageModel = FormatPageModel.getPageModel(recommendModel);
        GoPageUtil.goPage(AppContextProvider.getInstance().getStarter(), pageModel);
    }

    @Override
    public void init(Context context) {

    }
}
