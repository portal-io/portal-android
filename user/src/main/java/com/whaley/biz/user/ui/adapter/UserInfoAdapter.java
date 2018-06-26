package com.whaley.biz.user.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.common.widget.SettingViewLayout;
import com.whaley.biz.user.R;
import com.whaley.biz.user.ui.viewmodel.UserInfoViewModel;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;


/**
 * Author: qxw
 * Date: 2016/12/19
 */

public class UserInfoAdapter extends RecyclerViewAdapter<UserInfoViewModel, ViewHolder> {

    private OnSettingItemClickListener onItemClickListener;


    public void setOnSettingItemClickListener(OnSettingItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_info, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, UserInfoViewModel viewModel, final int position) {
        SettingViewLayout settingViewLayout = viewHolder.getView(R.id.view_user_info);
        settingViewLayout.setName(viewModel.getName());
        settingViewLayout.setText(viewModel.getText());
        settingViewLayout.setRightPic(viewModel.getRightPic());
        settingViewLayout.setCheckboxPic(viewModel.getCheckboxPic());
        settingViewLayout.setSegment(viewModel.getSegmentSize(), viewModel.getSegmentList());
        if (viewModel.isOnClick()) {
            settingViewLayout.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(viewHolder, position);
                    }
                }
            });
        } else {
            settingViewLayout.setOnItemClickListener(null);
        }
    }

    public interface OnSettingItemClickListener {
        void onItemClick(ViewHolder viewHolder, int position);
    }
}
