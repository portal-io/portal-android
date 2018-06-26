package com.whaley.biz.pay.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.R;
import com.whaley.biz.pay.model.CouponModel;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/21.
 */

public class ThirdPayAdapter extends RecyclerViewAdapter<CouponModel, ViewHolder> {

    private boolean isOnlyOne;
    private boolean isUnity;
    private boolean isHaveWhaleyPay;

    private PayPackgeClickListener payPackgeClickListener;

    public void setPayPackgeClickListener(PayPackgeClickListener payPackgeClickListener) {
        this.payPackgeClickListener = payPackgeClickListener;
    }

    public void setHaveWhaleyPay(boolean haveWhaleyPay) {
        isHaveWhaleyPay = haveWhaleyPay;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pay_tips, parent, false));
    }

    public void setOnlyOne(boolean isOnlyOne) {
        this.isOnlyOne = isOnlyOne;
    }

    public boolean isOnlyOne() {
        return isOnlyOne;
    }

    public void setUnity(boolean unity) {
        isUnity = unity;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final CouponModel data, int position) {
        TextView tvPrice = holder.getView(R.id.tv_price);
        TextView tvName = holder.getView(R.id.tv_name);
        ImageView btnPay = holder.getView(R.id.btn_pay);
        TextView tvWhaleyPrice = holder.getView(R.id.tv_whaley_price);
        TextView tvPriceTips = holder.getView(R.id.tv_price_tips);
        if (isHaveWhaleyPay) {
            tvWhaleyPrice.setText(new StringBuilder().append(data.getJingbiPrice()).append("鲸币"));
            tvWhaleyPrice.setVisibility(View.VISIBLE);
        } else {
            tvWhaleyPrice.setVisibility(View.GONE);
        }
        RelativeLayout relativeLayout = holder.getView(R.id.view_pay);
        String displayName = data.viewName;
        if (displayName.endsWith("观看券")) {
            displayName = displayName.substring(0, displayName.length() - 3);
            if (displayName.length() > 20) {
                displayName = displayName.substring(0, 20) + "... ";
            }
            displayName = displayName + " 观看券";
        }
        tvName.setText(displayName);
        tvPrice.setText(new StringBuilder().append("¥").append(PayUtil.fromFenToYuan(data.getPrice())));
        TextView tvTopic = holder.getView(R.id.tv_topic);
        if (!data.isTopic && "content_packge".equals(data.getRelatedType()) && !isUnity) {
            tvTopic.setVisibility(View.VISIBLE);
        } else {
            tvTopic.setVisibility(View.GONE);
        }
        tvTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payPackgeClickListener != null) {
                    payPackgeClickListener.onPackgeClick(data);
                }
            }
        });
        if (isOnlyOne) {
            btnPay.setVisibility(View.GONE);
            relativeLayout.setSelected(false);
            tvPriceTips.setVisibility(View.VISIBLE);
            switch (data.showPayType) {
                case CouponModel.NOT_PAY:
                    tvPriceTips.setText("待支付");
                    tvPriceTips.setSelected(true);
                    break;
                case CouponModel.PAY_FAIL:
                    tvPriceTips.setText("支付失败");
                    tvPriceTips.setSelected(true);
                    break;
                case CouponModel.PAY_SUCCESS:
                    tvPriceTips.setText("支付成功");
                    tvPriceTips.setSelected(false);
                    break;
            }
            return;
        }
        tvPriceTips.setVisibility(View.GONE);
        btnPay.setVisibility(View.VISIBLE);
        if (data.isSelect) {
            relativeLayout.setSelected(true);
            btnPay.setSelected(true);
        } else {
            relativeLayout.setSelected(false);
            btnPay.setSelected(false);
        }
    }


    public interface PayPackgeClickListener {
        void onPackgeClick(CouponModel data);
    }

}
