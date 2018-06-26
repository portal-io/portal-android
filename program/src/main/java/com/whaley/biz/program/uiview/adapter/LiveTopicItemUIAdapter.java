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
import com.whaley.biz.program.uiview.viewmodel.LiveTopicItemUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveTopicItemUIAdapter extends BaseUIAdapter<LiveTopicItemUIAdapter.ViewHolder, LiveTopicItemUIViewModel> implements ImageloaderUser {

    ImageRequest.RequestManager requestManager;

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public LiveTopicItemUIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new LiveTopicItemUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_live_recommend_topic_item_layout, parent, false));
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivImage);
        setRequestManager(null);
    }

    @Override
    public void onBindViewHolder(LiveTopicItemUIAdapter.ViewHolder UIViewHolder, LiveTopicItemUIViewModel uiViewModel, int position) {
        if (uiViewModel != null) {
            requestManager.load(uiViewModel.getImage()).centerCrop().into(UIViewHolder.ivImage);
            UIViewHolder.tvTitle.setText(uiViewModel.getTitle());
            UIViewHolder.tvSubtitle.setText(uiViewModel.getSubTitle());
        }
    }

    @Override
    protected void onViewClick() {
        super.onViewClick();
    }

    @Override
    protected void setViewClick() {
        super.setViewClick();
    }

    static class ViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_image)
        ImageView ivImage;
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.tv_subtitle)
        TextView tvSubtitle;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
