package com.whaley.biz.program.widget;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.titlebar.ITitleBar;
import com.yz.pullable.PullBehavior;
import com.yz.pullable.PullableLayout;

/**
 * Created by yangzhi on 17/1/12.
 */

public class PullBehaviorImpl implements PullBehavior {

    private static final String TAG = "Pullable";

    private View view;
    private ITitleBar titleBar;
    private ImageView ivPlayer;
    private int minHeight = -1;

    private int minWidth = -1;


    private float startPlayerY = -1;

    int lastPullHeight = 0;

    ObjectAnimator animator;


    boolean isTitleOnShow;
    boolean isShowPlayer;


    private PullBehaviorListener pullBehaviorListener;

    public void setPullBehaviorListener(PullBehaviorListener pullBehaviorListener) {
        this.pullBehaviorListener = pullBehaviorListener;
    }

    public void setShowPlayer(boolean isShowPlayer) {
        this.isShowPlayer = isShowPlayer;
        if (ivPlayer == null) {
            return;
        }
        if (isShowPlayer) {
            ivPlayer.setVisibility(View.VISIBLE);
        } else {
            ivPlayer.setVisibility(View.INVISIBLE);
        }
    }

    public PullBehaviorImpl(View view, ITitleBar titleBar, ImageView ivPlayer) {
        this.view = view;
        this.titleBar = titleBar;
        this.ivPlayer = ivPlayer;
        init();
    }

    private void init() {
//        titleBar.setTitleBarAlpha(0);
        isTitleOnShow = true;
        setTitleBarBg(0);
//        titleBar.setTitleTextBold();
        titleBar.setLeftIcon(R.drawable.back_white_selector);
        hideTitle();
    }

    @Override
    public void onPullEnd(PullableLayout pullableLayout) {

    }

    @Override
    public void onOverPullDown(PullableLayout pullableLayout, int overPullHeight) {
        initializeData();
        changeLargerViewXY(overPullHeight);
        changeImageWH(overPullHeight);
    }

    @Override
    public void onPull(PullableLayout pullableLayout, int transHeight) {
        initializeData();
        changeSmallViewXY(transHeight);
    }

    @Override
    public void startFling(View target, int velocityX, int velocityY) {
        if (target != null && target instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) target;
            recyclerView.fling(velocityX, velocityY);
        }
    }


    private void initializeData() {
        if (minHeight == -1) {
            minHeight = view.getMeasuredHeight();
            minWidth = view.getMeasuredWidth();
            startPlayerY = ivPlayer.getY();
        }
    }

    private void changeLargerViewXY(int overPullHeight) {
        ivPlayer.setY(startPlayerY + overPullHeight);
    }

    private void changeImageWH(int offsetWH) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.height = minHeight + offsetWH;
        layoutParams.width = minWidth + offsetWH;
        layoutParams.topMargin = -offsetWH;
        layoutParams.leftMargin = -offsetWH / 2;
        view.requestLayout();
    }

    private void changeSmallViewXY(int offsetY) {
        if (lastPullHeight == offsetY)
            return;
        lastPullHeight = offsetY;
        offsetY = -offsetY;

        float scale = (startPlayerY + offsetY) / startPlayerY;
        if (isShowPlayer) {
            float ivPlayerScale = scale;
            if (ivPlayerScale < 0) {
                ivPlayerScale = 0;
                ivPlayer.setVisibility(View.INVISIBLE);
            } else {
                ivPlayer.setVisibility(View.VISIBLE);
            }
            if (ivPlayerScale > 1) {
                ivPlayerScale = 1;
            }
            ivPlayer.setScaleX(ivPlayerScale);
            ivPlayer.setScaleY(ivPlayerScale);
            ivPlayer.setY(startPlayerY + offsetY);
        }
        setTitleBarBg(1 - scale);
        if (scale < 0.2) {
            if (pullBehaviorListener != null) {
                pullBehaviorListener.isWhite(false);
            }
            showTitle();

        } else {
            if (pullBehaviorListener != null) {
                pullBehaviorListener.isWhite(true);
            }

            hideTitle();
        }
    }


    private void setTitleBarBg(float percent) {
        if (percent > 1) {
            percent = 1;
        }
        if (percent < 0) {
            percent = 0;
        }
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        int color = (int) argbEvaluator.evaluate(percent, Color.TRANSPARENT, Color.WHITE);
        titleBar.setBackgroundColor(color);
    }

    public void showTitle() {
        if (!isTitleOnShow) {
            isTitleOnShow = true;
            titleBar.showBottomLine();
            titleBar.setLeftIcon(R.drawable.back_black_selector);
            titleBar.setRightIcon(R.drawable.share_black_selector);
            startAnimator(1f);
        }
    }

    public void hideTitle() {
        if (isTitleOnShow) {
            isTitleOnShow = false;
            titleBar.hideBottomLine();
            titleBar.setLeftIcon(R.drawable.back_white_selector);
            titleBar.setRightIcon(R.drawable.share_white_selector);
            startAnimator(0f);
        }
    }

    private void startAnimator(float finalAlpha) {
        cancelAnimator();
        animator = ObjectAnimator.ofFloat(titleBar.getCenterView(), "alpha", titleBar.getCenterView().getAlpha(), finalAlpha);
        animator.start();
    }

    private void cancelAnimator() {
        if (animator != null && animator.isStarted()) {
            animator.cancel();
        }
    }

    public interface PullBehaviorListener {
        void isWhite(boolean iswhite);
    }

}
