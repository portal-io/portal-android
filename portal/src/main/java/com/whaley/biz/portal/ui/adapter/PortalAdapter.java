package com.whaley.biz.portal.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.portal.R;
import com.whaley.biz.portal.ui.viewmodel.PortalViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2018/8/2.
 */

public class PortalAdapter extends RecyclerViewAdapter<PortalViewModel, ViewHolder> {

    private ImageRequest.RequestManager requestManager;

    public PortalAdapter(ImageRequest.RequestManager requestManager){
        this.requestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_portal, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder collectViewHolder, PortalViewModel portalViewModel, int position) {
        bindViewHolder(portalViewModel, collectViewHolder, position);
    }

    private void bindViewHolder(PortalViewModel portalViewModel, ViewHolder holder, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvValue = holder.getView(R.id.tv_value);
        ImageView ivIcon = holder.getView(R.id.iv_icon);
        tvName.setText(portalViewModel.getName());
        tvTime.setText(portalViewModel.getTime());
        tvValue.setText(portalViewModel.getValue());
        if(portalViewModel.getType()==1){
            ivIcon.setImageResource(R.mipmap.portal_watch);
        }else{
            ivIcon.setImageResource(R.mipmap.portal_receive);
        }
    }
}
