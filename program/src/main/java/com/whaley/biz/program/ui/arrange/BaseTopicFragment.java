package com.whaley.biz.program.ui.arrange;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.common.widget.emptylayout.EmptyDisplayLayout;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.ui.arrange.presenter.BaseTopicPresenter;
import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.adapter.RecyclerViewUIAdapter;
import com.whaley.biz.program.uiview.adapter.ShareBottomUIAdapter;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.biz.program.widget.PullBehaviorImpl;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.widget.titlebar.ITitleBar;
import com.whaley.core.widget.titlebar.TitleBar;
import com.yz.pullable.PullableLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date:2017/8/10
 * Introduction:
 */

public class BaseTopicFragment<P extends BaseTopicPresenter> extends BaseMVPFragment<P> implements TopicView, ShareBottomUIAdapter.ShareClickListener, EmptyDisplayLayout.OnRetryListener {

    @BindView(R2.id.ivImage)
    ImageView ivImage;
    @BindView(R2.id.rvList)
    RecyclerView rvList;
    @BindView(R2.id.pullableLayout)
    protected PullableLayout pullableLayout;
    @BindView(R2.id.titlebar)
    TitleBar titlebar;
    @BindView(R2.id.iv_player)
    ImageView ivPlayer;
    @BindView(R2.id.emptyLayout)
    EmptyDisplayLayout emptyLayout;
    @BindView(R2.id.vs_buy)
    protected ViewStub vsBuy;

    protected PullBehaviorImpl pullBehavior;
    protected ImageRequest.RequestManager requestManager;
    protected RecyclerViewUIAdapter.RecyclerHolder recyclerHolder;
    protected RecyclerViewUIAdapter uiAdapter;

    @OnClick(R2.id.iv_player)
    void onPlayerClick() {
        getPresenter().onPlayerClick();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtil.setTransparentFullStatusBar(getActivity().getWindow(), getSystemBarTintManager(), false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        requestManager = ImageLoader.with(this);
        titlebar.setPaddingStatus(true);
        if (getTitleBar() != null) {
            getTitleBar().setRightIcon(R.drawable.share_white_selector);
            getTitleBar().setLeftIcon(R.drawable.back_white_selector);
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener() {
                @Override
                public void onRightClick(View view) {
                    getPresenter().onShareClick(ShareConstants.TYPE_ALL);
                }
            });
            pullBehavior = new PullBehaviorImpl(ivImage, getTitleBar(), ivPlayer);
            pullBehavior.setPullBehaviorListener(new PullBehaviorImpl.PullBehaviorListener() {
                private boolean isWhite;

                @Override
                public void isWhite(boolean iswhite) {
                    if (iswhite == isWhite) {
                        return;
                    }
                    if (iswhite) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        StatusBarUtil.setTransparentFullStatusBar(getActivity().getWindow(), getSystemBarTintManager(), false);
                    } else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        StatusBarUtil.setWhiteFullStatusBar(getActivity().getWindow(), getSystemBarTintManager());
                    }
                    isWhite = iswhite;
                }
            });
            pullableLayout.addBehavior(pullBehavior);
            rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
            uiAdapter = new RecyclerViewUIAdapter();
            uiAdapter.setOnUIViewClickListener(this);
            recyclerHolder = new RecyclerViewUIAdapter.RecyclerHolder(rvList);
            uiAdapter.setRecyclerHolder(recyclerHolder);
            uiAdapter.setRequestManager(requestManager);
            uiAdapter.onCreateView(pullableLayout, 0);
        }
        emptyLayout.setOnRetryListener(this);
    }

    @Override
    public EmptyDisplayView getEmptyDisplayView() {
        return emptyLayout;
    }

    private SystemBarTintManager getSystemBarTintManager() {
        return ((CommonActivity) getActivity()).getSystemBarManager();
    }

    @Override
    public void onViewClick(ClickableUIViewHolder uiViewHolder) {
        getPresenter().onViewClick(uiViewHolder.getPosition(), uiViewHolder.getData());
    }

    @Override
    public ITitleBar getTitleBar() {
        return titlebar;
    }

    @Override
    public void updata(RecyclerViewModel recyclerViewModel) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtil.setTransparentFullStatusBar(getActivity().getWindow(), getSystemBarTintManager(), false);
        pullBehavior.hideTitle();
        TopicHeadViewModel topicHeadViewModel = getPresenter().getTopicHead();
        if (topicHeadViewModel != null) {
            requestManager.load(topicHeadViewModel.getBigImageUrl()).big().fitCenter().into(ivImage);
            getTitleBar().setTitleText(topicHeadViewModel.getName());
        }
        if (uiAdapter != null && recyclerHolder != null) {
            uiAdapter.onBindView(recyclerHolder, recyclerViewModel, 0);
        }
        pullBehavior.setShowPlayer(getPresenter().isShowPlayer());
    }

    @Override
    public void onError() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtil.setWhiteFullStatusBar(getActivity().getWindow(), getSystemBarTintManager());
        pullBehavior.showTitle();
    }


    @Override
    public void onRetry() {
        getPresenter().getData();
    }

    @Override
    public void onShareClick(int type) {
        getPresenter().onShareClick(type);
    }
}
