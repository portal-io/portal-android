package com.whaley.biz.program.ui.recommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.recommend.presenter.CompilationPresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.fragment.RefreshUIViewListFragment;

/**
 * Created by dell on 2017/8/10.
 * 分页推荐页（节目专题集合页面）
 */
@Route(path = ProgramRouterPath.COMPILATION)
public class CompilationFragment extends RefreshUIViewListFragment<CompilationPresenter> {

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getPresenter().getCompilationRepository().getTitle());
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        getPresenter().setUserVisibleHint(isUserVisibleHint());
    }

    public static Fragment newInstance(String code, String bitCode) {
        CompilationFragment fragment = new CompilationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CompilationPresenter.STR_CODE, code);
        bundle.putString(CompilationPresenter.STR_BIT_CODE, bitCode);
        bundle.putString(ProgramConstants.KEY_PARAM_GET_DATA_USECASE_PATH, ProgramRouterPath.USECASE_RECOMMEND_LIST);
        bundle.putString(ProgramConstants.KEY_PARAM_MAPPER_PATH, ProgramRouterPath.MAPPER_COMPILATION);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected CompilationPresenter onCreatePresenter() {
        return new CompilationPresenter(this);
    }


    @Override
    public void onViewClick(ClickableUIViewHolder uiViewHolder) {
        getPresenter().onViewClick(uiViewHolder.getData());
        Fragment fragment = getParentFragment();
        if (fragment != null) {
            String name = fragment.getClass().getSimpleName();
            if ("HomeFragment".equals(name)) {
                getPresenter().setBIOnClick(uiViewHolder.getData());
            }
        }
    }

    @Override
    protected boolean isShouldLoadMore() {
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getPresenter() != null) {
            getPresenter().setUserVisibleHint(isVisibleToUser);
        }
    }

}
