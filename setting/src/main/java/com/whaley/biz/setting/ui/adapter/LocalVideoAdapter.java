package com.whaley.biz.setting.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.setting.DownloadStatus;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.db.LocalVideoBean;
import com.whaley.biz.setting.ui.presenter.LocalVideoPresenter;
import com.whaley.biz.setting.ui.viewmodel.LocalVideoViewModel;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/4.
 */

public class LocalVideoAdapter extends RecyclerViewAdapter<LocalVideoViewModel, ViewHolder> {

    ImageRequest.RequestManager requestManager;
    boolean isOnCheck;

    public void setOnCheck(boolean isOnCheck) {
        this.isOnCheck = isOnCheck;
    }

    public LocalVideoAdapter(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_local_video, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, LocalVideoViewModel localVideoViewModel, int position) {
        bindViewHolder(localVideoViewModel, viewHolder, position);
    }

    private void bindViewHolder(LocalVideoViewModel localVideoViewModel, ViewHolder holder, int position) {
        ImageView pic = holder.getView(R.id.pic);
        TextView name = holder.getView(R.id.name);
        TextView size = holder.getView(R.id.size);
        View ll_content = holder.getView(R.id.ll_content);
        ProgressBar progressBar = holder.getView(R.id.pb_download);
        TextView tvState = holder.getView(R.id.tv_state);
        TextView tvSize = holder.getView(R.id.tv_size);
        ImageView ivCheck = holder.getView(R.id.iv_check);
        RelativeLayout rlCheck = holder.getView(R.id.rl_check);

        final LocalVideoBean bean = localVideoViewModel.getVideoBean();
        size.setText(SettingUtil.formatFileSize(bean.size));
        name.setText(bean.name);
        if (bean.isDowloading) {
            progressBar.setProgress(bean.progress);
            if (isOnCheck) {
                size.setVisibility(View.VISIBLE);
                ll_content.setVisibility(View.GONE);
                size.setText(SettingUtil.formatFileSize(bean.getSize()));
            } else {
                size.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
            }
            tvSize.setText(SettingUtil.formatFileSize(bean.progressSize) + "/" + SettingUtil.formatFileSize(bean.size));
            if (DownloadStatus.DOWNLOAD_STATUS_DOWNLOADING == localVideoViewModel.getStatus()) {
                tvState.setText(SettingUtil.formatFileSize(bean.speed) + "/s");
            } else {
                tvState.setText("等待");
            }
            pic.setImageResource(R.mipmap.icon_file_unknown);
        } else {
            size.setVisibility(View.VISIBLE);
            ll_content.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(bean.picPath)) {
                requestManager.load(bean.picPath).placeholder(R.mipmap.icon_file_downloading)
                        .error(R.mipmap.icon_file_unknown).centerCrop().into(pic);
            } else {
                pic.setImageResource(R.mipmap.icon_file_unknown);
            }
        }
        if (isOnCheck) {
            rlCheck.setVisibility(View.VISIBLE);
        } else {
            rlCheck.setVisibility(View.GONE);
        }
        if (localVideoViewModel.isOnCheck()) {
            ivCheck.setSelected(true);
        } else {
            ivCheck.setSelected(false);
        }
    }

}
