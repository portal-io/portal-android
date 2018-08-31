package com.whaley.biz.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whaley.biz.common.R;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.common.widget.emptylayout.EmptyDisplayLayout;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.widget.refresh.LoadMoreView;
import com.whaley.core.widget.viewholder.ListAdapter;

import java.util.List;

/**
 * Created by YangZhi on 2017/7/31 13:14.
 */

public abstract class RecyclerLoaderFragment<PRESENTER extends LoadPresenter,DATA> extends LoaderFragment<PRESENTER,List<DATA>> {

    private RecyclerView recyclerView;

    private ListAdapter adapter;

    private LoadMoreView loadMoreView;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_recyclerview;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        this.recyclerView = onCreateRecyclerView(view);
        this.adapter = onCreateAdapter();
        RecyclerView.Adapter recylerViewAdapter = (RecyclerView.Adapter) adapter;
        this.recyclerView.setLayoutManager(onCreateLayoutManager());
        if (!isShouldLoadMore()) {
            this.recyclerView.setAdapter(recylerViewAdapter);
        }
        super.setViews(view, savedInstanceState);
    }

    @Override
    protected void unBindViews() {
        super.unBindViews();
        if (recyclerView != null) {
            recyclerView.clearOnScrollListeners();
            recyclerView = null;
        }
        adapter = null;

    }

    protected RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    protected RecyclerView onCreateRecyclerView(View rootView) {
        return (RecyclerView) rootView.findViewById(R.id.recycleView);
    }



    protected RecyclerView getRecyclerView() {
        return recyclerView;
    }



    protected abstract ListAdapter onCreateAdapter();

    protected boolean isAddScrollListener(){
        return true;
    }

    @Override
    protected void setUpLoadMore() {
        RecyclerView.Adapter recylerViewAdapter = (RecyclerView.Adapter) adapter;
        loadMoreView = getRefreshLayout().setAdapter(this.recyclerView, recylerViewAdapter, false, isAddScrollListener());
        getRefreshLayout().getLoadMoreView().setListener(this);
    }

    @Override
    public void updateOnRefresh(List<DATA> refreshData) {
        super.updateOnRefresh(refreshData);
        if(adapter!=null) {
            adapter.setData(refreshData);
        }
    }

    @Override
    public void updateOnLoadMore(List<DATA> loadMoreData) {
        super.updateOnLoadMore(loadMoreData);
        if(adapter!=null) {
            adapter.setData(loadMoreData);
        }
    }

    @Override
    public void onRetry() {
        onRefresh();
    }

    public LoadMoreView getLoadMoreView() {
        return loadMoreView;
    }
}
