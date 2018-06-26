package com.whaley.biz.setting.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.ui.adapter.LocalVideoAdapter;
import com.whaley.biz.setting.ui.presenter.LocalTabPresenter;
import com.whaley.biz.setting.ui.presenter.LocalVideoPresenter;
import com.whaley.biz.setting.ui.viewmodel.LocalVideoViewModel;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.adapter.ViewHolder;
import com.whaley.core.widget.titlebar.ITitleBar;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/4.
 */

public class LocalVideoFragment extends BaseMVPFragment<LocalVideoPresenter> implements LocalVideoView {


    @BindView(R2.id.btn_gallery)
    LinearLayout btnGallery;
    @BindView(R2.id.btn_link)
    LinearLayout btnLink;
    @BindView(R2.id.btn_qrcode)
    LinearLayout btnQrcode;
    @BindView(R2.id.iv_line)
    ImageView ivLine;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R2.id.loading_img)
    ImageView loadingImg;
    @BindView(R2.id.loading_layout)
    RelativeLayout loadingLayout;
    @BindView(R2.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R2.id.tv_empty)
    TextView tvEmpty;
    @BindView(R2.id.empty)
    RelativeLayout empty;
    @BindView(R2.id.view_line)
    View viewLine;
    @BindView(R2.id.tv_check)
    TextView tvCheck;
    @BindView(R2.id.tv_delete)
    TextView tvDelete;
    @BindView(R2.id.layout_check)
    RelativeLayout layoutCheck;

    private ImageRequest.RequestManager requestManager;
    private LocalVideoAdapter localVideoAdapter;
    private boolean isShowDialog = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_video;
    }

    public static Fragment newInstance(int type) {
        LocalVideoFragment fragment = new LocalVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LocalTabPresenter.STR_PARAM_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        localVideoAdapter = new LocalVideoAdapter(requestManager);
        localVideoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int position) {
                if (getPresenter().getLocalVideoRepository().isShowEdit()) {
                    getPresenter().check(localVideoAdapter.getItem(position).getVideoBean().getId(), !localVideoAdapter.getItem(position).isOnCheck());
                    updatePosition(position);
                } else {
                    getPresenter().onPlayerClick(position);
                }
            }
        });
        recyclerview.setAdapter(localVideoAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImageLoader.cancelRequests(this);
    }

    @Override
    public void updateData() {
        empty.setVisibility(View.GONE);
        setLoadingVisibility(false);
//        cbCheckAll.setSelected(false);
        localVideoAdapter.setData(getPresenter().getLocalVideoRepository().getLocalVideoBeanList());
        getPresenter().checkEmpty();
        updateList();
    }

    @Override
    public void noData() {
        localVideoAdapter.setData(getPresenter().getLocalVideoRepository().getLocalVideoBeanList());
        localVideoAdapter.notifyDataSetChanged();
        empty.setVisibility(View.VISIBLE);
        updateList();
    }

    private void updateList() {
        LocalTabFragment fragment = (LocalTabFragment) getParentFragment();
        ITitleBar titleBar = fragment.getTitleBar();
        int type = fragment.getType();
        List<LocalVideoViewModel> list = getPresenter().getLocalVideoRepository().getLocalVideoBeanList();
        if (list == null || list.size() <= 0) {
            if (titleBar != null && type == getPresenter().getLocalVideoRepository().getType()) {
                titleBar.setRightViewVisibility(View.GONE);
            }
        } else {
            if (titleBar != null && type == getPresenter().getLocalVideoRepository().getType()) {
                titleBar.setRightText("编辑");
                titleBar.setRightViewVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void updatePosition(int position) {
        if (recyclerview == null)
            return;
        RecyclerView.ViewHolder viewHolder = recyclerview.findViewHolderForAdapterPosition(position);
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            if (holder == null) {
                return;
            }
            localVideoAdapter.onBindViewHolder(holder, localVideoAdapter.getItem(position), position);
        }
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


    @OnClick(R2.id.btn_qrcode)
    public void clickQrcode() {
        getPresenter().onClickQrcode();
    }

    @OnClick(R2.id.btn_link)
    public void clickLink() {
        getPresenter().onClickLink(LocalVideoFragment.this);
    }

    @OnClick(R2.id.btn_gallery)
    public void clickGallery() {
        getPresenter().onClickGallery();
    }

    private void startAnimator(final boolean isShow) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator ivLogoAnim1 = ObjectAnimator.ofFloat(layoutCheck, "y",
                isShow ? DisplayUtil.screenH : layoutCheck.getTop(),
                isShow ? layoutCheck.getTop() : DisplayUtil.screenH);
        set.play(ivLogoAnim1);
        set.setDuration(300);
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                LocalTabFragment fragment = (LocalTabFragment) getParentFragment();
                int type = fragment.getType();
                ITitleBar titleBar = fragment.getTitleBar();
                if (isShow) {
                    titleBar.setRightText("取消");
                    updateDelete();
                } else {
                    List<LocalVideoViewModel> list = getPresenter().getLocalVideoRepository().getLocalVideoBeanList();
                    if (list == null || list.size() <= 0) {
                        if (titleBar != null && type == getPresenter().getLocalVideoRepository().getType()) {
                            titleBar.setRightViewVisibility(View.GONE);
                        }
                    } else {
                        if (titleBar != null && type == getPresenter().getLocalVideoRepository().getType()) {
                            titleBar.setRightViewVisibility(View.VISIBLE);
                            titleBar.setRightText("编辑");
                        }
                    };
                    layoutCheck.setVisibility(View.GONE);
//                    cbCheckAll.setSelected(false);
                }
                localVideoAdapter.setOnCheck(getPresenter().isCheck());
                localVideoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    @Override
    public void cancelEdit() {
        LocalTabFragment fragment = (LocalTabFragment) getParentFragment();
        ITitleBar titleBar = fragment.getTitleBar();
        int type = fragment.getType();
        if (titleBar != null && type == getPresenter().getLocalVideoRepository().getType()) {
            titleBar.setRightText("编辑");
            titleBar.setLeftViewVisibility(View.VISIBLE);
        }
        startAnimator(false);
    }

    @Override
    public void showEditButton() {
        LocalTabFragment fragment = (LocalTabFragment) getParentFragment();
        ITitleBar titleBar = fragment.getTitleBar();
        int type = fragment.getType();
        if (titleBar != null && type == getPresenter().getLocalVideoRepository().getType()) {
            titleBar.setRightViewVisibility(View.VISIBLE);
            if (getPresenter().getLocalVideoRepository().isShowEdit()) {
                titleBar.setRightText("取消");
            } else {
                titleBar.setRightText("编辑");
            }
        }
    }

    @Override
    public void hideEditButton() {
        LocalTabFragment fragment = (LocalTabFragment) getParentFragment();
        ITitleBar titleBar = fragment.getTitleBar();
        int type = fragment.getType();
        if (titleBar != null && type == getPresenter().getLocalVideoRepository().getType()) {
            titleBar.setRightViewVisibility(View.GONE);
        }
    }

    @Override
    public void updateCheck() {
        localVideoAdapter.notifyDataSetChanged();
        updateDelete();
    }

    private void updateDelete() {
        tvDelete.setText("删除 (" + getPresenter().getCheckNum() + "/" + getPresenter().getTotalNum() + ")");
        if(getPresenter().getCheckNum()==getPresenter().getTotalNum()){
            tvCheck.setText("取消全选");
        }else{
            tvCheck.setText("全选");
        }
    }

    @Override
    public void showMemoryFullDialog() {
        if (isShowDialog) return;
        isShowDialog = true;
        DialogUtil.showDialogCustomConfirm(this.getActivity(),
                "导入失败\n手机存储空间不够，\n请清理后再导入", null,
                "确定", null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isShowDialog = false;
                    }
                }, null, true);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showEdit() {
        layoutCheck.setVisibility(View.VISIBLE);
        ViewTreeObserver vto = layoutCheck.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutCheck.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startAnimator(true);
            }
        });
        LocalTabFragment fragment = (LocalTabFragment) getParentFragment();
        ITitleBar titleBar = fragment.getTitleBar();
        if (titleBar != null) {
            titleBar.setLeftViewVisibility(View.INVISIBLE);
        }

    }

    @OnClick(R2.id.tv_check)
    void onCheck() {
        getPresenter().onAllClick();
    }

    @OnClick(R2.id.tv_delete)
    void onDelete() {
        getPresenter().onDeleteClick();
    }

}
