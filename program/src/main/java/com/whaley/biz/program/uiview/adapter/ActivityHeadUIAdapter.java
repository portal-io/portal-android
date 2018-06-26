package com.whaley.biz.program.uiview.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.OnClickableUIViewClickListener;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.core.image.ImageRequest;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date:2018/1/24
 * Introduction:
 */

public class ActivityHeadUIAdapter extends BaseUIAdapter<ActivityHeadUIAdapter.ViewHolder, TopicHeadViewModel> implements ImageloaderUser {


    ImageRequest.RequestManager requestManager;
    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new ActivityHeadUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_activity_topic_head, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, TopicHeadViewModel topicHeadViewModel, int i) {
        requestManager.load(topicHeadViewModel.getBigImageUrl()).big().placeholder(R.mipmap.default_4).centerCrop().into(viewHolder.ivImg);
        viewHolder.tvIntroduction.setText(topicHeadViewModel.getIntroduction());
        viewHolder.tvMyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityHeadUIAdapter.OnMyCardClickListener) getOnUIViewClickListener()).onMyCardClick(viewHolder);
            }
        });
    }




    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivImg);
        setRequestManager(null);
    }

    public class ViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.tv_introduction)
        TextView tvIntroduction;
        @BindView(R2.id.tv_my_card)
        TextView tvMyCard;
        @BindView(R2.id.iv_image)
        ImageView ivImg;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnMyCardClickListener extends OnClickableUIViewClickListener {

        void onMyCardClick(ClickableUIViewHolder viewHolder);


    }

}
