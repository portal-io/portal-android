package com.whaley.biz.program.ui.detail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.model.SeriesModel;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Author: qxw
 * Date: 2017/1/3
 */

public class SeriesAdapter extends RecyclerViewAdapter<SeriesModel,ViewHolder> {

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_series, parent, false);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.width = (DisplayUtil.screenW - 6) / 7;
        layoutParams.height = layoutParams.width;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, SeriesModel data, int position) {
        TextView tvNum = holder.getView(R.id.tv_num);
        tvNum.setText(data.getCurEpisode() + "");
        if (data.isReal()) {
            tvNum.setEnabled(true);
            if (data.isSelected()) {
                tvNum.setSelected(true);
            } else {
                tvNum.setSelected(false);
            }
        } else {
            tvNum.setEnabled(false);
        }
    }

    public void notifyAdapter(int lastPosition, int position) {
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }
}
