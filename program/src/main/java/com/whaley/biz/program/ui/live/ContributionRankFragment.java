package com.whaley.biz.program.ui.live;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.RecyclerLoaderFragment;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.live.adapter.ContributionAdapter;
import com.whaley.biz.program.ui.live.presenter.ContributionRankPresenter;
import com.whaley.biz.program.ui.uimodel.ContributionViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.viewholder.ListAdapter;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;
import butterknife.BindView;

/**
 * Created by YangZhi on 2017/10/12 17:47.
 */

@Route(path = ProgramRouterPath.CONTRIBUTION_RANK)
public class ContributionRankFragment extends RecyclerLoaderFragment<ContributionRankPresenter, Object> implements ContributionRankPresenter.ContributionRankView<Object> {

    static final int ITEM_HEIGHT = DisplayUtil.convertDIP2PX(56);

    @BindView(R2.id.layout_contribution_item)
    View myContributionView;

    @BindView(R2.id.layout_gift_empty)
    View layoutGiftEmpty;

    @BindView(R2.id.tv_msg)
    TextView tvMsg;

    ContributionAdapter.ContributionViewHolder myContributionViewHolder;

    ContributionAdapter adapter;

    int loginUserPosition = -1;

    @Override
    protected ListAdapter onCreateAdapter() {
        adapter = new ContributionAdapter();
        adapter.setRequestManager(ImageLoader.with(this));
        return adapter;
    }

    @Override
    protected boolean isShouldLoadMore() {
        return false;
    }

    @Override
    protected ContributionRankPresenter onCreatePresenter() {
        return new ContributionRankPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contribution_rank;
    }

    @Override
    protected RecyclerView onCreateRecyclerView(View rootView) {
        return (RecyclerView) rootView.findViewById(R.id.recycleView);
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        myContributionView.setBackgroundColor(Color.WHITE);
        myContributionViewHolder = ContributionAdapter.ContributionViewHolder.createViewHolderByNormal(myContributionView);
        myContributionViewHolder.hideLine();
        myContributionView.setVisibility(View.GONE);

        tvMsg.setText("贡献榜还是空的\n赶快去送出第一个礼物吧");

        layoutGiftEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onRefresh();
            }
        });

        getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(loginUserPosition <=0 ){
                    hideMyContribution(false);
                    return;
                }
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItem == 0) {
                    hideMyContribution(false);
                    return;
                }
                int lastCompleteVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                if(lastVisibleItem < loginUserPosition){
                    showMyContribution(true);
                }else if(lastVisibleItem == loginUserPosition){
                    if(lastCompleteVisibleItem == lastVisibleItem){
                        hideMyContribution(false);
                    }else {
                        showMyContribution(false);
                    }
                }else {
//                    int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
//                    if(firstVisibleItem > loginUserPosition){
//                        showMyContribution(true);
//                    }else {
                        hideMyContribution(false);
//                    }
                }
            }
        });
    }

    private void showMyContribution(boolean isAnim){
        if(!isAnim){
            if(myContributionView.getVisibility() != View.VISIBLE){
                myContributionView.setVisibility(View.VISIBLE);
            }
            myContributionView.setTranslationY(1f);
            myContributionView.setAlpha(1f);
            return;
        }
        AdditiveAnimator.cancelAnimations(myContributionView);
        AdditiveAnimator.animate(myContributionView)
                .translationY(1f)
                .alpha(1f)
                .addStartAction(new Runnable() {
                    @Override
                    public void run() {
                        myContributionView.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    private void hideMyContribution(boolean isAnim){
        if(!isAnim){
            if(myContributionView.getVisibility() == View.VISIBLE){
                myContributionView.setVisibility(View.GONE);
            }
            myContributionView.setTranslationY(ITEM_HEIGHT);
            myContributionView.setAlpha(0f);
            return;
        }
        AdditiveAnimator.cancelAnimations(myContributionView);
        AdditiveAnimator.animate(myContributionView)
                .translationY(ITEM_HEIGHT)
                .alpha(0f)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        if(wasCancelled){
                            myContributionView.setVisibility(View.GONE);
                        }
                    }
                })
                .start();
    }

    @Override
    public void showMyContribution(ContributionViewModel viewModel, int loginUserPosition) {
        if(viewModel!=null) {
            adapter.onBindContributionViewHolder(myContributionViewHolder, viewModel, 0);
            this.loginUserPosition = loginUserPosition;
        }else {
            this.loginUserPosition = -1;
        }
    }

    @Override
    public EmptyDisplayView getEmptyDisplayView() {
        return new EmptyDisplayView() {
            @Override
            public void showEmpty() {
                showContent();
                layoutGiftEmpty.setVisibility(View.VISIBLE);
            }

            @Override
            public void showLoading(String s) {
                ContributionRankFragment.super.getEmptyDisplayView().showLoading(s);
                layoutGiftEmpty.setVisibility(View.GONE);
            }

            @Override
            public void showError(Throwable throwable) {
                ContributionRankFragment.super.getEmptyDisplayView().showError(throwable);
                layoutGiftEmpty.setVisibility(View.GONE);
            }

            @Override
            public void showContent() {
                ContributionRankFragment.super.getEmptyDisplayView().showContent();
                layoutGiftEmpty.setVisibility(View.GONE);
            }

            @Override
            public void setOnRetryListener(OnRetryListener onRetryListener) {
                ContributionRankFragment.super.getEmptyDisplayView().setOnRetryListener(onRetryListener);
            }
        };
    }
}
