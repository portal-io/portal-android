package com.whaley.biz.program.ui.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.recommend.presenter.RecommendPagePresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.fragment.RefreshUIViewListFragment;

/**
 * Created by YangZhi on 2017/8/14 17:53.
 */
@Route(path = ProgramRouterPath.PAGE_MIX)
public class RecommendPageFragment extends RefreshUIViewListFragment<RecommendPagePresenter> {

    public static Fragment newInstance(String code) {
        Fragment fragment = new RecommendPageFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ProgramConstants.KEY_PARAM_ID, code);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onViewClick(ClickableUIViewHolder viewHolder) {
        getPresenter().onViewClick(viewHolder.getData(), viewHolder.getPosition());
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        getPresenter().setUserVisibleHint(isUserVisibleHint());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getPresenter() != null) {
            getPresenter().setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    protected RecommendPagePresenter onCreatePresenter() {
        return new RecommendPagePresenter(this);
    }

    @Override
    protected void unBindViews() {
        super.unBindViews();
    }

    @Override
    protected void onFragmentVisibleChanged(boolean isVisible) {
        super.onFragmentVisibleChanged(isVisible);
    }
}
