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
import com.whaley.biz.program.uiview.viewmodel.PromulagtorContentUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/17
 */

public class PromulagtorContentUIAdapter extends BaseUIAdapter<PromulagtorContentUIAdapter.ProgramViewHolder, PromulagtorContentUIViewModel> implements ImageloaderUser {
    ImageRequest.RequestManager requestManager;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }


    @Override
    public ProgramViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new ProgramViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_promulagtor_content_layout, parent, false));

    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivImg);
        setRequestManager(null);
    }

    @Override
    public void onBindViewHolder(ProgramViewHolder UIViewHolder, PromulagtorContentUIViewModel uiViewModel, int position) {
        if (uiViewModel != null) {
            UIViewHolder.tvDes.setText(uiViewModel.getName());
            UIViewHolder.tvContent.setText(uiViewModel.getPalyNum());
            requestManager.load(uiViewModel.getImgUrl()).medium().fitCenter().into(UIViewHolder.ivImg);
        }
    }

    @Override
    protected void onViewClick() {
        super.onViewClick();
    }

    @Override
    protected void setViewClick() {
        super.setViewClick();
    }

    static class ProgramViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_img)
        ImageView ivImg;
        @BindView(R2.id.tv_des)
        TextView tvDes;
        @BindView(R2.id.tv_content)
        TextView tvContent;


        public ProgramViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
