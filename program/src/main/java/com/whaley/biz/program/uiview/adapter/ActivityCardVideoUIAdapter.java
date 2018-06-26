package com.whaley.biz.program.uiview.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.viewmodel.CardVideoUIViewModel;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date:2018/1/24
 * Introduction:
 */

public class ActivityCardVideoUIAdapter extends BaseUIAdapter<ActivityCardVideoUIAdapter.ViewHolder, CardVideoUIViewModel> implements ImageloaderUser {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new ActivityCardVideoUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_activity_card_video, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder UIViewHolder, CardVideoUIViewModel uiViewModel, int position) {
        UIViewHolder.tvName.setText(uiViewModel.getName());
        UIViewHolder.tvContent.setText(uiViewModel.getSubtitle());
        requestManager.load(uiViewModel.getImgUrl()).medium().placeholder(R.mipmap.default_4).fitCenter().into(UIViewHolder.ivImg);
    }

    ImageRequest.RequestManager requestManager;

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }
    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivImg);
        setRequestManager(null);
    }
    public class ViewHolder extends ClickableSimpleViewHolder {

        @BindView(R2.id.iv_pic)
        ImageView ivImg;
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_content)
        TextView tvContent;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
