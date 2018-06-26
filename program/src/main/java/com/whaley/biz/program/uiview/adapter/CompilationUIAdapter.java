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
import com.whaley.biz.program.uiview.viewmodel.CompilationUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/8/15.
 */

public class CompilationUIAdapter extends BaseUIAdapter<CompilationUIAdapter.ViewHolder, CompilationUIViewModel> implements ImageloaderUser {

    ImageRequest.RequestManager requestManager;

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public CompilationUIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new CompilationUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_compilation, parent, false));

    }

    @Override
    public void onDettatch() {
        super.onDettatch();
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivImg);
        setRequestManager(null);
    }

    @Override
    public void onBindViewHolder(final CompilationUIAdapter.ViewHolder UIViewHolder, final CompilationUIViewModel uiViewModel, int position) {
        if (uiViewModel != null) {
            UIViewHolder.tvName.setText(uiViewModel.getName());
            UIViewHolder.tvNum.setText(uiViewModel.getSubtitle());
            requestManager.load(uiViewModel.getImgUrl()).medium().placeholder(R.mipmap.default_4).fitCenter().into(UIViewHolder.ivImg);
        }
    }

    public static class ViewHolder extends ClickableSimpleViewHolder {

        @BindView(R2.id.iv_pic)
        ImageView ivImg;
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_num)
        TextView tvNum;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
