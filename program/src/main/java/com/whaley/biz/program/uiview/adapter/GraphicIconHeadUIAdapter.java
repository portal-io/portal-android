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
import com.whaley.biz.program.uiview.viewmodel.GraphicIconHeadUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/22
 */

public class GraphicIconHeadUIAdapter extends BaseUIAdapter<GraphicIconHeadUIAdapter.GraphicIconHeadViewHolder, GraphicIconHeadUIViewModel> implements ImageloaderUser {
    ImageRequest.RequestManager requestManager;
    static final int SIZE = DisplayUtil.convertDIP2PX(40);

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public GraphicIconHeadViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new GraphicIconHeadViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_graphic_icon_head, parent, false));
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivPic);
        setRequestManager(null);
    }

    @Override
    public void onBindViewHolder(GraphicIconHeadViewHolder viewHolder, GraphicIconHeadUIViewModel uiViewModel, int position) {
        viewHolder.tvName.setText(uiViewModel.getName());
        viewHolder.tvTime.setText(uiViewModel.getTime());
        requestManager.load(uiViewModel.getImgUrl()).size(SIZE, SIZE).circle().centerCrop().into(viewHolder.ivPic);
    }

    public class GraphicIconHeadViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_pic)
        ImageView ivPic;
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_time)
        TextView tvTime;


        public GraphicIconHeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
