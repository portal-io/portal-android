package com.whaley.biz.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.common.R;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.common.widget.emptylayout.EmptyDisplayLayout;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.widget.refresh.ILoadMoreView;
import com.whaley.core.widget.refresh.IRefreshView;
import com.whaley.core.widget.refresh.RefreshLayout;

/**
 * Created by YangZhi on 2017/7/25 17:42.
 */

public abstract class LoaderFragment<PRESENTER extends LoadPresenter, DATA> extends BaseMVPFragment<PRESENTER> implements LoaderView<DATA>, EmptyDisplayLayout.OnRetryListener {

    private RefreshLayout refreshLayout;

    protected EmptyDisplayLayout emptyLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loader, container, false);
        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.addView(inflater.inflate(getLayoutId(), refreshLayout, false));
        return view;
    }

//    protected void initRefreshLayout(View view, LayoutInflater inflater) {
//
//    }

    protected void initEmptyLayout(View view) {
        this.emptyLayout = onCreateEmptyLayout(view);
        if (emptyLayout != null) {
            emptyLayout.setOnRetryListener(this);
        }
    }


    protected EmptyDisplayLayout onCreateEmptyLayout(View rootView) {
        return (EmptyDisplayLayout) rootView.findViewById(R.id.emptyLayout);
    }

    @Override
    public EmptyDisplayView getEmptyDisplayView() {
        return emptyLayout;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getRefreshLayout() != null) {
            this.refreshLayout = getRefreshLayout();
        } else {
            this.refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        }
        initEmptyLayout(view);
        getRefreshLayout().setListener(this);
        if (isShouldLoadMore()) {
            setUpLoadMore();
        }
    }

    @Override
    protected void unBindViews() {
        super.unBindViews();
        if (refreshLayout != null) {
            refreshLayout.setListener(null);
            if (refreshLayout.getLoadMoreView() != null) {
                refreshLayout.getLoadMoreView().setListener(null);
            }
            refreshLayout = null;
        }
        if (getRefreshLayout() != null) {
            getRefreshLayout().setListener(null);
            if (getRefreshLayout().getLoadMoreView() != null) {
                getRefreshLayout().getLoadMoreView().setListener(null);
            }
        }
    }

    @Override
    public void onRefresh() {
        getPresenter().onRefresh();
    }

    @Override
    public void onLoadMore() {
        getPresenter().onLoadMore();
    }

    @Override
    public ILoadMoreView getILoadMoreView() {
        return getRefreshLayout();
    }

    @Override
    public IRefreshView getIRefreshView() {
        return getRefreshLayout();
    }

    @Override
    public void updateOnLoadMore(DATA loadMoreData) {

    }

    @Override
    public void updateOnRefresh(DATA refreshData) {

    }

    protected abstract boolean isShouldLoadMore();

    protected abstract void setUpLoadMore();

    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
    }


}
