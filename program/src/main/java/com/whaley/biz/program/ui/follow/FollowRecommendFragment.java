package com.whaley.biz.program.ui.follow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.follow.presenter.FollowRecommendPresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.adapter.PromulagtorUIAdapter;
import com.whaley.biz.program.uiview.fragment.RefreshUIViewListFragment;

/**
 * Author: qxw
 * Date:2017/8/14
 * Introduction:
 */
@Route(path = ProgramRouterPath.FOLLOW_RECOMMEND)
public class FollowRecommendFragment extends RefreshUIViewListFragment<FollowRecommendPresenter> implements PromulagtorUIAdapter.OnFollowViewClickListener {

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.title_follow_recommend));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        getUiAdapter().setOnUIViewClickListener(this);
    }



    @Override
    public void onViewClick(ClickableUIViewHolder viewHolder) {
        getPresenter().onViewClick(viewHolder.getData());
    }

    @Override
    public void onFollowClick(ClickableUIViewHolder viewHolder) {
        getPresenter().onFollowClick(viewHolder.getData(), viewHolder.getPosition());
    }

    @Override
    public void onPromulagtorClick(ClickableUIViewHolder viewHolder) {
        getPresenter().onPromulagtorClick(viewHolder.getData());
    }
}
