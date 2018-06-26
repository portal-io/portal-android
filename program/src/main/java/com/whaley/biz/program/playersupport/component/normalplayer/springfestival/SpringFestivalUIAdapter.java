package com.whaley.biz.program.playersupport.component.normalplayer.springfestival;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.core.appcontext.AppContextProvider;

import org.greenrobot.eventbus.EventBus;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by dell on 2018/1/23.
 */

public class SpringFestivalUIAdapter extends ControlUIAdapter<SpringFestivalController> {

    TextView tvCountDown;

    ImageView ivChest;

    AnimatorSet animatorSet;

    Animator.AnimatorListener animatorListener;

    View layout_lottery;

    boolean isBoxLotteryable;

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sweepstakes, parent, false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        tvCountDown = (TextView) view.findViewById(R.id.tv_countdown);
        ivChest = (ImageView) view.findViewById(R.id.iv_chest);
        layout_lottery = view.findViewById(R.id.layout_lottery);
        getRootView().setVisibility(View.GONE);
        getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onLotteryClick();
            }
        });
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
    }

    public void updateToLotteryable() {
        tvCountDown.setText("");
        tvCountDown.setVisibility(View.GONE);
        startBoxAnimator();
        isBoxLotteryable = true;
    }

    public void resetLotteryable(){
        cancelLotteryable();
        tvCountDown.setText("");
        tvCountDown.setVisibility(View.GONE);
    }

    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
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
                if (ivChest != null) {
                    ivChest.setRotation(0);
                    ivChest.setScaleX(1f);
                    ivChest.setScaleY(1f);
                }
                if(!isCancel){
                    repeatAnimation();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isCancel = true;
                if (ivChest != null) {
                    ivChest.setRotation(0);
                    ivChest.setScaleX(1f);
                    ivChest.setScaleY(1f);
                }
            }
        };
        animatorSet.addListener(animatorListener);
        animatorSet.start();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==1){
                startBoxAnimator();
            }
            return false;
        }
    });

    private void repeatAnimation(){
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
    }

    private void cancelLotteryable() {
        cancelBoxAnim();
        isBoxLotteryable = false;
    }

    private void cancelBoxAnim() {
        if (animatorSet != null) {
            animatorSet.cancel();
            animatorListener.onAnimationCancel(animatorSet);
            animatorSet = null;
        }
    }
    public void removeBox(){
        resetLotteryable();
        ivChest.setVisibility(View.GONE);
    }
    public void showDialog(){
        DialogUtil.showDialogCustomConfirm(getContext(),
                AppContextProvider.getInstance().getContext().getString(R.string.spring_leave_play),
                null,
                AppContextProvider.getInstance().getContext().getString(R.string.spring_leave_play_no),
                AppContextProvider.getInstance().getContext().getString(R.string.spring_leave_play_yes),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getController().goWeb();
                    }
                }, false, true);
    }

    @Override
    public void destroy() {
        super.destroy();
        cancelBoxAnim();
        if (ivChest != null) {
            AdditiveAnimator.cancelAnimations(ivChest);
        }
    }

}

