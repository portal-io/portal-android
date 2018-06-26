package com.whaley.biz.program.ui.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.program.R;
import com.whaley.biz.program.ui.search.adapter.SearchResultAdapter;
import com.whaley.biz.program.ui.search.presenter.SearchResultPresenter;
import com.whaley.biz.program.ui.uimodel.SearchResultViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.ListAdapter;
import com.whaley.core.widget.viewholder.OnItemClickListener;

/**
 * Created by dell on 2017/8/25.
 */

public class SearchResultFragment extends RecyclerLoaderFragment<SearchResultPresenter, SearchResultViewModel> implements SearchResultView {

    SearchResultAdapter adapter;
    ImageRequest.RequestManager requestManager;

    public static SearchResultFragment newInstance(String text) {
        SearchResultFragment fragmentNew = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SearchResultPresenter.STR_PARAM_TEXT, text);;
        fragmentNew.setArguments(bundle);
        return fragmentNew;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        emptyLayout.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.layout_search_empty, null, false));
    }

    @Override
    protected boolean isShouldLoadMore() {
        return false;
    }

    @Override
    protected ListAdapter onCreateAdapter() {
        adapter = new SearchResultAdapter(requestManager);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int position) {
                getPresenter().onClick(position);
            }
        });
        return adapter;
    }

    public void search(String text){
        if(getPresenter() != null) {
            getPresenter().getRepository().setText(text);
            getPresenter().onRefresh();
        }
    }

}


