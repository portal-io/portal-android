package com.whaley.biz.setting.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.setting.R;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.ui.presenter.DownloadPresenter;
import com.whaley.biz.setting.ui.viewmodel.DownloadViewModel;
import com.whaley.biz.setting.widget.LargeTouchableAreasButton;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;

/**
 * Created by dell on 2017/8/4.
 */

public class DownloadAdapter extends RecyclerViewAdapter<DownloadViewModel, ViewHolder> {

    ImageRequest.RequestManager requestManager;
    DownloadPresenter presenter;

    public DownloadAdapter(ImageRequest.RequestManager requestManager, DownloadPresenter presenter) {
        this.requestManager = requestManager;
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateNewViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_download_video, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, DownloadViewModel downloadViewModel, int position) {
        bindViewHolder(downloadViewModel, viewHolder, position);
    }

    private void bindViewHolder(DownloadViewModel downloadViewModel, ViewHolder holder, final int position) {
        ImageView ivCheck = holder.getView(R.id.iv_check);
        RelativeLayout rlCheck = holder.getView(R.id.rl_check);
        ImageView ivPic = holder.getView(R.id.iv_pic);
        LargeTouchableAreasButton btnDownload = holder.getView(R.id.btn_download);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvSize = holder.getView(R.id.tv_size);
        TextView tvIntro = holder.getView(R.id.tv_intro);
        LinearLayout llContent = holder.getView(R.id.ll_content);
        RelativeLayout layoutCheck = holder.getView(R.id.layout_check);
        tvName.setText(downloadViewModel.itemData.name);
        tvSize.setText(SettingUtil.formatFileSize(downloadViewModel.itemData.totalSize));
        tvIntro.setText(downloadViewModel.itemData.intro);
        requestManager.load(downloadViewModel.itemData.pic).centerCrop().size(360, 204).into(ivPic);
        btnDownload.setText(presenter.getDownloadText(position));
        if (presenter.getDownloadRepository().isOnCheck()) {
            btnDownload.setVisibility(View.GONE);
            rlCheck.setVisibility(View.VISIBLE);
        } else {
            btnDownload.setVisibility(View.VISIBLE);
            rlCheck.setVisibility(View.GONE);
            layoutCheck.setOnClickListener(null);
        }
        if (downloadViewModel.isSelect) {
            ivCheck.setSelected(true);
        } else {
            ivCheck.setSelected(false);
        }
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onRecyclerViewListener) {
                    onRecyclerViewListener.onBtnClick(position);
                }
            }
        });

    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public interface OnRecyclerViewListener {
        void onBtnClick(int position);
    }

}
