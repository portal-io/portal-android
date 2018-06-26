package com.whaley.biz.program.ui.live.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.ui.uimodel.ContributionViewModel;
import com.whaley.biz.program.ui.uimodel.HeadContributionViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

import java.util.List;

/**
 * Created by YangZhi on 2017/10/12 12:30.
 */

public class ContributionAdapter extends RecyclerViewAdapter<Object, ViewHolder> {

    static final int TYPE_HEADER = 1;

    static final int TYPE_NORMAL = 0;

    ImageRequest.RequestManager requestManager;

    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int type) {
        ViewHolder viewHolder;
        switch (type) {
            case TYPE_HEADER:
                viewHolder = new HeadContributionViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contribution_head_layout, viewGroup, false));
                break;
            default:
                viewHolder = ContributionViewHolder.createViewHolderByNormal(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contribution_layout, viewGroup, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object o, int position) {
        if (position == 0) {
            HeadContributionViewHolder holder = (HeadContributionViewHolder) viewHolder;
            HeadContributionViewModel viewModel = (HeadContributionViewModel) o;
            List<ContributionViewModel> contributionViewModels = viewModel.getContributionViewModels();
            ContributionViewModel contributionViewModel = contributionViewModels.get(0);
            onBindContributionViewHolder(holder.firstViewHolder, contributionViewModel, 0);

            contributionViewModel = contributionViewModels.get(1);
            if (contributionViewModel.isRealData()) {
                holder.imageSecondReal.setVisibility(View.VISIBLE);
                holder.imageSecondFake.setVisibility(View.GONE);
            } else {
                holder.imageSecondReal.setVisibility(View.GONE);
                holder.imageSecondFake.setVisibility(View.VISIBLE);
            }
            onBindContributionViewHolder(holder.secondViewHolder, contributionViewModel, 0);

            contributionViewModel = contributionViewModels.get(2);
            if (contributionViewModel.isRealData()) {
                holder.imageThridReal.setVisibility(View.VISIBLE);
                holder.imageThridFake.setVisibility(View.GONE);
            } else {
                holder.imageThridReal.setVisibility(View.GONE);
                holder.imageThridFake.setVisibility(View.VISIBLE);
            }
            onBindContributionViewHolder(holder.thirdViewHolder, contributionViewModel, 0);
            return;
        }
        ContributionViewHolder holder = (ContributionViewHolder) viewHolder;
        onBindContributionViewHolder(holder, (ContributionViewModel) o, position);
    }

    public void onBindContributionViewHolder(ContributionViewHolder viewHolder, ContributionViewModel viewModel, int position) {
        viewHolder.tv_name.setText(viewModel.getName());
        viewHolder.tv_contribute.setText(viewModel.getContribute());
        if (viewHolder.tv_rank != null) {
            viewHolder.tv_rank.setText(viewModel.getRank());
        }
        if (viewHolder.iv_login_user_tip != null){
            if(viewModel.isLoginUser()){
                viewHolder.iv_login_user_tip.setVisibility(View.VISIBLE);
            }else {
                viewHolder.iv_login_user_tip.setVisibility(View.GONE);
            }
        }
        if (viewModel.isRealData()) {
            requestManager.load(viewModel.getImage())
                    .small()
                    .centerCrop()
                    .circle()
                    .placeholder(R.drawable.bg_placeholeder_circle_shape)
                    .error(R.drawable.bg_placeholeder_circle_shape)
                    .into(viewHolder.iv_image);
        } else {
            viewHolder.iv_image.setImageResource(0);
        }
    }

    static class HeadContributionViewHolder extends ViewHolder {

        private ContributionViewHolder firstViewHolder;

        private ContributionViewHolder secondViewHolder;

        private View imageSecondReal;

        private View imageSecondFake;

        private ContributionViewHolder thirdViewHolder;

        private View imageThridReal;

        private View imageThridFake;

        public HeadContributionViewHolder(View view) {
            super(view);
            View layout = view.findViewById(R.id.layout_first);
            firstViewHolder = ContributionViewHolder.createViewHolderByHeader(layout);
            firstViewHolder.iv_image = (ImageView) layout.findViewById(R.id.iv_image_first);
            firstViewHolder.tv_name = (TextView) layout.findViewById(R.id.tv_name_first);
            firstViewHolder.tv_contribute = (TextView) layout.findViewById(R.id.tv_contribute_first);

            layout = view.findViewById(R.id.layout_second);
            secondViewHolder = ContributionViewHolder.createViewHolderByHeader(layout);
            secondViewHolder.iv_image = (ImageView) layout.findViewById(R.id.iv_image_second);
            secondViewHolder.tv_name = (TextView) layout.findViewById(R.id.tv_name_second);
            secondViewHolder.tv_contribute = (TextView) layout.findViewById(R.id.tv_contribute_second);
            imageSecondReal = layout.findViewById(R.id.layout_image_second_real);
            imageSecondFake = layout.findViewById(R.id.iv_image_second_fake);


            layout = view.findViewById(R.id.layout_third);
            thirdViewHolder = ContributionViewHolder.createViewHolderByHeader(layout);
            thirdViewHolder.iv_image = (ImageView) layout.findViewById(R.id.iv_image_thrid);
            thirdViewHolder.tv_name = (TextView) layout.findViewById(R.id.tv_name_thrid);
            thirdViewHolder.tv_contribute = (TextView) layout.findViewById(R.id.tv_contribute_thrid);
            imageThridReal = layout.findViewById(R.id.layout_image_thrid_real);
            imageThridFake = layout.findViewById(R.id.iv_image_thrid_fake);
        }
    }

    public static class ContributionViewHolder extends ViewHolder {
        private ImageView iv_image;
        private TextView tv_name;
        private TextView tv_contribute;
        private TextView tv_rank;
        private View view_line;
        private ImageView iv_login_user_tip;

        private ContributionViewHolder(View view) {
            super(view);
        }

        public static ContributionViewHolder createViewHolderByHeader(View view) {
            return new ContributionViewHolder(view);
        }

        public static ContributionViewHolder createViewHolderByNormal(View view) {
            ContributionViewHolder contributionViewHolder = new ContributionViewHolder(view);
            contributionViewHolder.findViews();
            return contributionViewHolder;
        }

        private void findViews() {
            iv_image = (ImageView) getItemView().findViewById(R.id.iv_image);
            tv_name = (TextView) getItemView().findViewById(R.id.tv_name);
            tv_contribute = (TextView) getItemView().findViewById(R.id.tv_contribute);
            tv_rank = (TextView) getItemView().findViewById(R.id.tv_rank);
            view_line = getItemView().findViewById(R.id.view_line);
            iv_login_user_tip = (ImageView) getItemView().findViewById(R.id.iv_login_user_tip);
        }

        public void hideLine() {
            view_line.setVisibility(View.INVISIBLE);
        }
    }
}
