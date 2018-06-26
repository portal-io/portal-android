package com.whaley.biz.program.uiview.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.viewmodel.CardTopicUIViewModel;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

public class CardTopicUIAdapter extends BaseUIAdapter<CardTopicUIAdapter.ViewHolder, CardTopicUIViewModel> implements ImageloaderUser {

    ImageRequest.RequestManager requestManager;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(getLayoutInfalterFormParent(viewGroup).inflate(R.layout.item_card_topic, viewGroup, false));
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivPic);
        setRequestManager(null);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, CardTopicUIViewModel data, int position) {
        viewHolder.tvName.setText(data.getName());
        viewHolder.tvNum.setText(data.getPlayCount());
        viewHolder.tvContext.setText(data.getIntroduction());
        requestManager.load(data.getImgUrl()).medium().fitCenter().into(viewHolder.ivPic);
    }

    public class ViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_pic)
        ImageView ivPic;
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_num)
        TextView tvNum;
        @BindView(R2.id.tv_context)
        TextView tvContext;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
