package com.whaley.biz.program.ui.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.ui.user.presenter.CollectPresenter;
import com.whaley.biz.program.ui.user.viewmodel.CollectViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/1.
 */

public class CollectAdapter extends RecyclerViewAdapter<CollectViewModel, ViewHolder> {

    ImageRequest.RequestManager requestManager;
    boolean isCheck;

    public CollectAdapter(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collect, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder collectViewHolder, CollectViewModel collectViewModel, int position) {
        bindViewHolder(collectViewModel, collectViewHolder, position);
    }

    private void bindViewHolder(CollectViewModel collectViewModel, ViewHolder holder, int position) {
        ImageView pic = holder.getView(R.id.pic);
        TextView name = holder.getView(R.id.name);
        TextView duration = holder.getView(R.id.duration);
        RelativeLayout rl_check = holder.getView(R.id.rl_check);
        ImageView ivCheck = holder.getView(R.id.iv_check);
        ImageView tag = holder.getView(R.id.tag);
        name.setText(collectViewModel.getName());
        duration.setText(collectViewModel.getDuration());
        if (ProgramConstants.TYPE_DYNAMIC.equals(collectViewModel.getProgramType())) {
            tag.setVisibility(View.VISIBLE);
        } else {
            tag.setVisibility(View.GONE);
        }
        if (collectViewModel.getPic() != null) {
            requestManager.load(collectViewModel.getPic()).centerCrop().into(pic);
        }
        if (isCheck) {
            rl_check.setVisibility(View.VISIBLE);
        } else {
            rl_check.setVisibility(View.GONE);
        }
        if (collectViewModel.isSelected()) {
            ivCheck.setSelected(true);
        } else {
            ivCheck.setSelected(false);
        }
    }

    public void setCheck(boolean check) {
        this.isCheck = check;
    }
}
