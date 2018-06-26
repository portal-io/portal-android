package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.AreaHeadViewUIViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/10
 */

public class AreaHeadUIAdapter extends BaseUIAdapter<AreaHeadUIAdapter.AreaHeadViewHolder, AreaHeadViewUIViewModel> {

    @Override
    public AreaHeadViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new AreaHeadViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_discoverytitle_layout, parent, false));
    }


    @Override
    public void onBindViewHolder(AreaHeadViewHolder UIViewHolder, AreaHeadViewUIViewModel uiViewModel, int position) {
        UIViewHolder.tvTitle.setText(uiViewModel.getTitleLeft());
        if (uiViewModel.isShowRight()) {
            UIViewHolder.ivArrow.setText(uiViewModel.getTitleRight());
            UIViewHolder.ivArrow.setVisibility(View.VISIBLE);
        } else {
            UIViewHolder.ivArrow.setVisibility(View.GONE);
        }
    }


    static class AreaHeadViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.iv_arrow)
        TextView ivArrow;

        AreaHeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
