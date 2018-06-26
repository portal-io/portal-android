package com.whaley.biz.program.ui.live.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.LiveConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.ui.uimodel.ReserveViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/21.
 */

public class ReserveAdapter extends RecyclerViewAdapter<ReserveViewModel, ViewHolder> implements ProgramConstants{

    ImageRequest.RequestManager requestManager;

    public ReserveAdapter(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_reserve, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder collectViewHolder, ReserveViewModel reserveViewModel, int position) {
        bindViewHolder(reserveViewModel, collectViewHolder, position);
    }

    private void bindViewHolder(ReserveViewModel reserveViewModel, ViewHolder holder, int position) {
        ImageView iv_pic = holder.getView(R.id.iv_pic);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_intro = holder.getView(R.id.tv_intro);
        TextView tv_buy = holder.getView(R.id.tv_buy);
        tv_name.setText(reserveViewModel.getName());
        if(reserveViewModel.isChargeable() && reserveViewModel.isBuy()){
            tv_buy.setVisibility(View.VISIBLE);
        }else{
            tv_buy.setVisibility(View.GONE);
        }
        requestManager.load(reserveViewModel.getPic()).medium().into(iv_pic);
        if (LIVE_STATE_BEFORE == reserveViewModel.getLiveStatus()) {
            tv_name.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color3));
            tv_intro.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color15_alpha_70));
            tv_intro.getPaint().setFakeBoldText(true);
            iv_pic.setAlpha(1.0f);
        } else if (LIVE_STATE_BEING == reserveViewModel.getLiveStatus()){
            tv_name.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color3));
            tv_intro.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color15_alpha_70));
            tv_intro.getPaint().setFakeBoldText(true);
            iv_pic.setAlpha(1.0f);
        }else{
            tv_name.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color7));
            tv_intro.setTextColor(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.color7));
            tv_intro.getPaint().setFakeBoldText(false);
            iv_pic.setAlpha(0.4f);
            tv_buy.setVisibility(View.GONE);
        }
        tv_intro.setText(reserveViewModel.getIntro());
    }

}
