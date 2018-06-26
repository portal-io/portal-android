package com.whaley.biz.setting.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.ui.adapter.PayDetailAdapter;
import com.whaley.biz.setting.ui.presenter.CurrencyDetailPresenter;
import com.whaley.biz.setting.ui.viewmodel.PayDetailViewModel;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.ListAdapter;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import java.util.List;

import butterknife.BindView;

/**
 * Created by dell on 2017/10/13.
 */

public class CurrencyDetailFragment extends RecyclerLoaderFragment<CurrencyDetailPresenter, PayDetailViewModel> implements CurrencyDetailView {

    @BindView(R2.id.tv_pay_num)
    TextView tvPayNum;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pay_details;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if(getTitleBar()!=null){
            getTitleBar().setTitleText("充值明细");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, null, false);
        ((TextView)emptyView.findViewById(R.id.tv_empty)).setText("充值记录为空");
        emptyLayout.setEmptyView(emptyView);
    }

    @Override
    protected ListAdapter onCreateAdapter() {
        PayDetailAdapter adapter = new PayDetailAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int i) {
                //
            }
        });
        return adapter;
    }

    @Override
    protected boolean isShouldLoadMore() {
        return true;
    }

    @Override
    public void updateOnLoadMore(List<PayDetailViewModel> loadMoreData) {
        super.updateOnLoadMore(loadMoreData);
        tvPayNum.setText(getPresenter().getCurrencyDetailRepository().getBuyNum());
    }

    @Override
    public void updateOnRefresh(List<PayDetailViewModel> refreshData) {
        super.updateOnRefresh(refreshData);
        tvPayNum.setText(getPresenter().getCurrencyDetailRepository().getBuyNum());
    }

}

