package com.whaley.biz.program.ui.follow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.common.widget.emptylayout.EmptyDisplayLayout;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.follow.presenter.FollowMyPresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.fragment.RefreshUIViewListFragment;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.widget.refresh.RefreshLayout;
import com.whaley.core.widget.titlebar.TitleBar;
import com.whaleyvr.core.network.http.exception.EmptyDataError;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */
@Route(path = ProgramRouterPath.FOLLOW_MY)
public class FollowMyFragment extends RefreshUIViewListFragment<FollowMyPresenter> implements FollowMyView {


    @BindView(R2.id.titlebar)
    TitleBar titlebar;
    @BindView(R2.id.refreshLayout)
    RefreshLayout refreshLayout;

    EmptyViewHolder emptyViewHolder;
    @BindView(R2.id.emptyLayout)
    EmptyDisplayLayout emptyLayout;

//    @Override
//    public boolean isFragmentVisible() {
//        boolean isFragmentVisible = super.isFragmentVisible();
//        getPresenter().isFragmentVisible(isFragmentVisible);
//        return isFragmentVisible;
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_follow_my;
    }


    @Override
    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        titlebar.setTitleText(getString(R.string.title_follow_my));
        titlebar.setLeftIcon(R.drawable.btn_go_follow_selector);
        titlebar.setTitleBarListener(new SimpleTitleBarListener() {
            @Override
            public void onLeftClick(View view) {
                getPresenter().onFollowClick();
            }
        });
    }

    @Override
    public void onViewClick(ClickableUIViewHolder viewHolder) {
        getPresenter().onViewClick(viewHolder.getData(), viewHolder.getPosition());
    }

    @Override
    protected boolean isShouldLoadMore() {
        return true;
    }


    @Override
    public void showEmptyLogin() {
        setEmptyView(false);
        emptyLayout.showEmpty();
    }

    @Override
    public void updataNoData() {
        setEmptyView(true);
        emptyLayout.showEmpty();
    }

    public EmptyDisplayLayout getEmptyLayout() {
        return emptyLayout;
    }

    @Override
    public EmptyDisplayView getEmptyDisplayView() {
        return new EmptyDisplayView() {
            @Override
            public void showEmpty() {
                setEmptyView(true);
                emptyLayout.showEmpty();
            }

            @Override
            public void showLoading(String loadtext) {
                emptyLayout.showLoading(loadtext);
            }

            @Override
            public void showError(Throwable throwable) {
                if (throwable != null && throwable instanceof EmptyDataError) {
                    showEmpty();
                } else {
                    emptyLayout.showError(throwable);
                }
            }

            @Override
            public void showContent() {
                emptyLayout.showContent();
            }

            @Override
            public void setOnRetryListener(OnRetryListener onRetryListener) {
                emptyLayout.setOnRetryListener(onRetryListener);
            }
        };
    }

    public void setEmptyView(boolean isNoFollow) {
        if (emptyViewHolder == null) {
            View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_no_follow, null, false);
            emptyLayout.setEmptyView(emptyView);
            emptyViewHolder = new EmptyViewHolder(emptyView);
            emptyViewHolder.btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onLoginClick();
                }
            });
        }
        if (isNoFollow) {
            emptyViewHolder.ivImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_no_follows));
            emptyViewHolder.btnLogin.setVisibility(View.GONE);
            emptyViewHolder.tvError.setText(R.string.empty_no_follow);
            emptyViewHolder.resetLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().refreshData();
                }
            });
        } else {
            emptyViewHolder.ivImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_follow_no_login));
            emptyViewHolder.btnLogin.setVisibility(View.VISIBLE);
            emptyViewHolder.tvError.setText(R.string.empty_no_login);
            emptyViewHolder.resetLayout.setOnClickListener(null);
        }


    }


    static class EmptyViewHolder {
        @BindView(R2.id.iv_img)
        ImageView ivImg;
        @BindView(R2.id.tv_error)
        TextView tvError;
        @BindView(R2.id.btn_login)
        Button btnLogin;
        @BindView(R2.id.reset_layout)
        FrameLayout resetLayout;

        EmptyViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
