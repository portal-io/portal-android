package com.whaley.biz.program.ui.recommend;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.TabIndicatorFragment;
import com.whaley.biz.common.ui.presenter.TabIndicatorPresenter;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.recommend.presenter.PageTitlePresenter;

import butterknife.OnClick;

/**
 * Created by YangZhi on 2017/8/14 15:34.
 */

@Route(path = ProgramRouterPath.PAGE_HOME)
public class HomeFragment extends TabIndicatorFragment {

    PageTitlePresenter homePagePresenter;

    @Override
    protected TabIndicatorPresenter onCreatePresenter() {
        homePagePresenter = new PageTitlePresenter(this);
        return homePagePresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_page;
    }


    @OnClick(R2.id.btn_history)
    public void onClickHistory() {
        homePagePresenter.onClickHistory();
    }

    @OnClick(R2.id.btn_download)
    public void onClickDownload() {
        homePagePresenter.onClickDownload();
    }

    @OnClick(R2.id.view_search_bg)
    public void onClickSearch() {
        homePagePresenter.onClickSearch();
    }

    @OnClick(R2.id.btn_more_channel)
    public void onClick() {
        homePagePresenter.onClickMoreChannel();
    }

}
