package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.viewmodel.LiveUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveUIAdapter extends BaseUIAdapter<LiveUIAdapter.ViewHolder, LiveUIViewModel> implements ImageloaderUser, ProgramConstants {

    ImageRequest.RequestManager requestManager;

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public LiveUIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new LiveUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_recommend_live_layout, parent, false));

    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivImg);
        setRequestManager(null);
    }

    @Override
    public void onBindViewHolder(final LiveUIAdapter.ViewHolder UIViewHolder, final LiveUIViewModel uiViewModel, int position) {
        if (uiViewModel != null) {
            UIViewHolder.tvTitle.setText(uiViewModel.getTitle());
            UIViewHolder.tvSubtitle.setText(uiViewModel.getSubtitle());
            if(LIVE_STATE_BEING == uiViewModel.getStatus()){
                UIViewHolder.tvStatus.setVisibility(View.VISIBLE);
                UIViewHolder.tvStatus.setText("直播中");
                if(uiViewModel.isSoccer()) {
                    UIViewHolder.tvStatus.setBackgroundResource(R.drawable.bg_show_bar_on);
                }else{
                    UIViewHolder.tvStatus.setBackgroundResource(R.drawable.bg_rectangle_conner_green_alpha);
                }
            }else if(LIVE_STATE_BEFORE == uiViewModel.getStatus()){
                if(uiViewModel.isSoccer()) {
                    UIViewHolder.tvStatus.setBackgroundResource(R.drawable.bg_show_bar_off);
                    UIViewHolder.tvStatus.setText("未开始");
                }else{
                    UIViewHolder.tvStatus.setVisibility(View.GONE);
                }
            }else if(LIVE_STATE_AFTER == uiViewModel.getStatus()){
                if(uiViewModel.isSoccer()) {
                    UIViewHolder.tvStatus.setBackgroundResource(R.drawable.bg_show_bar_off);
                    UIViewHolder.tvStatus.setText("已完赛");
                }else{
                    UIViewHolder.tvStatus.setVisibility(View.GONE);
                }
            }else{
                UIViewHolder.tvStatus.setVisibility(View.GONE);
            }
            if(uiViewModel.getStatus()== LIVE_STATE_BEFORE){
                UIViewHolder.tvReserve.setVisibility(View.VISIBLE);
            }else{
                UIViewHolder.tvReserve.setVisibility(View.GONE);
            }
            if(uiViewModel.isSoccer()){
                UIViewHolder.ivSoccer.setVisibility(View.VISIBLE);
            }else{
                UIViewHolder.ivSoccer.setVisibility(View.GONE);
            }
            requestManager.load(uiViewModel.getImgUrl()).medium().centerCrop().into(UIViewHolder.ivImg);
        }
    }


    public static class ViewHolder extends ClickableSimpleViewHolder {

        @BindView(R2.id.iv_soccer)
        ImageView ivSoccer;
        @BindView(R2.id.iv_image)
        ImageView ivImg;
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.tv_subtitle)
        TextView tvSubtitle;
        @BindView(R2.id.tv_status)
        TextView tvStatus;
        @BindView(R2.id.tv_reserve)
        TextView tvReserve;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
