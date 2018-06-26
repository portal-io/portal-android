package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.HorziontalFullImgViewUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/16
 */

public class HorziontalFullImgUIAdapter extends BaseUIAdapter<HorziontalFullImgUIAdapter.HorziontalFullImgViewHolder, HorziontalFullImgViewUIViewModel> implements ImageloaderUser {

    ImageRequest.RequestManager requestManager;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivMoviceAd);
        setRequestManager(null);
    }


    @Override
    public HorziontalFullImgViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new HorziontalFullImgViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_horziontal_full_img, parent, false));
    }


    @Override
    public boolean onInitViewHolder(HorziontalFullImgViewHolder UIViewHolder, HorziontalFullImgViewUIViewModel uiViewModel, int position) {
        ViewGroup.LayoutParams layoutParams = UIViewHolder.ivMoviceAd.getLayoutParams();
        layoutParams.height = uiViewModel.getImgHeight();
        UIViewHolder.ivMoviceAd.setLayoutParams(layoutParams);
        return true;
    }

    @Override
    public void onBindViewHolder(HorziontalFullImgViewHolder UIViewHolder, HorziontalFullImgViewUIViewModel uiViewModel, int position) {
        requestManager.load(uiViewModel.getImageUrl()).centerCrop().big().into(UIViewHolder.ivMoviceAd);
    }

    public class HorziontalFullImgViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_movice_ad)
        ImageView ivMoviceAd;

        public HorziontalFullImgViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
