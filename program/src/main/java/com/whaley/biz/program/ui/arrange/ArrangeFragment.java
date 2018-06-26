package com.whaley.biz.program.ui.arrange;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.arrange.presenter.ArrangePresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.fragment.RefreshUIViewListFragment;

/**
 * Author: qxw
 * Date:2017/8/17
 * Introduction:
 */
@Route(path = ProgramRouterPath.ARRANGE)
public class ArrangeFragment extends RefreshUIViewListFragment<ArrangePresenter> {

    public static Fragment newInstance(String code) {
        Fragment fragment = new ArrangeFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ProgramConstants.KEY_PARAM_ID, code);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected boolean isShouldLoadMore() {
        return true;
    }

    @Override
    public void onViewClick(ClickableUIViewHolder uiViewHolder) {
        getPresenter().onViewClick(uiViewHolder.getData());
    }
}
