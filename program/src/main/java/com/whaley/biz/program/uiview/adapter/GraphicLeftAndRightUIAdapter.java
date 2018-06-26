package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.GraphicLeftAndRightUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/22
 */

public class GraphicLeftAndRightUIAdapter extends BaseUIAdapter<GraphicLeftAndRightUIAdapter.GraphicLeftAndRightViewHolder, GraphicLeftAndRightUIViewModel> implements ImageloaderUser {


    ImageRequest.RequestManager requestManager;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivPic);
        setRequestManager(null);
    }

    @Override
    public GraphicLeftAndRightViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new GraphicLeftAndRightViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_graphic_left_and_right, parent, false));

    }

    @Override
    public void onBindViewHolder(GraphicLeftAndRightViewHolder viewHolder, GraphicLeftAndRightUIViewModel uiViewModel, int position) {
        requestManager.load(uiViewModel.getImgUrl()).small().centerCrop().into(viewHolder.ivPic);
        viewHolder.tvName.setText(uiViewModel.getName());
        viewHolder.tvPlayCount.setText(uiViewModel.getPlayNum());
    }

    public class GraphicLeftAndRightViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_pic)
        ImageView ivPic;
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_play_count)
        TextView tvPlayCount;


        public GraphicLeftAndRightViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
