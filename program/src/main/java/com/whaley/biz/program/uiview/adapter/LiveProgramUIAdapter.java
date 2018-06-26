package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.LiveConstants;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.viewmodel.LiveProgramUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveProgramUIAdapter extends BaseUIAdapter<LiveProgramUIAdapter.ViewHolder, LiveProgramUIViewModel> implements ImageloaderUser, LiveConstants {

    ImageRequest.RequestManager requestManager;

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public LiveProgramUIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new LiveProgramUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_recommend_program_layout, parent, false));
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivImg);
        setRequestManager(null);
    }

    @Override
    public void onBindViewHolder(final LiveProgramUIAdapter.ViewHolder UIViewHolder, final LiveProgramUIViewModel uiViewModel, int position) {
        if (uiViewModel != null) {
            UIViewHolder.tvTitle.setText(uiViewModel.getTitle());
            UIViewHolder.tvSubtitle.setText(uiViewModel.getSubtitle());
            requestManager.load(uiViewModel.getImgUrl()).medium().centerCrop().into(UIViewHolder.ivImg);
            if(uiViewModel.isSoccer()){
                UIViewHolder.ivSoccer.setVisibility(View.VISIBLE);
            }else{
                UIViewHolder.ivSoccer.setVisibility(View.GONE);
            }
        }
    }


    public class ViewHolder extends ClickableSimpleViewHolder {

        @BindView(R2.id.iv_soccer)
        ImageView ivSoccer;
        @BindView(R2.id.iv_image)
        ImageView ivImg;
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.tv_subtitle)
        TextView tvSubtitle;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

