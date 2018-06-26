package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.LineViewUIViewModel;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/17
 */

public class LineUIAdapter extends BaseUIAdapter<LineUIAdapter.LineViewHolder, LineViewUIViewModel> implements ImageloaderUser {

    ImageRequest.RequestManager requestManager;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }


    @Override
    public LineViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new LineViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_line_layout, parent, false));

    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        setRequestManager(null);
    }

    @Override
    public boolean onInitViewHolder(LineViewHolder viewHolder, LineViewUIViewModel uiViewModel, int position) {
        ViewGroup.LayoutParams layoutParams = viewHolder.viewLine.getLayoutParams();
        if (layoutParams == null) {
            Log.e("layoutParams=null");
        }
        if (uiViewModel == null) {
            Log.e("uiViewModel=null");
        }
        layoutParams.height = uiViewModel.getLineHeight();
        viewHolder.viewLine.setLayoutParams(layoutParams);
        return true;
    }

    @Override
    public void onBindViewHolder(LineViewHolder viewHolder, LineViewUIViewModel uiViewModel, int position) {
        ViewGroup.LayoutParams layoutParams = viewHolder.viewLine.getLayoutParams();
        if (layoutParams.height != uiViewModel.getLineHeight()) {
            layoutParams.height = uiViewModel.getLineHeight();
            viewHolder.viewLine.requestLayout();
        }
    }


    public static class LineViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.view_line)
        View viewLine;

        public LineViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
