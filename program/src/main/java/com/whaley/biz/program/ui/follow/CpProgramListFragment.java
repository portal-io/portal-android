package com.whaley.biz.program.ui.follow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.whaley.biz.program.ui.follow.presenter.CpProgramListPresenter;
import com.whaley.biz.program.uiview.fragment.RefreshUIViewListFragment;

/**
 * Author: qxw
 * Date:2017/8/16
 * Introduction:
 */

public class CpProgramListFragment extends RefreshUIViewListFragment<CpProgramListPresenter> {

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        getRefreshLayout().setEnabled(false);
    }

    @Override
    protected boolean isShouldLoadMore() {
        return true;
    }
}
