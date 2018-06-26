package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.viewmodel.IconRoundUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import com.whaley.core.widget.uiview.OnUIViewClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/22
 */

public class IconRoundUIAdapter extends BaseUIAdapter<IconRoundUIAdapter.ChannelRoundViewHolder, IconRoundUIViewModel> implements ImageloaderUser {
    ImageRequest.RequestManager requestManager;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ChannelRoundViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new ChannelRoundViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_channel_icon_round_layout, parent, false));
    }

    @Override
    public void setOnUIViewClickListener(OnUIViewClickListener onViewClickListener) {
        super.setOnUIViewClickListener(onViewClickListener);
    }

    @Override
    protected void setViewClick() {
        super.setViewClick();
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivIcon);
        setRequestManager(null);
    }

    @Override
    public void onBindViewHolder(ChannelRoundViewHolder viewHolder, IconRoundUIViewModel uiViewModel, int position) {
        viewHolder.tvName.setText(uiViewModel.getName());
        requestManager.load(uiViewModel.getImgUrl()).circle().into(viewHolder.ivIcon);
    }

    public class ChannelRoundViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_icon)
        ImageView ivIcon;
        @BindView(R2.id.tv_name)
        TextView tvName;


        public ChannelRoundViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
