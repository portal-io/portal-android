package com.whaley.biz.setting.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whaley.biz.setting.R;
import com.whaley.biz.setting.ui.viewmodel.PayDetailViewModel;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/4.
 */

public class PayDetailAdapter extends RecyclerViewAdapter<PayDetailViewModel, ViewHolder> {

    public PayDetailAdapter() {
        //
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pay_content, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder collectViewHolder, PayDetailViewModel payDetailViewModel, int position) {
        bindViewHolder(payDetailViewModel, collectViewHolder, position);
    }

    private void bindViewHolder(PayDetailViewModel payDetailViewModel, ViewHolder holder, int position) {
        TextView tvPrice = holder.getView(R.id.tv_price);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvPayTime = holder.getView(R.id.tv_pay_time);
        TextView tvPayChannel = holder.getView(R.id.tv_pay_channel);
        tvPrice.setText(payDetailViewModel.getPrice());
        tvName.setText(payDetailViewModel.getName());
        tvPayTime.setText(payDetailViewModel.getTime());
        tvPayChannel.setText(payDetailViewModel.getChannel());
    }

}
