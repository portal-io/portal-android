package com.whaley.biz.setting.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.ui.adapter.LocalVideoImportAdapter;
import com.whaley.biz.setting.ui.presenter.LocalImportByVideoPresenter;
import com.whaley.biz.setting.widget.BaseHeadFootAdapter;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.widget.adapter.ViewHolder;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/7.
 */

public class LocalImportByVideoFragment extends BaseMVPFragment<LocalImportByVideoPresenter> implements LocalImportByVideoView {

    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R2.id.loading_img)
    ImageView loadingImg;
    @BindView(R2.id.loading_layout)
    RelativeLayout loadingLayout;
    @BindView(R2.id.empty)
    RelativeLayout empty;

    ImageRequest.RequestManager requestManager;
    LocalVideoImportAdapter localVideoAdapter;

    @BindView(R2.id.tv_check)
    TextView tvCheck;
    @BindView(R2.id.tv_import)
    TextView tvImport;

    public static void goPage(Starter starter) {
        Intent intent = TitleBarActivity.createIntent(starter, LocalImportByVideoFragment.class.getName(), null, false);
        starter.startActivityForResult(intent, 0);
    }

    @OnClick(R2.id.tv_import)
    public void onClickImport(){
        getPresenter().importVideo();
    }

    @OnClick(R2.id.tv_check)
    void onCheck() {
        getPresenter().onAllClick(tvCheck.getText().toString().equals("全选"));
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        localVideoAdapter = new LocalVideoImportAdapter(requestManager);
        localVideoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int position) {
                getPresenter().onPlayerClick(position);
            }
        });
        setLoadingVisibility(true);
        BaseHeadFootAdapter headFootAdapter = new BaseHeadFootAdapter(localVideoAdapter);
        headFootAdapter.addFooter(LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_bar, recyclerview, false));
        recyclerview.setAdapter(headFootAdapter);
        if(getTitleBar()!=null) {
            getTitleBar().setTitleText("相册视频导入");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImageLoader.cancelRequests(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_import_local_video;
    }

    @Override
    public void updateData() {
        setLoadingVisibility(false);
        localVideoAdapter.setData(getPresenter().getLocalImportByVideoRepository().getLocalVideoBeanList());
        tvImport.setText("导入 (" + "0/" + getPresenter().getTotalNum() + ")");
        tvCheck.setText("全选");
    }

    @Override
    public void noData() {
        empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateImage(int position, String picPath) {
        RecyclerView.ViewHolder viewHolder = recyclerview.findViewHolderForAdapterPosition(position);
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            ImageView pic = holder.getView(R.id.pic);
            requestManager.load(picPath).centerCrop().into(pic);
        }
    }

    @Override
    public void updateCheck() {
        tvImport.setText("导入 (" + getPresenter().getCheckNum() + "/" + getPresenter().getTotalNum() + ")");
        if(getPresenter().getCheckNum()==getPresenter().getTotalNum()){
            tvCheck.setText("取消全选");
        }else{
            tvCheck.setText("全选");
        }
        localVideoAdapter.notifyDataSetChanged();
    }

    private void setLoadingVisibility(boolean Visible) {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.page_loading);
        LinearInterpolator lin = new LinearInterpolator();
        anim.setInterpolator(lin);
        if (Visible) {
            loadingLayout.setVisibility(View.VISIBLE);
            loadingImg.startAnimation(anim);
        } else {
            loadingImg.clearAnimation();
            loadingLayout.setVisibility(View.GONE);
        }
    }

}
