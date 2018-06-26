package com.whaley.biz.program.ui.live.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.ui.uimodel.MemberRankItemViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YangZhi on 2017/10/13 16:28.
 */

public class MemberRankAdapter extends RecyclerViewAdapter<MemberRankItemViewModel, MemberRankAdapter.MemberViewHolder> {

    ImageRequest.RequestManager requestManager;


    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public MemberViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_member_rank_layout, viewGroup, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberViewHolder memberViewHolder, MemberRankItemViewModel memberRankItemViewModel, int position) {
        requestManager.load(memberRankItemViewModel.getImageUrl())
                .circle()
                .centerCrop()
                .small()
                .error(R.drawable.bg_placeholeder_circle_shape)
                .placeholder(R.drawable.bg_placeholeder_circle_shape)
                .into(memberViewHolder.ivImage);
        if (memberRankItemViewModel.getImageBg() == 0){
            memberViewHolder.ivImageBg.setVisibility(View.INVISIBLE);
        }else {
            memberViewHolder.ivImageBg.setVisibility(View.VISIBLE);
            memberViewHolder.ivImageBg.setImageResource(memberRankItemViewModel.getImageBg());
        }
        memberViewHolder.tvName.setText(memberRankItemViewModel.getName());
        memberViewHolder.tvContributionDes.setText(memberRankItemViewModel.getContributeText());
        memberViewHolder.tvRank.setText(memberRankItemViewModel.getRank());
        memberViewHolder.tvName.setTextColor(memberRankItemViewModel.getNameColor());
        memberViewHolder.tvContributionDes.setTextColor(memberRankItemViewModel.getContributeColor());
        memberViewHolder.tvRank.setTextColor(memberRankItemViewModel.getRankColor());
    }

    static class MemberViewHolder extends ViewHolder {

        @BindView(R2.id.iv_image_bg)
        ImageView ivImageBg;
        @BindView(R2.id.iv_image)
        ImageView ivImage;
        @BindView(R2.id.tv_rank)
        TextView tvRank;
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_contribution_des)
        TextView tvContributionDes;

        public MemberViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
