package com.whaley.biz.setting.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.ui.adapter.HistoryAdapter;
import com.whaley.biz.setting.ui.presenter.HistoryPresenter;
import com.whaley.biz.setting.ui.viewmodel.HistoryViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.ListAdapter;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/24.
 * 历史浏览
 */

@Route(path = SettingRouterPath.HISTORY)
public class HistoryFragment extends RecyclerLoaderFragment<HistoryPresenter, HistoryViewModel> implements HistoryView {

    @BindView(R2.id.layout_check)
    RelativeLayout layoutCheck;
    @BindView(R2.id.tv_delete)
    TextView tvDelete;
    @BindView(R2.id.tv_check)
    TextView tvCheck;

    private AnimatorSet animatorSet;

    @OnClick(R2.id.tv_check)
    void onCheck() {
        getPresenter().onAllClick();
    }

    @OnClick(R2.id.tv_delete)
    void onDelete() {
        getPresenter().showDeleteDialog();
    }

    StickyRecyclerHeadersDecoration headersDecor;
    HistoryAdapter adapter;
    ImageRequest.RequestManager requestManager;

    @Override
    public boolean onBackPressed() {
        if (getPresenter() != null && getPresenter().getHistoryRepository().isCheck()) {
            getPresenter().onCheck();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText("浏览历史");
            getTitleBar().setRightText(" ");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener() {
                @Override
                public void onRightClick(View view) {
                    super.onRightClick(view);
                    if (!getTitleBar().getRightText().trim().isEmpty())
                        getPresenter().onCheck();
                }
            });
        }
        getRecyclerView().addItemDecoration(headersDecor);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, null, false);
        ((TextView) emptyView.findViewById(R.id.tv_empty)).setText("浏览记录为空");
        emptyLayout.setEmptyView(emptyView);
    }

    @Override
    protected boolean isShouldLoadMore() {
        return false;
    }

    @Override
    protected ListAdapter onCreateAdapter() {
        adapter = new HistoryAdapter(requestManager);
        headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int position) {
                getPresenter().onItemClick(position);
            }
        });
        return adapter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_history;
    }

    @Override
    public void updateOnRefresh(List<HistoryViewModel> refreshData) {
        super.updateOnRefresh(refreshData);
        setBtnRight();
        cancelEdit();
    }

    private void startAnimator(final boolean isShow) {
        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
        } else {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
        }
        ObjectAnimator ivLogoAnim1 = ObjectAnimator.ofFloat(layoutCheck, "y",
                isShow ? DisplayUtil.screenH : layoutCheck.getTop(),
                isShow ? layoutCheck.getTop() : DisplayUtil.screenH);
        animatorSet.play(ivLogoAnim1);
        animatorSet.setDuration(300);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isShow) {
                    getPresenter().onUpdate(false);
                    layoutCheck.setVisibility(View.VISIBLE);
                    getTitleBar().setRightText("取消");
                    getTitleBar().setLeftViewVisibility(View.GONE);
                    updateDelete();
                    getRefreshLayout().setEnabled(false);
                } else {
                    setBtnRight();
                    layoutCheck.setVisibility(View.GONE);
                    getTitleBar().setLeftViewVisibility(View.VISIBLE);
                    getRefreshLayout().setEnabled(true);
                }
                adapter.setCheck(getPresenter().getHistoryRepository().isCheck());
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
        animatorSet.start();
    }

    private void setBtnRight() {
        List<HistoryViewModel> list = getPresenter().getLoaderRepository().getLoadListData().getViewDatas();
        if (getTitleBar() != null) {
            if (list != null && list.size() > 0) {
                getTitleBar().setRightText("编辑");
                getTitleBar().setRightViewVisibility(View.VISIBLE);
            } else {
                getTitleBar().setRightViewVisibility(View.GONE);
            }
        }
    }

    private void updateDelete() {
        tvDelete.setText("删除 " + "(" + getPresenter().getCheckNum() + "/"
                + getPresenter().getTotalNum() + ")");
        if (getPresenter().getCheckNum() == getPresenter().getTotalNum()) {
            tvCheck.setText("取消全选");
        } else {
            tvCheck.setText("全选");
        }
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
    }

    @Override
    public void cancelEdit() {
        if (layoutCheck.getVisibility() == View.VISIBLE) {
            getPresenter().getHistoryRepository().getCheckList().clear();
            startAnimator(false);
        }
    }

    @Override
    public void update(int position) {
        adapter.notifyItemChanged(position);
        updateDelete();
    }

    @Override
    public void updateAll() {
        adapter.notifyDataSetChanged();
        updateDelete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
            animatorSet = null;
        }
    }

}
