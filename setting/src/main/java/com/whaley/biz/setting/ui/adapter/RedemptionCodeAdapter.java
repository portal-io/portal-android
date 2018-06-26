package com.whaley.biz.setting.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.setting.constant.SettingConstants;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.model.RedeemCodeModel;
import com.whaley.biz.setting.ui.viewmodel.RedemptionCodeViewModel;
import com.whaley.core.utils.DateUtils;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/3.
 */

public class RedemptionCodeAdapter extends RecyclerViewAdapter<RedemptionCodeViewModel, ViewHolder> {

    private ConvertOnClickListener convertonClickListener;

    public void setConvertOnClickListener(ConvertOnClickListener convertonClickListener) {
        this.convertonClickListener = convertonClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return getData().get(position).type;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        if (RedemptionCodeViewModel.TYPE_BOX == viewType) {
            layoutId = R.layout.item_redemption_box;
        } else if (RedemptionCodeViewModel.TYPE_CODE == viewType) {
            layoutId = R.layout.item_redemption_code;
        } else if (RedemptionCodeViewModel.TYPE_NAME == viewType) {
            layoutId = R.layout.item_text_redemption_name;
        } else {
            layoutId = R.layout.item_redemption_voucher;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final RedemptionCodeViewModel data, int position) {
        if (RedemptionCodeViewModel.TYPE_BOX == data.type) {
            RelativeLayout relativeLayout = holder.getView(R.id.rl_redemption_box);
            if (data.isHaveCode) {
                relativeLayout.setSelected(true);
            } else {
                relativeLayout.setSelected(false);
            }
        } else if (RedemptionCodeViewModel.TYPE_CODE == data.type) {
            final RedeemCodeModel redeemCodeModel = data.redeemCodeModel;
            RelativeLayout relativeLayout = holder.getView(R.id.rl_redemption_code);
            TextView tv_name = holder.getView(R.id.tv_name);
            TextView tv_redemption = holder.getView(R.id.tv_redemption);
            TextView tv_association = holder.getView(R.id.tv_association);
            tv_association.setText(data.association);
            tv_name.setText(redeemCodeModel.getRedeemCode());
            if (data.isLast) {
                relativeLayout.setSelected(true);
            } else {
                relativeLayout.setSelected(false);
            }
            tv_redemption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (convertonClickListener == null) {
                        return;
                    }
                    convertonClickListener.onConvertClick(redeemCodeModel.getRedeemCode());
                }
            });
        } else if (RedemptionCodeViewModel.TYPE_NAME == data.type) {
            //
        } else if (RedemptionCodeViewModel.TYPE_VOUCHER == data.type) {
            TextView tv_name = holder.getView(R.id.tv_name);
            TextView tv_price = holder.getView(R.id.tv_price);
            TextView tv_look = holder.getView(R.id.tv_look);
            TextView tv_content = holder.getView(R.id.tv_content);
            TextView tv_code = holder.getView(R.id.tv_code);
            tv_name.setText(data.redemptionCodeModel.getDisplayName());
            tv_price.setText(new StringBuilder().append("¥")
                    .append(SettingUtil.fromFenToYuan(data.redemptionCodeModel.getPrice())));
            tv_content.setText(new StringBuilder().append("购买于")
                    .append(DateUtils.foramteToDate(data.redemptionCodeModel.getCreateTime()
                            , DateUtils.YYYYMMDD_NYR)));
            if (SettingConstants.TYPE_REDEEM_CODE.equals(data.redemptionCodeModel.getCouponSource())) {
                tv_code.setText(data.redemptionCodeModel.getCouponSourceCode().toUpperCase());
                tv_code.setVisibility(View.VISIBLE);
            } else {
                tv_code.setVisibility(View.GONE);
            }
            if (SettingConstants.TYPE_LIVE.equals(data.redemptionCodeModel.getRelatedType())
                    && SettingConstants.LIVE_STATE_BEFORE == data.redemptionCodeModel.getLiveStatus()) {
                tv_look.setText(R.string.tv_redemption_no_player);
                tv_look.setSelected(true);
            } else {
                tv_look.setText(R.string.tv_redemption_look);
                tv_look.setSelected(false);
            }
        }
    }

    public interface ConvertOnClickListener {
        void onConvertClick(String code);
    }

}
