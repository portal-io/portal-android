package com.whaley.biz.setting.ui.view;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.common.widget.SettingViewLayout;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.model.user.UserModel;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.ui.presenter.MePresenter;
import com.whaley.biz.setting.ui.viewmodel.MeViewModel;
import com.whaley.biz.setting.widget.BounceScrollView;
import com.whaley.biz.setting.widget.SimpleAnimatorListener;
import com.whaley.biz.setting.widget.ZZoomImageView;
import com.whaley.core.image.Animator;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/7/31.
 */

@Route(path = SettingRouterPath.ME)
public class MeFragment extends BaseMVPFragment<MePresenter> implements MeView {


    @BindView(R2.id.me_layout)
    LinearLayout meLayout;
    ImageRequest.RequestManager requestManager;
    @BindView(R2.id.btn_login)
    TextView btnLogin;
    @BindView(R2.id.btn_register)
    TextView btnRegister;
    @BindView(R2.id.layout_unlogin)
    RelativeLayout layoutUnlogin;
    @BindView(R2.id.iv_avater)
    ImageView ivAvater;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.btn_info)
    TextView btnInfo;
    @BindView(R2.id.layout_logined)
    RelativeLayout layoutLogined;
    @BindView(R2.id.layout_card)
    LinearLayout layoutCard;
    @BindView(R2.id.layout_scroll)
    BounceScrollView layoutScroll;
    @BindView(R2.id.view_bg)
    ZZoomImageView viewBg;

    private int maxDeltaY = DisplayUtil.convertDIP2PX(150);
    private ValueAnimator mAnimator;
    private float finalY;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected MePresenter onCreatePresenter() {
        return new MePresenter(this);
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        requestManager = ImageLoader.with(this);
        super.setViews(view, savedInstanceState);
        layoutScroll.setMaxDeltaY(maxDeltaY);
        layoutScroll.setScrollListener(new BounceScrollView.ScrollListener() {
            @Override
            public void onScrolled(int deltaY) {
                float t = layoutCard.getTranslationY()-deltaY/3;
                if(t<0){
                    t = 0;
                }
                if(t != layoutCard.getTranslationY()) {
                    layoutCard.setTranslationY(t);
                }
                float scale = 1f - 0.2f * deltaY / maxDeltaY;
                viewBg.setScaleMatrix(scale);
            }
            @Override
            public void onReleased() {
                if (mAnimator == null) {
                    mAnimator = ValueAnimator.ofFloat(0f, 1f);
                    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float animatorValue = (float) animation.getAnimatedValue();
                            float a = finalY - finalY *animatorValue;
                            float deltaY = layoutCard.getTranslationY() - a;
                            layoutCard.setTranslationY(a);
                            float scale = 1f - 0.6f * deltaY / maxDeltaY;
                            viewBg.setScaleMatrix(scale);
                        }
                    });
                    mAnimator.addListener(new SimpleAnimatorListener() {
                        @Override
                        public void onAnimationEnd(android.animation.Animator animation) {
                            super.onAnimationEnd(animation);
                            layoutCard.setTranslationY(0);
                            viewBg.setScaleMatrix(0.5f);
                        }

                        @Override
                        public void onAnimationCancel(android.animation.Animator animation) {
                            super.onAnimationCancel(animation);
                            layoutCard.setTranslationY(0);
                            viewBg.setScaleMatrix(0.5f);
                        }
                    });
                    mAnimator.setDuration(500);
                }
                if (mAnimator.isStarted()) {
                    mAnimator.cancel();
                }
                finalY = layoutCard.getTranslationY();
                mAnimator.start();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        hideStatusBar();
    }

    @Override
    public void updateLoginStatus() {
        if (getPresenter().getMeRepository().isLogin()) {
            if (ivAvater == null) {
                return;
            }
            ivAvater.setVisibility(View.VISIBLE);
            UserModel userModel = getPresenter().getMeRepository().getUserModel();
            if (!TextUtils.isEmpty(userModel.getAvatar())) {
                requestManager.load(userModel.getAvatar())
                        .notInitBefore()
                        .skipMemoryCache(true)
                        .placeholder(R.mipmap.head).error(R.mipmap.head)
                        .animate(new Animator() {
                            @Override
                            public void animate(View view) {
                                //nothing to do
                            }
                        })
                        .diskCacheStrategy(ImageRequest.DISK_RESULT).circle().centerCrop().into(ivAvater);
            }
            if (!TextUtils.isEmpty(userModel.getNickname())) {
                tvName.setText(userModel.getNickname());
            }
            layoutLogined.setVisibility(View.VISIBLE);
            layoutUnlogin.setVisibility(View.GONE);
        } else {
            ivAvater.setImageResource(R.mipmap.head);
            tvName.setText("");
            layoutLogined.setVisibility(View.GONE);
            layoutUnlogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateList() {
        List<MeViewModel> meViewModels = getPresenter().getMeRepository().getMeViewModels();
        for (final MeViewModel meViewModel : meViewModels) {
            SettingViewLayout settingViewLayout = (SettingViewLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_setting, null);
            settingViewLayout.setName(meViewModel.getName());
            settingViewLayout.setText(meViewModel.getText());
            settingViewLayout.setRightPic(meViewModel.getRightPic());
            settingViewLayout.setLeftPic(meViewModel.getLeftPic());
            settingViewLayout.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onClickItem(meViewModel.getType());
                }
            });
            meLayout.addView(settingViewLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (meViewModel.isDivederBelow()) {
                View view = new View(getContext());
                view.setBackgroundResource(R.color.color10);
                meLayout.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.convertDIP2PX(10)));
            }
        }
    }

    @Override
    public void updateCardList() {
        List<MeViewModel> meViewModels = getPresenter().getMeRepository().getMeCardViewModels();
        for (final MeViewModel meViewModel : meViewModels) {
            View cardItemLayout = LayoutInflater.from(getContext()).inflate(R.layout.me_card_item, null);
            cardItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onClickItem(meViewModel.getType());
                }
            });
            ImageView pic = (ImageView) cardItemLayout.findViewById(R.id.iv_card_pic);
            TextView name = (TextView) cardItemLayout.findViewById(R.id.tv_card_name);
            pic.setImageResource(meViewModel.getLeftPic());
            name.setText(meViewModel.getName());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            cardItemLayout.setLayoutParams(lp);
            layoutCard.addView(cardItemLayout);
        }
    }

    @Override
    public void hideStatusBar() {
        if (getActivity() != null && isFragmentVisible()) {
            StatusBarUtil.changeStatusBar(getActivity().getWindow(), false, true);
        }
    }

    @OnClick(R2.id.btn_login)
    void login() {
        getPresenter().onClickLogin();
    }

    @OnClick(R2.id.btn_register)
    void register() {
        getPresenter().onClickRegister();
    }

    @OnClick(R2.id.btn_info)
    void logined() {
        getPresenter().onClickNickname();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requestManager != null)
            ImageLoader.resumeRequests(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (requestManager != null)
            ImageLoader.pauseRequests(this);
    }

    @Override
    public void onDestroyView() {
        if (mAnimator != null) {
            if (mAnimator.getListeners() != null) {
                mAnimator.getListeners().clear();
            }
            if (mAnimator.isStarted()) {
                mAnimator.cancel();
            }
            mAnimator = null;
        }
        super.onDestroyView();
    }

}
