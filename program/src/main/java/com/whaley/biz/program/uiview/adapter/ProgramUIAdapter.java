package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.viewmodel.ProgramUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/17
 */

public class ProgramUIAdapter extends BaseUIAdapter<ProgramUIAdapter.ProgramViewHolder, ProgramUIViewModel> implements ImageloaderUser {
    static final int DEFAULT_HEIGHT = DisplayUtil.convertDIP2PX(101);

    ImageRequest.RequestManager requestManager;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }


    @Override
    public ProgramViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new ProgramViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_program_layout, parent, false));

    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivImg);
        setRequestManager(null);
    }

    @Override
    public void onBindViewHolder(ProgramViewHolder UIViewHolder, ProgramUIViewModel uiViewModel, int position) {
        if (uiViewModel != null) {
            boolean isChanged = false;
            ViewGroup.LayoutParams layoutParams = UIViewHolder.ivImg.getLayoutParams();
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = DEFAULT_HEIGHT;
            if (uiViewModel.isCustomizeHeight() || uiViewModel.isCustomizeWidth()) {
                if (uiViewModel.isCustomizeHeight() ) {
                    height = uiViewModel.getImgHeight();
                }
                if (uiViewModel.isCustomizeWidth() ) {
                    width = uiViewModel.getImgWidth();
                }
            }
            if(layoutParams.height!=height){
                isChanged = true;
            }
            if(layoutParams.width != width){
                isChanged = true;
            }
            if(isChanged){
                layoutParams.width = width;
                layoutParams.height = height;
                UIViewHolder.ivImg.requestLayout();
            }
            UIViewHolder.tvDes.setText(uiViewModel.getName());
            UIViewHolder.tvContent.setText(uiViewModel.getSubtitle());
            requestManager.load(uiViewModel.getImgUrl()).medium().fitCenter().into(UIViewHolder.ivImg);
//            if (!TextUtils.isEmpty(uiViewModel.getFlagUrl())) {
//                int[] size = Util.getPicSize(data.getFlagUrl());
//                if (size != null) {
//                    iv_tag.setVisibility(View.VISIBLE);
//                    requestManager.load(data.getFlagUrl()).size(size[0], size[1]).placeholder(R.mipmap.default_6).fitCenter().into(iv_tag);
//                } else {
//                    iv_tag.setVisibility(View.GONE);
//                }
//            } else {
//                iv_tag.setVisibility(View.GONE);
//            }
            if(uiViewModel.getCornerResourceId()!=0){
                UIViewHolder.ivTag.setVisibility(View.VISIBLE);
                UIViewHolder.ivTag.setImageResource(uiViewModel.getCornerResourceId());
            }else{
                UIViewHolder.ivTag.setVisibility(View.GONE);
            }
        }
    }


    public class ProgramViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_img)
        ImageView ivImg;
        @BindView(R2.id.view_bg)
        View viewBg;
        @BindView(R2.id.tv_count)
        TextView tvCount;
        @BindView(R2.id.layout_img)
        RelativeLayout layoutImg;
        @BindView(R2.id.iv_tag)
        ImageView ivTag;
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
