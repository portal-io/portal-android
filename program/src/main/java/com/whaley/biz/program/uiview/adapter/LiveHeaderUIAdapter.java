package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.viewmodel.LiveHeaderUIViewModel;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveHeaderUIAdapter extends BaseUIAdapter<LiveHeaderUIAdapter.ViewHolder, LiveHeaderUIViewModel> implements ImageloaderUser {

    ImageRequest.RequestManager requestManager;

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public LiveHeaderUIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new LiveHeaderUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_live_recommend_header_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(final LiveHeaderUIAdapter.ViewHolder UIViewHolder, final LiveHeaderUIViewModel uiViewModel, int position) {
        if (uiViewModel != null) {
            UIViewHolder.tvTitle.setText(uiViewModel.getTitle());
        }
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        setRequestManager(null);
    }

    public static class ViewHolder extends ClickableSimpleViewHolder {

        @BindView(R2.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
