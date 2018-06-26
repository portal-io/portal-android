package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.LiveTopicMoreUIViewModel;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveTopicMoreUIAdapter extends BaseUIAdapter<LiveTopicMoreUIAdapter.ViewHolder, LiveTopicMoreUIViewModel> {

    @Override
    public LiveTopicMoreUIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new LiveTopicMoreUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_live_recommend_topic_item_more_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(LiveTopicMoreUIAdapter.ViewHolder UIViewHolder, LiveTopicMoreUIViewModel uiViewModel, int position) {

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
        TextView tvMore;

        public ViewHolder(View view) {
            super(view);
            this.tvMore = (TextView) view;
        }
    }

}
