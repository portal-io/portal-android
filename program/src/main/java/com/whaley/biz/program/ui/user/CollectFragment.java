package com.whaley.biz.program.ui.user;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.user.adapter.CollectAdapter;
import com.whaley.biz.program.ui.user.presenter.CollectPresenter;
import com.whaley.biz.program.ui.user.viewmodel.CollectViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.adapter.ViewHolder;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.ListAdapter;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

;

/**
 * Created by dell on 2017/8/1.
 */

@Route(path = ProgramRouterPath.COLLECT, extras = RouterConstants.EXTRA_LOGIN)
public class CollectFragment extends RecyclerLoaderFragment<CollectPresenter, CollectViewModel> implements CollectView {

    CollectAdapter adapter;
    ImageRequest.RequestManager requestManager;
    //    @BindView(R2.id.cb_check_all)
//    CheckBox cbCheckAll;
//    @BindView(R2.id.tv_check_num)
//    TextView tvCheckNum;
    @BindView(R2.id.layout_check)
    RelativeLayout layoutCheck;
    @BindView(R2.id.tv_delete)
    TextView tvDelete;
    @BindView(R2.id.tv_check)
    TextView tvCheck;

    @OnClick(R2.id.tv_check)
    void onCheck() {
        getPresenter().onAllClick();
    }

    @OnClick(R2.id.tv_delete)
    void onDelete() {
        getPresenter().onClickDelete();
    }

    private AnimatorSet animatorSet;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collect;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText("我的播单");
            getTitleBar().setRightText(" ");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener() {
                @Override
                public void onRightClick(View view) {
                    if (!getTitleBar().getRightText().trim().isEmpty())
                        getPresenter().onClickEdit();
                }
            });
        }
        emptyLayout.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_collect, null, false));
//        cbCheckAll.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && getPresenter().isEdit()) {
            getPresenter().onClickEdit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void updateOnRefresh(List<CollectViewModel> refreshData) {
        super.updateOnRefresh(refreshData);
        if (refreshData != null && !refreshData.isEmpty()) {
            getTitleBar().setRightText("编辑");
            getTitleBar().setRightViewVisibility(View.VISIBLE);
        } else {
            getTitleBar().setRightViewVisibility(View.GONE);
        }
    }

    @Override
    protected ListAdapter onCreateAdapter() {
        adapter = new CollectAdapter(requestManager);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder iViewHolder, int position) {
                getPresenter().onItemClick(position);
            }
        });
        return adapter;
    }

    @Override
    protected boolean isShouldLoadMore() {
        return true;
    }

    @Override
    public void onChangeEdit(boolean isEdit) {
        if (isEdit) {
            layoutCheck.setVisibility(View.VISIBLE);
            ViewTreeObserver vto = layoutCheck.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layoutCheck.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    startAnimator(true);
                }
            });
        } else {
            getPresenter().getSelectedList().clear();
            startAnimator(false);
        }
        adapter.notifyDataSetChanged();
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
                    getPresenter().unCheckAll();
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
                adapter.setCheck(getPresenter().isEdit());
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
        List<CollectViewModel> list = getPresenter().getListViewModel();
        if (getTitleBar() != null) {
            if (list != null && list.size() > 0) {
                getTitleBar().setRightText("编辑");
                getTitleBar().setRightViewVisibility(View.VISIBLE);
            } else {
                getTitleBar().setRightViewVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onChangeSelected(boolean isSelected, int position) {
        RecyclerView.ViewHolder viewHolder = getRecyclerView().findViewHolderForAdapterPosition(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        ImageView ivCheck = holder.getView(R.id.iv_check);
        ivCheck.setSelected(isSelected);
        tvDelete.setText("删除 (" + getPresenter().getCheckNum() + "/" + getPresenter().getTotalNum() + ")");
        if (getPresenter().getCheckNum() == getPresenter().getTotalNum()) {
            tvCheck.setText("取消全选");
        } else {
            tvCheck.setText("全选");
        }
    }

    @Override
    public void onChangeCheck() {
        adapter.notifyDataSetChanged();
        updateDelete();

    }

    private void updateDelete() {
        tvDelete.setText("删除 (" + getPresenter().getCheckNum() + "/"
                + getPresenter().getTotalNum() + ")");
        if (getPresenter().getCheckNum() == getPresenter().getTotalNum()) {
            tvCheck.setText("取消全选");
        } else {
            tvCheck.setText("全选");
        }
    }

    @Override
    public void onResetEdit() {
//        cbCheckAll.setSelected(false);
    }

    @Override
    public void onRemove() {
        adapter.setData(getPresenter().getLoaderRepository().getLoadListData().getViewDatas());
        if (adapter.getData() == null || adapter.getData().isEmpty()) {
            getTitleBar().setRightViewVisibility(View.GONE);
            getEmptyDisplayView().showEmpty();
        }
    }

}
