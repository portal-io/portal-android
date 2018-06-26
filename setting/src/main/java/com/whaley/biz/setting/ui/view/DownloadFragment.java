package com.whaley.biz.setting.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.ui.adapter.DownloadAdapter;
import com.whaley.biz.setting.ui.presenter.DownloadPresenter;
import com.whaley.biz.setting.ui.viewmodel.DownloadViewModel;
import com.whaley.biz.setting.widget.LargeTouchableAreasButton;
import com.whaley.biz.setting.widget.NoAlphaItemAnimator;
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

import static com.whaley.biz.setting.ui.presenter.LocalTabPresenter.STR_PARAM_TYPE;

/**
 * Created by dell on 2017/8/4.
 */

public class DownloadFragment extends BaseMVPFragment<DownloadPresenter> implements DownloadView {

    public static Fragment newInstance(int type) {
        DownloadFragment fragment = new DownloadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(STR_PARAM_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R2.id.tv_empty)
    TextView tvEmpty;
    @BindView(R2.id.empty)
    RelativeLayout empty;
    @BindView(R2.id.layout_check)
    RelativeLayout layoutCheck;
    @BindView(R2.id.tv_check)
    TextView tvCheck;
    @BindView(R2.id.tv_delete)
    TextView tvDelete;
    @BindView(R2.id.lv_download)
    RecyclerView lvDownload;

    private DownloadAdapter adapter;
    private ImageRequest.RequestManager requestManager;

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        lvDownload.setLayoutManager(layoutManager);
        lvDownload.setItemAnimator(new NoAlphaItemAnimator());
        adapter = new DownloadAdapter(requestManager, getPresenter());
        adapter.setOnRecyclerViewListener(new DownloadAdapter.OnRecyclerViewListener() {
            @Override
            public void onBtnClick(int position) {
                getPresenter().onItemClick(position, true);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int position) {
                if (layoutCheck.getVisibility() == View.GONE)
                    getPresenter().onItemClick(position, false);
                else {
                    getPresenter().check(position);
                }
            }
        });
        lvDownload.setAdapter(adapter);
    }

//    CompoundButton.OnCheckedChangeListener checkAllListener = new CompoundButton.OnCheckedChangeListener() {
//
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView,
//                                     boolean isChecked) {
//            if (cbCheckAll.isSelected()) {
//                getPresenter().getDownloadRepository().unheckAll();
//            } else {
//                getPresenter().getDownloadRepository().checkAll();
//            }
//            cbCheckAll.setSelected(!cbCheckAll.isSelected());
//            tvCheckNum.setText("(" + getPresenter().getDownloadRepository().getCheckNum()
//                    + "/" + getPresenter().getDownloadRepository().getTotalNum() + ")");
//            adapter.notifyDataSetChanged();
//        }
//    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_download;
    }

    @Override
    public void updateList() {
        LocalTabFragment fragment = (LocalTabFragment) getParentFragment();
        ITitleBar titleBar = fragment.getTitleBar();
        int type = fragment.getType();
        List<DownloadViewModel> list = getPresenter().getDownloadRepository().getItemDatas();
        if (list == null || list.size() <= 0) {
            if (titleBar != null && type == getPresenter().getDownloadRepository().getType()) {
                titleBar.setRightViewVisibility(View.GONE);
            }
        } else {
            if (titleBar != null && type == getPresenter().getDownloadRepository().getType()) {
                titleBar.setRightText("编辑");
                titleBar.setRightViewVisibility(View.VISIBLE);
            }
        }
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showEdit() {
        if (getParentFragment() instanceof LocalTabFragment) {
            layoutCheck.setVisibility(View.VISIBLE);
            ViewTreeObserver vto = layoutCheck.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layoutCheck.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    startAnimator(true);
                }
            });
            ((LocalTabFragment) getParentFragment()).getTitleBar()
                    .setLeftViewVisibility(View.INVISIBLE);
        }
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
                    tvDelete.setText("删除 (" + getPresenter().getDownloadRepository().getCheckNum()
                            + "/" + getPresenter().getDownloadRepository().getTotalNum() + ")");
                } else {
                    List<DownloadViewModel> list = getPresenter().getDownloadRepository().getItemDatas();
                    if (list == null || list.size() <= 0) {
                        if (titleBar != null && type == getPresenter().getDownloadRepository().getType()) {
                            titleBar.setRightViewVisibility(View.GONE);
                        }
                    } else {
                        if (titleBar != null && type == getPresenter().getDownloadRepository().getType()) {
                            titleBar.setRightViewVisibility(View.VISIBLE);
                            titleBar.setRightText("编辑");
                        }
                    }
                    layoutCheck.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
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
        if (getParentFragment() instanceof LocalTabFragment) {
            startAnimator(false);
            ((LocalTabFragment) getParentFragment()).getTitleBar()
                    .setLeftViewVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateDownloadText(int position) {
        RecyclerView.ViewHolder viewHolder = lvDownload.findViewHolderForAdapterPosition(position);
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            LargeTouchableAreasButton btnDownload = holder.getView(R.id.btn_download);
            TextView tvSize = holder.getView(R.id.tv_size);
            btnDownload.setText(getPresenter().getDownloadText(position));
            String totalSize = SettingUtil.formatFileSize(getPresenter().getTotalSize(position));
            if (!totalSize.equals(tvSize.getText().toString())) {
                tvSize.setText(totalSize);
            }
        }
    }

    @Override
    public void remove(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void updateCheck() {
        tvDelete.setText("删除 (" + getPresenter().getCheckNum()
                + "/" + getPresenter().getTotalNum() + ")");
        if(getPresenter().getCheckNum()==getPresenter().getTotalNum()){
            tvCheck.setText("取消全选");
        }else{
            tvCheck.setText("全选");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateCheck(int position) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void noList() {
        empty.setVisibility(View.VISIBLE);
        lvDownload.setVisibility(View.GONE);
        updateList();
    }

    @Override
    public void updateMainList() {
        empty.setVisibility(View.GONE);
        lvDownload.setVisibility(View.VISIBLE);
        updateList();
    }

    @OnClick(R2.id.tv_check)
    public void clickLayout() {
        getPresenter().onAllClick();
    }

    @OnClick(R2.id.tv_delete)
    public void clickDelete() {
        getPresenter().onDeleteClick();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onPause();
        } else {
            onResume();
        }
    }

}
