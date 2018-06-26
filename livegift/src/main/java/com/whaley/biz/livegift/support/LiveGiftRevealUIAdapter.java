package com.whaley.biz.livegift.support;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.livegift.R;
import com.whaley.biz.livegift.model.GiftNoticeModle;
import com.whaley.biz.livegift.model.GiftState;
import com.whaley.biz.livegift.widget.GiftViewLayout;
import com.whaley.biz.livegift.widget.RedoubleTextView;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Author: qxw
 * Date:2017/10/20
 * Introduction:
 */

public class LiveGiftRevealUIAdapter extends BaseUIAdapter<LiveGiftRevealController> {

    ViewStub viewStub;
    View viewGiftLayout;
    GiftViewLayout giftViewLayout1;
    GiftViewLayout giftViewLayout2;
    ImageRequest.RequestManager requestManager;
    final int STAY_TIME = 1500;

    @Override
    protected View initView(ViewGroup parent) {
        ViewStub viewStub = new ViewStub(parent.getContext(), R.layout.layout_gift_reveal);
        viewStub.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.convertDIP2PX(90)));
        return viewStub;
    }

    @Override
    public View getRootView() {
        return viewGiftLayout == null ? super.getRootView() : viewGiftLayout;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }

    public boolean changeToView() {
        if (viewGiftLayout == null) {
            if(viewStub==null || viewStub.getParent() ==null) {
                return false;
            }
            viewGiftLayout = viewStub.inflate();
            if (getController().isHideGift) {
                viewGiftLayout.setAlpha(0f);
            }
            giftViewLayout1 = (GiftViewLayout) viewGiftLayout.findViewById(R.id.view_gift1);
            giftViewLayout2 = (GiftViewLayout) viewGiftLayout.findViewById(R.id.view_gift2);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewGiftLayout.getLayoutParams();
            params.topMargin = getController().getGiftOrdinaryHeight();
            viewGiftLayout.getLayoutParams();
        }
        if (requestManager == null) {
            requestManager = ImageLoader.with(getActivity());
        }
        return true;
    }

    @Override
    public void destroy() {
        if (viewGiftLayout != null) {
            AdditiveAnimator.cancelAnimations(viewGiftLayout);
        }
        if (giftViewLayout1 != null) {
            AdditiveAnimator.cancelAnimations(giftViewLayout1);
        }
        if (giftViewLayout2 != null) {
            AdditiveAnimator.cancelAnimations(giftViewLayout2);
        }
        requestManager = null;
    }


    public void setViewGift(boolean isGift2) {
        if (!changeToView())
            return;
        GiftNoticeModle gift = getController().getGiftMode(isGift2);
        if (gift == null) {
            return;
        }
        if (isGift2) {
            giftViewLayout2.setName(gift.getNickName());
            giftViewLayout2.setText(gift.getTextConent());
            requestManager.load(gift.getUserHeadUrl()).error(R.drawable.bg_placeholeder_circle_shape).placeholder(R.drawable.bg_placeholeder_circle_shape).circle().small().centerCrop().into(giftViewLayout2.getIvAvatar());
            requestManager.load(gift.getGiftIcon()).error(0).placeholder(0).small().centerCrop().into(giftViewLayout2.getIvGift());
            return;
        }
        giftViewLayout1.setName(gift.getNickName());
        giftViewLayout1.setText(gift.getTextConent());
        requestManager.load(gift.getUserHeadUrl()).error(R.drawable.bg_placeholeder_circle_shape).placeholder(R.drawable.bg_placeholeder_circle_shape).circle().small().centerCrop().into(giftViewLayout1.getIvAvatar());
        requestManager.load(gift.getGiftIcon()).error(0).placeholder(0).small().centerCrop().into(giftViewLayout1.getIvGift());
    }

    private void hideRedouble(boolean isGift2) {
        if (!changeToView())
            return;
        if (isGift2) {
            giftViewLayout2.hideRedouble();
        } else {
            giftViewLayout1.hideRedouble();
        }
    }

    public void setBatter(boolean isGift2, int duplicate) {
        if (!changeToView())
            return;
        if (isGift2) {
            giftViewLayout2.showRedouble(String.valueOf(duplicate + 1));
        } else {
            giftViewLayout1.showRedouble(String.valueOf(duplicate + 1));
        }
    }

    public void updateGift(boolean isGift2) {
        if (!changeToView())
            return;
        hideRedouble(isGift2);
        setViewGift(isGift2);
        stayGift(isGift2, getAnimatorView(isGift2), STAY_TIME);
    }

    public void batter(boolean isGift2, int duplicate) {
        if (!changeToView())
            return;
        setBatter(isGift2, duplicate);
        stayGift(isGift2, getAnimatorView(isGift2), STAY_TIME);
    }

    public void showGift(boolean isGift2) {
        if (!changeToView())
            return;
        showGift(isGift2, 0);
    }

    public void showGift(final boolean isGift2, int delay) {
        if (!changeToView())
            return;
        changeToView();
        setViewGift(isGift2);
        final View view = getAnimatorView(isGift2);
        AdditiveAnimator.cancelAnimations(view);
        AdditiveAnimator.animate(view)
                .alpha(1f)
                .setDuration(200)
                .setStartDelay(delay)
                .addStartAction(new Runnable() {
                    @Override
                    public void run() {
                        getController().setState(isGift2, GiftState.SHOW_STATE);
                        view.setVisibility(View.VISIBLE);
                    }
                })
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        if (!wasCancelled) {
                            getController().setState(isGift2, GiftState.STAY_STATE);
                            stayGift(isGift2, view, STAY_TIME);
                        }
                    }
                })
                .start();
    }

    public void stayGift(final boolean isGift2, View view, int delay) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null) {
                    return;
                }
                if (getController().nextGift(isGift2)) {
                    getController().setState(isGift2, GiftState.HIDE_STATE);
                    hideGift(isGift2);
                }
            }
        }, delay);
    }


    public void hideGift(final boolean isGift2) {
        final View view = getAnimatorView(isGift2);
        AdditiveAnimator.cancelAnimations(view);
        AdditiveAnimator additiveAnimator = AdditiveAnimator.animate(view);
        additiveAnimator.alpha(0f)
                .setDuration(500)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        if (!wasCancelled) {
                            getController().endGift(isGift2);
                            view.setVisibility(View.GONE);
                            hideRedouble(isGift2);
                        }
                    }
                }).start();

    }


    private View getAnimatorView(boolean isGift2) {
        return isGift2 ? giftViewLayout2 : giftViewLayout1;
    }


    public void updateHeight(int height) {
        if (!changeToView())
            return;
        changeToView();
        AdditiveAnimator.cancelAnimations(viewGiftLayout);
        AdditiveAnimator additiveAnimator = AdditiveAnimator.animate(viewGiftLayout);
        additiveAnimator.topMargin(height)
                .start();
    }

    public void hideGiftTemp() {
        AdditiveAnimator.cancelAnimations(getRootView());
        AdditiveAnimator.animate(getRootView())
                .translationX(-getRootView().getMeasuredWidth())
                .alpha(0f)
                .start();
    }

    public void showGiftTemp() {
        AdditiveAnimator.cancelAnimations(getRootView());
        AdditiveAnimator.animate(getRootView())
                .translationX(0f)
                .alpha(1f)
                .start();
    }
}
