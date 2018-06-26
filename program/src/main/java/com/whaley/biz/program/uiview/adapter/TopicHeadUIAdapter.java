package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.core.utils.StrUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date:2017/8/21
 * Introduction:
 */

public class TopicHeadUIAdapter extends BaseUIAdapter<TopicHeadUIAdapter.ViewHolder, TopicHeadViewModel> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(getLayoutInfalterFormParent(viewGroup).inflate(R.layout.item_topic_head, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, TopicHeadViewModel data, int position) {
        viewHolder.tvName.setText(data.getName());
        viewHolder.tvNumVideo.setText(data.getNumVideo());
        if (StrUtil.isEmpty(data.getIntroduction())) {
            viewHolder.tvSummary.setVisibility(View.GONE);
        } else {
            viewHolder.tvSummary.setVisibility(View.VISIBLE);
            viewHolder.tvSummary.setText(data.getIntroduction());
        }

        if (data.isPay()) {
            viewHolder.tvHaveVoucher.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvHaveVoucher.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder extends ClickableSimpleViewHolder {

        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_numVideo)
        TextView tvNumVideo;
        @BindView(R2.id.tv_have_voucher)
        TextView tvHaveVoucher;
        @BindView(R2.id.view_num)
        LinearLayout viewNum;
        @BindView(R2.id.tv_summary)
        TextView tvSummary;

        public ViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }

}
