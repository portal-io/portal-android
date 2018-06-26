package com.whaley.biz.setting.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.setting.R;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.ui.viewmodel.LocalVideoImportViewModel;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/7.
 */

public class LocalVideoImportAdapter extends RecyclerViewAdapter<LocalVideoImportViewModel, ViewHolder> {

    ImageRequest.RequestManager requestManager;

    public LocalVideoImportAdapter(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_local_video_import, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder collectViewHolder, LocalVideoImportViewModel localVideoImportViewModel, int position) {
        bindViewHolder(localVideoImportViewModel, collectViewHolder, position);
    }

    private void bindViewHolder(LocalVideoImportViewModel localVideoImportViewModel, ViewHolder holder, int position) {
        ImageView pic = holder.getView(R.id.pic);
        TextView name = holder.getView(R.id.name);
        TextView size = holder.getView(R.id.size);
        ImageView ivCheck = holder.getView(R.id.iv_check);
        name.setText(localVideoImportViewModel.name);
        size.setText(SettingUtil.formatFileSize(localVideoImportViewModel.size));
        if(localVideoImportViewModel.picPath != null) {
            requestManager.load(localVideoImportViewModel.picPath).centerCrop().into(pic);
        }
        if (localVideoImportViewModel.isCheck) {
            ivCheck.setSelected(true);
        } else {
            ivCheck.setSelected(false);
        }
    }

}

