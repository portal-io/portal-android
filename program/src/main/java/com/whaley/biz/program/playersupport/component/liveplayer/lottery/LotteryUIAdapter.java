package com.whaley.biz.program.playersupport.component.liveplayer.lottery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whaley.biz.common.utils.SimpleAnim;
import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.DisplayUtil;

import java.util.Iterator;
import java.util.List;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by YangZhi on 2017/8/10 17:56.
 */

public class LotteryUIAdapter extends ControlUIAdapter<LotteryController> {

    TextView tvCountDown;

    ImageButton btnClose;

    ImageView ivChest;

    AnimatorSet animatorSet;

    Animator.AnimatorListener animatorListener;

    View layout_lottery;

    boolean isBoxLotteryable;

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_player_lottery, parent, false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        tvCountDown = (TextView) view.findViewById(R.id.tv_countdown);
        btnClose = (ImageButton) view.findViewById(R.id.btn_close);
        ivChest = (ImageView) view.findViewById(R.id.iv_chest);
        layout_lottery = view.findViewById(R.id.layout_lottery);
//        getRootView().setAlpha(0f);
//        getRootView().setTranslationX(-getRootView().getMeasuredWidth());
        getRootView().setVisibility(View.GONE);
        getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onLotteryClick();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onLotteryCloseClick();
            }
        });;
    }

    @Override
    public void show(boolean anim) {
        AdditiveAnimator.cancelAnimations(getRootView());
        AdditiveAnimator.animate(getRootView())
                .translationX(0f)
                .alpha(1f)
                .addStartAction(new Runnable() {
                    @Override
                    public void run() {
                        if (getRootView() != null) {
                            getRootView().setVisibility(View.VISIBLE);
                        }
                        if (isBoxLotteryable) {
                            startBoxAnimator();
                        }
                    }
                })
                .start();
    }

    @Override
    public void hide(boolean anim) {
        int translationX = -getRootView().getMeasuredWidth();
        if (getController().isRight()) {
            translationX = DisplayUtil.screenW + getRootView().getMeasuredWidth();
        }
        AdditiveAnimator.cancelAnimations(getRootView());
        AdditiveAnimator.animate(getRootView())
                .translationX(translationX)
                .alpha(0f)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        if (getRootView() != null) {
                            getRootView().setVisibility(View.GONE);
                        }
                        cancelBoxAnim();
                    }
                })
                .start();
    }


    public void updateCountDown(CharSequence text) {
        cancelLotteryable();
        tvCountDown.setText(text);
        tvCountDown.setVisibility(View.VISIBLE);
        btnClose.setVisibility(View.VISIBLE);
    }

    public void updateToLotteryable() {
        tvCountDown.setText("");
        tvCountDown.setVisibility(View.GONE);
        btnClose.setVisibility(View.GONE);
        startBoxAnimator();
        isBoxLotteryable = true;
    }

    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }


    private void startBoxAnimator() {
        cancelBoxAnim();
        if (getRootView().getVisibility() != View.VISIBLE) {
            return;
        }
        final ObjectAnimator ivChestAnim = ObjectAnimator.ofFloat(ivChest, "rotation", 0, -15, 0, 15);
        ivChestAnim.setDuration(200);
        ivChestAnim.setRepeatCount(5);
        ObjectAnimator ivChestAnimEnlargeX = ObjectAnimator.ofFloat(ivChest, "scaleX", 1f, 0.9f, 0.9f, 1.2f);
        ivChestAnimEnlargeX.setDuration(300);
        ObjectAnimator ivChestAnimEnlargeY = ObjectAnimator.ofFloat(ivChest, "scaleY", 1f, 0.9f, 0.9f, 1.2f);
        ivChestAnimEnlargeY.setDuration(300);
        ObjectAnimator ivChestAnimShrinkX = ObjectAnimator.ofFloat(ivChest, "scaleX", 1.2f, 1f);
        ivChestAnimShrinkX.setDuration(100);
        ObjectAnimator ivChestAnimShrinkY = ObjectAnimator.ofFloat(ivChest, "scaleY", 1.2f, 1f);
        ivChestAnimShrinkY.setDuration(100);
        animatorSet = new AnimatorSet();
        animatorSet.play(ivChestAnimShrinkX)
                .with(ivChestAnimShrinkY)
                .after(ivChestAnim)
                .after(ivChestAnimEnlargeX)
                .after(ivChestAnimEnlargeY);
        animatorListener = new AnimatorListenerAdapter() {
            boolean isCancel;

            @Override
            public void onAnimationStart(Animator animation) {
                isCancel = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Log.d("onAnimationEnd isBoxAnimCanceled= "+isCancel);
                if (ivChest != null) {
                    AdditiveAnimator.cancelAnimations(ivChest);
                    AdditiveAnimator.animate(ivChest)
                            .rotation(0f)
                            .scale(1f)
                            .start();
                }
                if (!isCancel) {
                    animation.removeAllListeners();
                    animation.addListener(this);
                    animation.start();
                    animatorSet = (AnimatorSet) animation;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Log.d("onAnimationCancel");
                isCancel = true;
                if (ivChest != null) {
                    AdditiveAnimator.cancelAnimations(ivChest);
                    AdditiveAnimator.animate(ivChest)
                            .rotation(0f)
                            .scale(1f)
                            .start();
                }
            }
        };
        animatorSet.addListener(animatorListener);
        animatorSet.start();

    }

    private void cancelLotteryable() {
        cancelBoxAnim();
        isBoxLotteryable = false;
    }

    private void cancelBoxAnim() {
        if (animatorSet != null) {
            // Log.d("cancelBoxAnim");
            animatorSet.cancel();
            animatorListener.onAnimationCancel(animatorSet);
            animatorSet = null;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        cancelBoxAnim();
        if (ivChest != null) {
            AdditiveAnimator.cancelAnimations(ivChest);
        }
    }

    public void updateTopMargin(int marginTop) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) layout_lottery.getLayoutParams();
        layoutParams.topMargin = marginTop;
        layout_lottery.requestLayout();
    }

    public void updateRightAlignment() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT;
        layout_lottery.setLayoutParams(layoutParams);
    }
}
