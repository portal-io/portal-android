package com.whaley.biz.setting.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.whaley.biz.common.widget.SettingViewLayout;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.ui.viewmodel.SettingViewModel;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import butterknife.BindView;


/**
 * Author: qxw
 * Date: 2016/12/19
 */

public class SettingAdapter extends RecyclerViewAdapter<SettingViewModel, ViewHolder> {

    private OnSettingItemClickListener onItemClickListener;


    public void setOnSettingItemClickListener(OnSettingItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_setting, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, SettingViewModel viewModel, final int position) {
        SettingViewLayout settingViewLayout = viewHolder.getView(R.id.view_setting);
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
        if (SettingViewModel.SETTING_WIFI == viewModel.getType()) {
            settingViewLayout.clearSegment();
            settingViewLayout.setChecked(!viewModel.isWifiOnly());
            settingViewLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onCheckedChanged(buttonView, isChecked, position);
                    }
                }
            });
        }else if(SettingViewModel.SETTING_LEVE == viewModel.getType()){
            settingViewLayout.clearChecked();
            settingViewLayout.selectSegment(viewModel.getSegmentIndex());
            settingViewLayout.setOnSegmentCheckedChangeListener(new SettingViewLayout.OnSegmentCheckedChangeListener() {
                @Override
                public void onCheckedChanged(int index) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onSegmentCheckedChanged(index, position);
                    }
                }
            });
        }else{
            settingViewLayout.clearChecked();
            settingViewLayout.clearSegment();
        }
    }

    public interface OnSettingItemClickListener {
        void onItemClick(ViewHolder viewHolder, int position);

        void onCheckedChanged(CompoundButton buttonView,
                              boolean isChecked, int position);

        void onSegmentCheckedChanged(int index, int position);
    }
}
