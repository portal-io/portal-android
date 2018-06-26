package com.whaley.biz.setting.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.ui.viewmodel.HistoryViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/24.
 */

public class HistoryAdapter extends RecyclerViewAdapter<HistoryViewModel, ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private ImageRequest.RequestManager requestManager;

    private boolean isCheck;

    public HistoryAdapter(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, HistoryViewModel data, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvDuration = holder.getView(R.id.tv_duration);
        ImageView ivPic = holder.getView(R.id.iv_pic);
        RelativeLayout rlCheck = holder.getView(R.id.rl_check);
        ImageView ivCheck = holder.getView(R.id.iv_check);
        ImageView tag = holder.getView(R.id.tag);
        if (isCheck) {
            rlCheck.setVisibility(View.VISIBLE);
        } else {
            rlCheck.setVisibility(View.GONE);
        }
        ivCheck.setSelected(data.isSelect);
        if (data.isDrama()) {
            tag.setVisibility(View.VISIBLE);
            tvDuration.setText(data.duration);
            tvDuration.setVisibility(View.INVISIBLE);
        } else {
            tag.setVisibility(View.GONE);
            tvDuration.setText(data.duration);
            tvDuration.setVisibility(View.VISIBLE);
        }
        requestManager.load(data.pic).medium().placeholder(R.mipmap.default_history).fitCenter().into(ivPic);
        tvName.setText(data.name);
    }

    @Override
    protected void onSetItemClick(ViewHolder holder, int position) {
        super.onSetItemClick(holder, position);
    }

    @Override
    public long getHeaderId(int position) {
        if (getData().size() > position) {
            return getData().get(position).headType;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headType = (int) getHeaderId(position);
        if (headType != -1) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(getData().get(position).headDate);
        }
    }

}
