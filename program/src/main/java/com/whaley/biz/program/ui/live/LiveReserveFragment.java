package com.whaley.biz.program.ui.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.whaley.biz.program.R;
import com.whaley.biz.program.ui.live.presenter.LiveReserverPresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.adapter.CardVideoUIAdapter;
import com.whaley.biz.program.uiview.fragment.RefreshUIViewListFragment;

/**
 * Created by dell on 2017/8/10.
 */

public class LiveReserveFragment extends RefreshUIViewListFragment<LiveReserverPresenter> implements CardVideoUIAdapter.OnReserveClickListener {

    public static Fragment newInstance() {
        LiveReserveFragment fragment = new LiveReserveFragment();
        return fragment;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        getUiAdapter().setOnUIViewClickListener(this);
        emptyLayout.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_yugao, null, false));
    }

    @Override
    public void add(ClickableUIViewHolder viewHolder) {
        getPresenter().addReserve(viewHolder);
    }

    @Override
    public void cancel(ClickableUIViewHolder viewHolder) {
        getPresenter().cancelReserve(viewHolder);
    }

    @Override
    public void onViewClick(ClickableUIViewHolder uiViewHolder) {
        getPresenter().onViewClick(uiViewHolder);
    }


    @Override
    public void showToast(String text) {
        super.showToast(text);
    }

}
