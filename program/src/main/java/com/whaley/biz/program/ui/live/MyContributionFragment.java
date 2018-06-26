package com.whaley.biz.program.ui.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.widget.emptylayout.EmptyDisplayLayout;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.ui.live.presenter.MyContributionPresenter;
import com.whaley.biz.program.ui.uimodel.MyContributionViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.refresh.OnRefreshListener;
import com.whaley.core.widget.refresh.RefreshLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by YangZhi on 2017/10/13 15:23.
 */

public class MyContributionFragment extends BaseMVPFragment<MyContributionPresenter> implements MyContributionPresenter.MyContributionView {

    @BindView(R2.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R2.id.iv_image)
    ImageView ivImage;
    @BindView(R2.id.tv_rank)
    TextView tvRank;
    @BindView(R2.id.tv_live_count)
    TextView tvLiveCount;
    @BindView(R2.id.tv_gift_count)
    TextView tvGiftCount;
    @BindView(R2.id.tv_wcoin_count)
    TextView tvWcoinCount;
    @BindView(R2.id.tv_my_favorite)
    TextView tvMyFavorite;
    @BindView(R2.id.layout_favorite_user)
    LinearLayout layoutFavoriteUser;
    @BindView(R2.id.layout_gift_empty)
    View layoutGiftEmpty;
    @BindView(R2.id.tv_msg)
    TextView tvMsg;

    @BindView(R2.id.emptyLayout)
    EmptyDisplayLayout emptyDisplayLayout;

    ImageRequest.RequestManager requestManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_contribution;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        requestManager = ImageLoader.with(this);
        tvMsg.setText("你还没有打赏记录\n赶快去送出第一个礼物吧");
        emptyDisplayLayout.setOnRetryListener(new EmptyDisplayView.OnRetryListener() {
            @Override
            public void onRetry() {
                getPresenter().onRetry();
            }
        });

        layoutGiftEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onRetry();
            }
        });
        refreshLayout.setListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().onRefresh();
            }
        });
    }

    @Override
    public void stopRefresh() {
        refreshLayout.stopRefresh(true);
    }

    @Override
    public void update(MyContributionViewModel contributionViewModel) {
        requestManager.load(contributionViewModel.getImageUrl())
                .centerCrop()
                .circle()
                .error(R.drawable.bg_placeholeder_circle_shape)
                .placeholder(R.drawable.bg_placeholeder_circle_shape)
                .into(ivImage);
        tvRank.setText(contributionViewModel.getRank());
        tvLiveCount.setText(contributionViewModel.getLiveCount());
        tvGiftCount.setText(contributionViewModel.getGiftCount());
        tvWcoinCount.setText(contributionViewModel.getWcoinCount());
        layoutFavoriteUser.removeAllViews();
        List<MyContributionViewModel.UserModel> userModels = contributionViewModel.getFavoriteUsers();
        if (userModels == null || userModels.size() == 0) {
            tvMyFavorite.setVisibility(View.GONE);
            layoutFavoriteUser.setVisibility(View.GONE);
            return;
        }
        tvMyFavorite.setVisibility(View.VISIBLE);
        layoutFavoriteUser.setVisibility(View.VISIBLE);
        int position = 0;
        for (MyContributionViewModel.UserModel userModel : userModels) {
            addFavoriteUser(userModel, position);
            position++;
        }
    }

    private void addFavoriteUser(MyContributionViewModel.UserModel userModel, int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_my_contribution_favorite_user, layoutFavoriteUser, false);
        if (position > 0) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            marginLayoutParams.leftMargin = DisplayUtil.convertDIP2PX(20);
        }
        ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        layoutFavoriteUser.addView(view);
        requestManager.load(userModel.getImageUrl())
                .centerCrop()
                .circle()
                .error(R.drawable.bg_placeholeder_circle_shape)
                .placeholder(R.drawable.bg_placeholeder_circle_shape)
                .into(ivImage);
        tvName.setText(userModel.getUserName());
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
                emptyDisplayLayout.showLoading(s);
                layoutGiftEmpty.setVisibility(View.GONE);
            }

            @Override
            public void showError(Throwable throwable) {
                emptyDisplayLayout.showError(throwable);
                layoutGiftEmpty.setVisibility(View.GONE);
            }

            @Override
            public void showContent() {
                emptyDisplayLayout.showContent();
                layoutGiftEmpty.setVisibility(View.GONE);
            }

            @Override
            public void setOnRetryListener(OnRetryListener onRetryListener) {
                emptyDisplayLayout.setOnRetryListener(onRetryListener);
            }
        };
    }
}
