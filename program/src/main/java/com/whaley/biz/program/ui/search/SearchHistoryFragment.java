package com.whaley.biz.program.ui.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.ui.search.adapter.SearchHistoryAdapter;
import com.whaley.biz.program.ui.search.presenter.SearchHistoryPresenter;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/24.
 */

public class SearchHistoryFragment extends BaseMVPFragment<SearchHistoryPresenter> implements SearchHistoryView {

    @BindView(R2.id.layout_delete)
    RelativeLayout layoutDelete;
    @BindView(R2.id.rv_history)
    RecyclerView rvHistory;

    SearchHistoryAdapter searchHistoryAdapter;

    public static Fragment newInstance() {
        SearchHistoryFragment fragmentNew = new SearchHistoryFragment();
        Bundle bundle = new Bundle();
        fragmentNew.setArguments(bundle);
        return fragmentNew;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchHistoryAdapter = new SearchHistoryAdapter();
        searchHistoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int position) {
                Fragment parent = getParentFragment();
                if (parent != null && parent instanceof SearchFragment) {
                    SearchFragment searchFragment = (SearchFragment) parent;
                    searchFragment.searchHistory(getPresenter().getRepository().getHistoryList().get(position));
                }
            }
        });
        rvHistory.setAdapter(searchHistoryAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_history;
    }

    @Override
    public void onUpdate() {
        if (getPresenter().getRepository().getHistoryList().size() > 0) {
            layoutDelete.setVisibility(View.VISIBLE);
        } else {
            layoutDelete.setVisibility(View.GONE);
        }
        searchHistoryAdapter.setData(getPresenter().getRepository().getHistoryList());
    }

    @OnClick(R2.id.layout_delete)
    public void onClick() {
        getPresenter().clear();
    }

}
