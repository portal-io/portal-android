package com.whaley.biz.program.ui.follow;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.common.ui.TabIndicatorFragment;
import com.whaley.biz.common.ui.presenter.TabIndicatorPresenter;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.common.widget.emptylayout.EmptyDisplayLayout;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.follow.presenter.PublisherDetailPresenter;
import com.whaley.biz.program.ui.uimodel.PublisherDetailViewModel;
import com.whaley.biz.program.utils.StringUtil;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.titlebar.TitleBar;
import com.yz.pullable.PullBehavior;
import com.yz.pullable.PullableLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date:2017/8/16
 * Introduction:
 */

@Route(path = ProgramRouterPath.PUBLISHER_DETAIL)
public class PublisherDetailFragment extends TabIndicatorFragment implements PublisherView {


    PublisherDetailPresenter mPresenter;

    @BindView(R2.id.header)
    ImageView header;
    @BindView(R2.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R2.id.fl_avatar)
    FrameLayout flAvatar;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_fans_num)
    TextView tvFansNum;
    @BindView(R2.id.tv_info)
    TextView tvInfo;
    @BindView(R2.id.tabLayout)
    MagicIndicator tabLayout;
    @BindView(R2.id.separator)
    View separator;
    @BindView(R2.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R2.id.viewpager)
    ViewPager viewpager;
    @BindView(R2.id.pullableLayout)
    PullableLayout pullableLayout;
    @BindView(R2.id.btn_follow)
    Button btnFollow;

    @BindView(R2.id.emptyLayout)
    EmptyDisplayLayout emptyLayout;
    ImageRequest.RequestManager requestManager;

    @BindView(R2.id.shade_toolbar)
    RelativeLayout shadeToolbar;
    @BindView(R2.id.btn_back)
    ImageView btnBack;
    @BindView(R2.id.tv_name_shadow)
    TextView tvNameShadow;
    @BindView(R2.id.title_separator)
    View titleSeparator;

    @OnClick(R2.id.btn_follow)
    void onFollowClick() {
        mPresenter.onFollowClick();
    }

    @OnClick(R2.id.btn_back)
    void onBackClick() {
        mPresenter.finish();
    }

    @Override
    protected TabIndicatorPresenter onCreatePresenter() {
        mPresenter = new PublisherDetailPresenter(this);
        return mPresenter;
    }

    private SystemBarTintManager getSystemBarTintManager() {
        return ((CommonActivity) getActivity()).getSystemBarManager();
    }

    ObjectAnimator animator;
    boolean isTitleOnShow = true;
    private boolean isWhite;
    private int headerHeight;

    private void showTitle() {
        if (!isTitleOnShow) {
            isTitleOnShow = true;
            titleSeparator.setVisibility(View.VISIBLE);
            btnBack.setImageResource(R.drawable.back_black_selector);
            startAnimator(1f);
        }
    }

    private void hideTitle() {
        if (isTitleOnShow) {
            isTitleOnShow = false;
            titleSeparator.setVisibility(View.INVISIBLE);
            btnBack.setImageResource(R.drawable.back_white_selector);
            startAnimator(0f);
        }
    }

    private void startAnimator(float finalAlpha) {
        cancelAnimator();
        animator = ObjectAnimator.ofFloat(tvNameShadow, "alpha", tvNameShadow.getAlpha(), finalAlpha);
        animator.start();
    }

    private void cancelAnimator() {
        if (animator != null && animator.isStarted()) {
            animator.cancel();
        }
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
        headerHeight = DisplayUtil.convertDIP2PX(48) + getSystemBarTintManager().getConfig().getStatusBarHeight();
        requestManager = ImageLoader.with(this);
//            viewTitlebar.setPadding(0, getSystemBarManager().getConfig().getStatusBarHeight(), 0, 0);
        FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) shadeToolbar.getLayoutParams();
        contentParams.topMargin = getSystemBarManager().getConfig().getStatusBarHeight();
        shadeToolbar.setLayoutParams(contentParams);
        pullableLayout.getSetting().setOverScrollEnable(false);
        pullableLayout.addBehavior(new PullBehavior() {
            @Override
            public void onPull(PullableLayout pullableLayout, int transHeight) {
                int range = header.getBottom() - headerHeight;
                if (Math.abs(transHeight) < range) {
                    if (isWhite) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        StatusBarUtil.setTransparentFullStatusBar(getActivity().getWindow(), getSystemBarTintManager(), false);
                        isWhite = false;
                    }
                    hideTitle();
                    separator.setVisibility(View.INVISIBLE);
                } else {
                    if (!isWhite) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        StatusBarUtil.setWhiteFullStatusBar(getActivity().getWindow(), getSystemBarTintManager());
                        isWhite = true;
                    }
                    showTitle();
                    separator.setVisibility(View.VISIBLE);
                }

                float percent = Math.abs(transHeight) * 1.0f / range;
                if (percent > 1.0f) {
                    percent = 1.0f;
                }
                if (percent < 0) {
                    percent = 0;
                }
                ArgbEvaluator argbEvaluator = new ArgbEvaluator();
                int color = (int) argbEvaluator.evaluate(percent, Color.TRANSPARENT, Color.WHITE);
                shadeToolbar.setBackgroundColor(color);
            }

            @Override
            public void onPullEnd(PullableLayout pullableLayout) {

            }

            @Override
            public void onOverPullDown(PullableLayout pullableLayout, int overPullHeight) {

            }

            @Override
            public void startFling(View target, int velocityX, int velocityY) {

            }

        });

        pullableLayout.setChildScrollUpCallback(new PullableLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(PullableLayout parent, @Nullable View child) {
                View view = getCurrentChild();
                return view != null && ViewCompat.canScrollVertically(
                        view.findViewById(R.id.recycleView), -1);
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mPresenter.onSelectPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        mPresenter.onSelectPage(0);
        updateHeaderHeight();
        hideTitle();
    }

    ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (pullableLayout != null) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) viewpager.getLayoutParams();
                layoutParams.topMargin = rlHeader.getMeasuredHeight();
                viewpager.requestLayout();
                pullableLayout.getSetting().setMaxHeadScrollDistance(tvInfo.getBottom() - headerHeight);
                pullableLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    };

    private void updateHeaderHeight() {
        pullableLayout.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        pullableLayout.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }

    /**
     * Return the current programs recyclerView, so that PullableLayout can know its scroll
     * status.
     */
    public View getCurrentChild() {
        if (viewpager != null && viewpager.getChildCount() > viewpager.getCurrentItem()) {
            return viewpager.getChildAt(viewpager.getCurrentItem());
        }
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_publisher_detail;
    }

    @Override
    public void updateTabs() {
        super.updateTabs();
        PublisherDetailViewModel model = mPresenter.getViewModel();
        if (model == null)
            return;
        requestManager.load(model.getBgPic()).into(header);
        requestManager.load(model.getHeadPic()).circle().placeholder(R.mipmap.default_circle_4).error(R.mipmap.default_circle_4).into(ivAvatar);
        tvNameShadow.setText(model.getName());
        tvName.setText(model.getName());
        tvInfo.setText(model.getInfo());
        updateFollowStatus(false);
        updateHeaderHeight();
    }

    @Override
    public void updateFollowStatus(boolean isUpdate) {
        PublisherDetailViewModel model = mPresenter.getViewModel();
        btnFollow.setSelected(!model.isFollowed());
        if (model.isFollowed()) {
            model.setFans(model.getFans() + (isUpdate ? 1 : 0));
            btnFollow.setText("√已关注");
            tvFansNum.setText(String.format(Locale.CHINESE, "%s粉丝",
                    StringUtil.getCuttingCount(model.getFans())));
        } else {
            model.setFans(model.getFans() - (isUpdate ? 1 : 0));
            btnFollow.setText("+关注");
            tvFansNum.setText(
                    String.format(Locale.CHINESE, "%s粉丝", StringUtil.getCuttingCount(model.getFans())));
        }
    }
}
