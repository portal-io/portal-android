package com.whaley.biz.program.ui.live;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.exception.NoNetworkErrorException;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.live.adapter.LiveCompletedContributionAdapter;
import com.whaley.biz.program.ui.live.adapter.MemberRankAdapter;
import com.whaley.biz.program.ui.live.presenter.LiveCompletedPresenter;
import com.whaley.biz.program.ui.uimodel.LiveCompletedViewModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.image.Transformation;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.FastBlur;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.List;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YangZhi on 2017/10/16 12:56.
 */

@Route(path = ProgramRouterPath.LIVE_COMPLETED)
public class LiveCompletedFragment extends BaseMVPFragment<LiveCompletedPresenter> implements LiveCompletedPresenter.LiveCompletedView {

    @BindView(R2.id.iv_background)
    ImageView ivBackground;
    @BindView(R2.id.vs_normal)
    ViewStub vsNormal;
    @BindView(R2.id.vs_contribution)
    ViewStub vsContribution;
    @BindView(R2.id.layout_loading)
    View layoutLoading;
    @BindView(R2.id.iv_loading)
    ImageView ivLoading;
    @BindView(R2.id.layout_fail)
    View layoutFail;
    @BindView(R2.id.tv_error)
    TextView tvError;
    @BindView(R2.id.btn_loading_back)
    ImageButton btnLoadingBack;
    @BindView(R2.id.btn_error_back)
    ImageButton btnErrorBack;


    private NormalViewHolder viewHolder;

    private ImageRequest.RequestManager requestManager;

    private View.OnClickListener onBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener onRetryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getPresenter().onRetry();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_completed;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        getSystemBarManager().setStatusBarTintEnabled(false);
        getSystemBarManager().setNavigationBarTintEnabled(true);
        StatusBarUtil.changeStatusBar(getActivity().getWindow(), false, true);
        requestManager = ImageLoader.with(this);
        ivBackground.setOnClickListener(onRetryListener);
    }


    @Override
    public void loadBackImage(String url) {
        requestManager
                .load(url)
                .maxSize(100)
                .transform(new BlurTransformation(url))
                .placeholder(R.drawable.bg_color_9_shape)
                .error(R.drawable.bg_color_9_shape)
                .diskCacheStrategy(ImageRequest.DISK_SOURCE)
                .centerCrop()
                .into(ivBackground);
    }

    @Override
    public void update(LiveCompletedViewModel viewModel) {
        if (viewModel.isContribution()) {
            changeToContributionViewHolder();
        } else {
            changeToNormalViewHolder();
        }
        updateViewHolder(viewModel);
    }

    private void changeToNormalViewHolder() {
        if (viewHolder == null) {
            View view = vsNormal.inflate();
            viewHolder = new NormalViewHolder(view);
            viewHolder.setOnBackClickListener(onBackClickListener);
        }
    }

    private void changeToContributionViewHolder() {
        if (viewHolder == null) {
            View view = vsContribution.inflate();
            viewHolder = new ContributionViewHolder(view, requestManager);
            viewHolder.setOnBackClickListener(onBackClickListener);
        }
    }

    private void updateViewHolder(LiveCompletedViewModel viewModel) {
        viewHolder.onBindData(viewModel);
    }

    private void startLoadingAnim() {
        this.cancelLoadingAnim();
        AdditiveAnimator.animate(this.ivLoading).rotationBy(360.0F).setDuration(700L).setRepeatCount(-1).start();
    }

    private void cancelLoadingAnim() {
        AdditiveAnimator.cancelAnimations(this.ivLoading);
    }

    @Override
    public EmptyDisplayView getEmptyDisplayView() {
        return new EmptyDisplayView() {
            @Override
            public void showEmpty() {
                layoutFail.setVisibility(View.VISIBLE);
                layoutLoading.setVisibility(View.GONE);
                cancelLoadingAnim();
                tvError.setText("获取数据失败");
                ivBackground.setEnabled(true);
            }

            @Override
            public void showLoading(String s) {
                layoutFail.setVisibility(View.GONE);
                layoutLoading.setVisibility(View.VISIBLE);
                startLoadingAnim();
                ivBackground.setEnabled(false);
            }

            @Override
            public void showError(Throwable throwable) {
                layoutFail.setVisibility(View.VISIBLE);
                layoutLoading.setVisibility(View.GONE);
                cancelLoadingAnim();
                if(throwable instanceof NoNetworkErrorException){
                    tvError.setText("当前网络不可用，请检查网络后重试\n点击屏幕重新加载");
                }else {
                    tvError.setText(throwable.getMessage());
                }
                ivBackground.setEnabled(true);
            }

            @Override
            public void showContent() {
                layoutFail.setVisibility(View.GONE);
                layoutLoading.setVisibility(View.GONE);
                cancelLoadingAnim();
                ivBackground.setEnabled(false);
            }

            @Override
            public void setOnRetryListener(OnRetryListener onRetryListener) {

            }
        };
    }

    @Override
    public void onDestroyView() {
        cancelLoadingAnim();
        super.onDestroyView();
    }

    @OnClick(R2.id.btn_loading_back)
    void onLoadingBackClick(View view) {
        finish();
    }

    @OnClick(R2.id.btn_error_back)
    void onErrorBackClick(View view) {
        finish();
    }

    static class NormalViewHolder {
        @BindView(R2.id.tv_play_count)
        TextView tvPlayCount;
        @BindView(R2.id.btn_back)
        View btnBack;

        View.OnClickListener onBackClickListener;

        NormalViewHolder(View view) {
            ButterKnife.bind(this, view);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBackClickListener != null)
                        onBackClickListener.onClick(v);
                }
            });
        }

        public void setOnBackClickListener(View.OnClickListener onBackClickListener) {
            this.onBackClickListener = onBackClickListener;
        }

        public void onBindData(LiveCompletedViewModel viewModel) {
            tvPlayCount.setText(viewModel.getPlayCount());
        }
    }


    static class ContributionViewHolder extends NormalViewHolder {
        @BindView(R2.id.tv_fans_count)
        TextView tvFansCount;
        @BindView(R2.id.tv_gift_count)
        TextView tvGiftCount;
        @BindView(R2.id.tv_wcoin_count)
        TextView tvWcoinCount;
        @BindView(R2.id.tabLayout)
        MagicIndicator tabLayout;
        @BindView(R2.id.viewpager)
        ViewPager viewpager;

        CommonNavigator commonNavigator;

        ContributionPagerAdapter pagerAdapter;

        ContributionViewHolder(View view, ImageRequest.RequestManager requestManager) {
            super(view);
            pagerAdapter = new ContributionPagerAdapter();
            pagerAdapter.setRequestManager(requestManager);
            viewpager.setAdapter(pagerAdapter);
            initIndicator();
            tabLayout.setNavigator(commonNavigator);
            ViewPagerHelper.bind(tabLayout, viewpager);
        }

        @Override
        public void onBindData(LiveCompletedViewModel viewModel) {
            super.onBindData(viewModel);
            tvFansCount.setText(viewModel.getFansCount());
            tvGiftCount.setText(viewModel.getGiftCount());
            tvWcoinCount.setText(viewModel.getwCoinCount());
            pagerAdapter.setData(viewModel.getTabItemViewModels());
            commonNavigator.getAdapter().notifyDataSetChanged();
        }

        private void initIndicator() {
            commonNavigator = new CommonNavigator(tabLayout.getContext());
            commonNavigator.setScrollPivotX(0.65f);
            commonNavigator.setAdapter(new CommonNavigatorAdapter() {
                @Override
                public int getCount() {
                    if (pagerAdapter != null)
                        return pagerAdapter.getCount();
                    return 0;
                }

                @Override
                public IPagerTitleView getTitleView(Context context, final int index) {
                    ClipPagerTitleView simplePagerTitleView = new ClipPagerTitleView(context);
                    simplePagerTitleView.setText((String) pagerAdapter.getPageTitle(index));
                    simplePagerTitleView.setTextSize(DisplayUtil.convertDIP2PX(16));
                    simplePagerTitleView.setPadding(DisplayUtil.convertDIP2PX(10), 0, DisplayUtil.convertDIP2PX(10), 0);
                    simplePagerTitleView.setTextColor(context.getResources().getColor(com.whaley.biz.common.R.color.color12_alpha_50));
                    simplePagerTitleView.setClipColor(context.getResources().getColor(com.whaley.biz.common.R.color.color12));
                    simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (viewpager != null)
                                viewpager.setCurrentItem(index);
                        }
                    });
                    return simplePagerTitleView;
                }

                @Override
                public IPagerIndicator getIndicator(Context context) {
                    LinePagerIndicator indicator = new LinePagerIndicator(context);
                    indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                    indicator.setLineHeight(UIUtil.dip2px(context, 2.7));
                    indicator.setLineWidth(UIUtil.dip2px(context, 14));
                    indicator.setRoundRadius(UIUtil.dip2px(context, 1.3));
                    indicator.setStartInterpolator(new AccelerateInterpolator());
                    indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                    indicator.setColors(context.getResources().getColor(com.whaley.biz.common.R.color.color12));
                    indicator.setYOffset(DisplayUtil.convertDIP2PX(6));
                    return indicator;
                }
            });

        }

        private static class ContributionPagerAdapter extends PagerAdapter {

            private List<LiveCompletedViewModel.TabItemViewModel> tabItemViewModels;

            private ImageRequest.RequestManager requestManager;

            private int count;

            public void setData(List<LiveCompletedViewModel.TabItemViewModel> tabItemViewModels) {
                this.tabItemViewModels = tabItemViewModels;
                this.count = this.tabItemViewModels == null ? 0 : this.tabItemViewModels.size();
                notifyDataSetChanged();
            }

            public void setRequestManager(ImageRequest.RequestManager requestManager) {
                this.requestManager = requestManager;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                RecyclerView recyclerView = new RecyclerView(container.getContext());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
                recyclerView.setLayoutParams(layoutParams);
                LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                LiveCompletedViewModel.TabItemViewModel tabItemViewModel = tabItemViewModels.get(position);
                RecyclerViewAdapter adapter;
                switch (tabItemViewModel.getType()) {
                    case LiveCompletedViewModel.TabItemViewModel.TYPE_CONTRIBUTION_RANK:
                        LiveCompletedContributionAdapter liveCompletedContributionAdapter = new LiveCompletedContributionAdapter();
                        liveCompletedContributionAdapter.setRequestManager(requestManager);
                        adapter = liveCompletedContributionAdapter;
                        break;
                    default:
                        MemberRankAdapter memberRankAdapter = new MemberRankAdapter();
                        memberRankAdapter.setRequestManager(requestManager);
                        adapter = memberRankAdapter;
                        break;
                }
                recyclerView.setAdapter(adapter);
                adapter.setData(tabItemViewModel.getDatas());
                container.addView(recyclerView);
                return recyclerView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabItemViewModels.get(position).getTitle();
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return this.count;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        }
    }

    static class BlurTransformation implements Transformation {

        final String url;

        public BlurTransformation(String url) {
            this.url = url;
        }

        @Override
        public Bitmap transform(Bitmap resource, int outHeigt, int outWidth) {
            if (resource.isRecycled())
                return resource;
            Bitmap bitmap = resource;
            Bitmap blurBitmap = FastBlur.doBlur(bitmap, 8, false);
            if (blurBitmap != null) {
                return blurBitmap;
            }
            return resource;
        }

        @Override
        public String getKey() {
            return url + "_Blur_size_+" + 100 + "_radio_" + 8;
        }
    }

}
