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
import com.whaley.biz.program.uiview.viewmodel.ChannelIconViewUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YangZhi on 2017/3/15 11:43.
 */

public class ChannelIconUIAdapter extends BaseUIAdapter<ChannelIconUIAdapter.ChannelIconViewHolder, ChannelIconViewUIViewModel> implements ImageloaderUser {

    static final int SIZE = DisplayUtil.convertDIP2PX(28);
    static final int SIZE2 = DisplayUtil.convertDIP2PX(33);
    ImageRequest.RequestManager requestManager;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }


    @Override
    public void onDettatch() {
        super.onDettatch();
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivIcon);
        setRequestManager(null);
    }

    @Override
    public ChannelIconViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new ChannelIconViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_channel_icon_layout, parent, false));
    }


    @Override
    public void onBindViewHolder(ChannelIconViewHolder UIViewHolder, ChannelIconViewUIViewModel uiViewModel, int position) {
        UIViewHolder.tvName.setText(uiViewModel.getName());
        ViewGroup.LayoutParams layoutParams = UIViewHolder.ivIcon.getLayoutParams();
        if (uiViewModel.isActivity()) {
            if (layoutParams.width != SIZE2) {
                layoutParams.height = SIZE2;
                layoutParams.width = SIZE2;
            }
        } else {
            if (layoutParams.width != SIZE) {
                layoutParams.height = SIZE;
                layoutParams.width = SIZE;
            }
        }
        requestManager.load(uiViewModel.getImageUrl()).size(SIZE, SIZE).into(UIViewHolder.ivIcon);
    }

    public class ChannelIconViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_icon)
        ImageView ivIcon;
        @BindView(R2.id.tv_name)
        TextView tvName;


        public ChannelIconViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
