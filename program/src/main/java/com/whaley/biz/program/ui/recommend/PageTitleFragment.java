package com.whaley.biz.program.ui.recommend;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.TabIndicatorFragment;
import com.whaley.biz.common.ui.presenter.TabIndicatorPresenter;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.recommend.presenter.PageTitlePresenter;

/**
 * Author: qxw
 * Date:2017/8/24
 * Introduction:
 */
@Route(path = ProgramRouterPath.PAGE_TITLE)
public class PageTitleFragment extends TabIndicatorFragment {

    PageTitlePresenter pageTitlePresenter;

    @Override
    protected TabIndicatorPresenter onCreatePresenter() {
        pageTitlePresenter = new PageTitlePresenter(this);
        return pageTitlePresenter;
    }
}
