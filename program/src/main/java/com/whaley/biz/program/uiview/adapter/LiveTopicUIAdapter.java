package com.whaley.biz.program.uiview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.LiveTopicUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveTopicUIAdapter extends BaseUIAdapter<LiveTopicUIAdapter.ViewHolder, LiveTopicUIViewModel> implements ImageloaderUser {

    ImageRequest.RequestManager requestManager;

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public LiveTopicUIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new LiveTopicUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_live_recommend_topic_layout, parent, false));
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivImage);
        getViewHolder().onRecycled();
        setRequestManager(null);
    }

    @Override
    public void onBindViewHolder(final LiveTopicUIAdapter.ViewHolder UIViewHolder, LiveTopicUIViewModel uiViewModel, int position) {
        UIViewHolder.uiAdapter.setRealContext(getRealContext());
        if (uiViewModel != null) {
            if (uiViewModel.isShowItems()) {
                UIViewHolder.viewLine.setVisibility(View.GONE);
                UIViewHolder.viewTriangle.setVisibility(View.VISIBLE);
                UIViewHolder.rvList.setVisibility(View.VISIBLE);
                UIViewHolder.uiAdapter.setRequestManager(requestManager);
                UIViewHolder.uiAdapter.setOnUIViewClickListener(
                        getOnUIViewClickListener());
                UIViewHolder.uiAdapter.onBindView(UIViewHolder.recyclerHolder, uiViewModel.getRecyclerViewModel(), 0);
            } else {
                UIViewHolder.viewTriangle.setVisibility(View.GONE);
                UIViewHolder.rvList.setVisibility(View.GONE);
                UIViewHolder.viewLine.setVisibility(View.VISIBLE);
            }
            requestManager.load(uiViewModel.getImage()).big().centerCrop().into(UIViewHolder.ivImage);
            UIViewHolder.tvTitle.setText(uiViewModel.getTitle());
            UIViewHolder.tvSubtitle.setText(uiViewModel.getSubTitle());
        }
    }


    public static class ViewHolder extends ClickableSimpleViewHolder {

        @BindView(R2.id.iv_image)
        ImageView ivImage;
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.tv_subtitle)
        TextView tvSubtitle;
        @BindView(R2.id.rv_list)
        RecyclerView rvList;
        @BindView(R2.id.view_triangle)
        View viewTriangle;
        @BindView(R2.id.view_line)
        View viewLine;


        public RecyclerViewUIAdapter uiAdapter;
        RecyclerViewUIAdapter.RecyclerHolder recyclerHolder;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            uiAdapter = new RecyclerViewUIAdapter();
            rvList.setNestedScrollingEnabled(true);
            rvList.setHasFixedSize(true);
            recyclerHolder = new RecyclerViewUIAdapter.RecyclerHolder(rvList);
            uiAdapter.setRecyclerHolder(recyclerHolder);
            uiAdapter.onCreateView((ViewGroup) getRootView(), 0);
        }

        private void onRecycled() {
            uiAdapter.onBindView(recyclerHolder,null,0);
            uiAdapter.onRecycled();
//            uiAdapter.onRecycled();
        }

    }

}
