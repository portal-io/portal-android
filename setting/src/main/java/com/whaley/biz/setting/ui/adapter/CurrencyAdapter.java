package com.whaley.biz.setting.ui.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.whaley.biz.setting.R;
import com.whaley.biz.setting.ui.viewmodel.CurrencyViewModel;
import com.whaley.biz.setting.ui.viewmodel.RedemptionCodeViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/10/12.
 */

public class CurrencyAdapter extends RecyclerViewAdapter<CurrencyViewModel, ViewHolder> {

    ImageRequest.RequestManager requestManager;

    public CurrencyAdapter(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public int getItemViewType(int position) {
        return getData().get(position).getType();
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutId;
        if (1 == viewType) {
            layoutId = R.layout.item_currency_bottom;
        } else {
            layoutId = R.layout.item_currency;
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, CurrencyViewModel currencyViewModel, final int position) {
        if(currencyViewModel.getType() == 1){
            TextView tvIntro = holder.getView(R.id.tv_intro);
            tvIntro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onBtnClickListener!=null){
                        onBtnClickListener.onBtnClick(position);
                    }
                }
            });
        }else {
            TextView tvName = holder.getView(R.id.tv_name);
            TextView tvPreferName = holder.getView(R.id.tv_preferName);
            TextView tvPrefer = holder.getView(R.id.tv_prefer);
            TextView tvContent = holder.getView(R.id.tv_content);
            TextView tvPreferContent = holder.getView(R.id.tv_preferContent);
            TextView tvRecharge = holder.getView(R.id.tv_recharge);
            tvName.setText(currencyViewModel.getName());
            tvContent.setText(currencyViewModel.getContent());
            tvRecharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBtnClickListener != null) {
                        onBtnClickListener.onBtnClick(position);
                    }
                }
            });
            if (TextUtils.isEmpty(currencyViewModel.getPreferName())) {
                tvPreferName.setVisibility(View.GONE);
            } else {
                tvPreferName.setText(currencyViewModel.getPreferName());
                tvPreferName.setVisibility(View.VISIBLE);
            }
            if (currencyViewModel.isPrefer()) {
                tvPrefer.setVisibility(View.VISIBLE);
                tvPreferContent.setText(currencyViewModel.getPreferContent());
                tvPreferContent.setVisibility(View.VISIBLE);
                tvContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            } else {
                tvPrefer.setVisibility(View.GONE);
                tvPreferContent.setText("");
                tvPreferContent.setVisibility(View.VISIBLE);
                tvContent.getPaint().setFlags(0);
            }
        }
    }

    private OnBtnClickListener onBtnClickListener;

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }

    public interface OnBtnClickListener {
        void onBtnClick(int position);
    }

}
