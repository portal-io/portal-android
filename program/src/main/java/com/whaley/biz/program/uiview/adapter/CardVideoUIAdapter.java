package com.whaley.biz.program.uiview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.OnClickableUIViewClickListener;
import com.whaley.biz.program.uiview.viewmodel.CardVideoUIViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2017/8/15.
 */

public class CardVideoUIAdapter extends BaseUIAdapter<CardVideoUIAdapter.ViewHolder, CardVideoUIViewModel> implements ImageloaderUser {

    ImageRequest.RequestManager requestManager;

    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    CardVideoUIAdapter.OnReserveClickListener onReserveClickListener;

    @Override
    public void setOnUIViewClickListener(OnClickableUIViewClickListener onUIViewClickListener) {
        super.setOnUIViewClickListener(onUIViewClickListener);
        if (onUIViewClickListener != null && onUIViewClickListener instanceof CardVideoUIAdapter.OnReserveClickListener) {
            onReserveClickListener = (CardVideoUIAdapter.OnReserveClickListener) onUIViewClickListener;
        }
    }

    @Override
    public CardVideoUIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new CardVideoUIAdapter.ViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_live_reserve, parent, false));

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
        onReserveClickListener = null;
    }

    @Override
    public void onBindViewHolder(final CardVideoUIAdapter.ViewHolder UIViewHolder, final CardVideoUIViewModel uiViewModel, int position) {
        if (uiViewModel != null) {
            if (uiViewModel.isHaveButton()) {
                UIViewHolder.tvAdd.setVisibility(View.VISIBLE);
                if (uiViewModel.isReserve()) {
                    UIViewHolder.tvAdd.setSelected(true);
                    UIViewHolder.tvAdd.setText("已预约");
                } else {
                    UIViewHolder.tvAdd.setSelected(false);
                    UIViewHolder.tvAdd.setText("立即预约");
                }
                UIViewHolder.tvAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((OnReserveClickListener) getOnUIViewClickListener()) != null) {
                            if (uiViewModel.isReserve()) {
                                ((OnReserveClickListener) getOnUIViewClickListener()).cancel(UIViewHolder);
                            } else {
                                ((OnReserveClickListener) getOnUIViewClickListener()).add(UIViewHolder);
                            }
                        }
                    }
                });
            } else {
                UIViewHolder.tvAdd.setVisibility(View.GONE);
            }
            if (uiViewModel.isPay()) {
                UIViewHolder.tvHaveVoucher.setVisibility(View.VISIBLE);
            } else {
                UIViewHolder.tvHaveVoucher.setVisibility(View.GONE);
            }
            if(uiViewModel.getCornerResureId()==0){
                UIViewHolder.ivCorner.setVisibility(View.GONE);
            }else{
                UIViewHolder.ivCorner.setVisibility(View.VISIBLE);
                UIViewHolder.ivCorner.setImageResource(uiViewModel.getCornerResureId());
            }
            UIViewHolder.tvName.setText(uiViewModel.getName());
            UIViewHolder.tvContent.setText(uiViewModel.getSubtitle());
            requestManager.load(uiViewModel.getImgUrl()).medium().placeholder(R.mipmap.default_4).fitCenter().into(UIViewHolder.ivImg);
        }
    }


    public class ViewHolder extends ClickableSimpleViewHolder {

        @BindView(R2.id.iv_pic)
        ImageView ivImg;
        @BindView(R2.id.tv_name)
        TextView tvName;
        @BindView(R2.id.tv_add)
        public TextView tvAdd;
        @BindView(R2.id.tv_content)
        TextView tvContent;
        @BindView(R2.id.tv_have_voucher)
        TextView tvHaveVoucher;
        @BindView(R2.id.iv_corner)
        ImageView ivCorner;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnReserveClickListener extends OnClickableUIViewClickListener {

        void add(ClickableUIViewHolder viewHolder);

        void cancel(ClickableUIViewHolder viewHolder);

    }

}
