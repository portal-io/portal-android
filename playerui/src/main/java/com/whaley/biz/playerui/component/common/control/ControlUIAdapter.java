package com.whaley.biz.playerui.component.common.control;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.whaley.biz.playerui.component.BaseUIAdapter;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by YangZhi on 2017/8/2 14:33.
 */

public abstract class ControlUIAdapter<CONTROLLER extends ControlController> extends BaseUIAdapter<CONTROLLER>{

    private AnimatorSet changeVisibleAnimtorset;

    public abstract void show(boolean anim);

    public abstract void hide(boolean anim);


    protected void startAnim(float finalTransX,float finalTransY,float finalAlpha){
        startAnim(finalTransX,finalTransY,finalAlpha,null);
    }

    protected void startAnim(float finalTransX, float finalTransY, float finalAlpha, Animator.AnimatorListener listener){
        cancelAnim();
        AdditiveAnimator animator =AdditiveAnimator.animate(getRootView())
                .translationX(finalTransX)
                .translationY(finalTransY)
                .alpha(finalAlpha);
        if(listener!=null)
            animator.addListener(listener);
        animator.start();
    }

    protected void cancelAnim(){
        if(getRootView()!=null) {
            AdditiveAnimator.cancelAnimations(getRootView());
        }
    }

    protected long getRootAnimDuration(){
        return 300;
    }

    @Override
    public void destroy() {
        cancelAnim();
    }
}
