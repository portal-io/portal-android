package com.whaley.biz.program.ui.live.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.model.LiveGuestModel;
import com.whaley.biz.program.widget.RoundedRatioImageView;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/17.
 */

public class TeamMemberAdapter extends RecyclerViewAdapter<LiveGuestModel, ViewHolder> {

    ImageRequest.RequestManager requestManager;

    public TeamMemberAdapter(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, LiveGuestModel data, int position) {
        RoundedRatioImageView ivImg = holder.getView(R.id.iv_img);
        TextView tvName = holder.getView(R.id.tv_name);
        tvName.setText(data.getGuestName());
        requestManager.load(data.getGuestPic()).centerCrop().small().into(ivImg);
    }

}
