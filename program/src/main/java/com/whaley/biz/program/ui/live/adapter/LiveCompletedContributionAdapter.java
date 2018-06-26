package com.whaley.biz.program.ui.live.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.ui.uimodel.ContributionViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by YangZhi on 2017/10/16 14:00.
 */

public class LiveCompletedContributionAdapter extends RecyclerViewAdapter<ContributionViewModel, ViewHolder> {

    ImageRequest.RequestManager requestManager;

    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ContributionViewModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int type) {
        ViewHolder viewHolder = new ContributionViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contribution_layout, viewGroup, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, ContributionViewModel viewModel, int position) {
        ContributionViewHolder viewHolder = (ContributionViewHolder) holder;
        viewHolder.view_line.setVisibility(position==0?View.INVISIBLE:View.VISIBLE);
        viewHolder.tv_name.setText(viewModel.getName());
        viewHolder.tv_contribute.setText(viewModel.getContribute());
        if (viewHolder.tv_rank != null) {
            viewHolder.tv_rank.setText(viewModel.getRank());
        }
        requestManager.load(viewModel.getImage())
                .small()
                .centerCrop()
                .circle()
                .error(R.drawable.bg_placeholeder_circle_shape)
                .placeholder(R.drawable.bg_placeholeder_circle_shape)
                .into(viewHolder.iv_image);
    }


    static class ContributionViewHolder extends ViewHolder {
        private ImageView iv_image;
        private TextView tv_name;
        private TextView tv_contribute;
        private TextView tv_rank;
        private View view_line;

        private ContributionViewHolder(View view) {
            super(view);
            findViews();
            tv_name.setTextColor(Color.WHITE);
            tv_contribute.setTextColor(Color.WHITE);
            tv_rank.setTextColor(Color.WHITE);
        }


        private void findViews() {
            iv_image = (ImageView) getItemView().findViewById(R.id.iv_image);
            tv_name = (TextView) getItemView().findViewById(R.id.tv_name);
            tv_contribute = (TextView) getItemView().findViewById(R.id.tv_contribute);
            tv_rank = (TextView) getItemView().findViewById(R.id.tv_rank);
            view_line = getItemView().findViewById(R.id.view_line);
            view_line.setBackgroundColor(getItemView().getContext().getResources().getColor(R.color.color12_alpha_50));
        }
    }
}
