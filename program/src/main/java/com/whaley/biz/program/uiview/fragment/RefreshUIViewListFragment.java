package com.whaley.biz.program.uiview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.OnClickableUIViewClickListener;
import com.whaley.biz.program.uiview.adapter.RecyclerViewUIAdapter;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.viewholder.ListAdapter;


/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */
public class RefreshUIViewListFragment<PRESENTER extends LoadUIViewPresenter> extends RecyclerLoaderFragment<PRESENTER, Object> implements OnClickableUIViewClickListener, LoadUIViewPresenter.RecyclerUIVIEW {

    RecyclerViewUIAdapter uiAdapter;

    RecyclerViewUIAdapter.RecyclerHolder recyclerHolder;
    ImageRequest.RequestManager requestManager;

    @Override
    protected ListAdapter onCreateAdapter() {
        if (recyclerHolder == null)
            return null;
        return recyclerHolder.getAdapter();
    }


    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        requestManager = ImageLoader.with(this);
        uiAdapter = new RecyclerViewUIAdapter();
        uiAdapter.setRealContext(getContext());
        uiAdapter.setOnUIViewClickListener(this);
        recyclerHolder = new RecyclerViewUIAdapter.RecyclerHolder(getRecyclerView());
        uiAdapter.setRecyclerHolder(recyclerHolder);
        uiAdapter.setRequestManager(requestManager);
        uiAdapter.onCreateView(getRefreshLayout(), 0);
        uiAdapter.changeVisible(isFragmentVisible());

//        uiAdapter.setOnUIViewClickListener(new OnClickableUIViewClickListener() {
//            @Override
//            public void onViewClick(ClickableUIViewHolder uiViewHolder) {
//                getPresenter().onItemClickListener(uiViewHolder.getData());
//            }
//        });
//        recyclerHolder.getAdapter().setViewPool(getViewPool());

    }

    @Override
    protected void unBindViews() {
        super.unBindViews();
        if (uiAdapter != null) {
            uiAdapter.setOnUIViewClickListener(null);
            uiAdapter.setRequestManager(null);
            uiAdapter.onDestroy();
        }
        uiAdapter = null;
        recyclerHolder = null;
        requestManager = null;
    }

    @Override
    public void bindViewModel(RecyclerViewModel recyclerViewModel) {
        if(uiAdapter!=null&&recyclerHolder!=null){
            if(!recyclerHolder.isInited()){
                uiAdapter.onInitViewHolder(recyclerHolder,recyclerViewModel,0);
                recyclerHolder.setInited();
                if (isShouldLoadMore()) {
                    RecyclerView.Adapter adapter = (RecyclerView.Adapter) onCreateAdapter();
                    getRefreshLayout().setAdapter(getRecyclerView(), adapter, false);
                    getRefreshLayout().getLoadMoreView().setListener(this);
                }
            }
        }
        if (uiAdapter != null && recyclerHolder != null && recyclerViewModel.isChanged()) {
            uiAdapter.onBindView(recyclerHolder, recyclerViewModel, 0);
            recyclerViewModel.setChanged(false);
        }
    }

    @Override
    public void updateItem(int pos) {
        if (recyclerHolder != null) {
            recyclerHolder.getAdapter().notifyItemChanged(pos);
        }
    }


    @Override
    protected void setUpLoadMore() {
    }

    @Override
    protected boolean isShouldLoadMore() {
        return false;
    }

    public RecyclerViewUIAdapter getUiAdapter() {
        return uiAdapter;
    }


    @Override
    public void onViewClick(ClickableUIViewHolder uiViewHolder) {
        getPresenter().onViewClick(uiViewHolder.getData());
    }

    @Override
    protected void onFragmentVisibleChanged(boolean isVisible) {
        super.onFragmentVisibleChanged(isVisible);
        if (uiAdapter != null) {
            uiAdapter.changeVisible(isVisible);
        }
    }

    @Override
    public RecyclerView getRecyclerViewRoot() {
        if(recyclerHolder!=null) {
            return recyclerHolder.getRootView();
        }
        return null;
    }
}
