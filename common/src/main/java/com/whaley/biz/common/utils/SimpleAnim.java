package com.whaley.biz.common.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.util.Property;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangZhi on 2017/8/12 19:02.
 */

public class SimpleAnim {

    final private List<Animator> animators = new ArrayList<>();

    final private AnimatorSet animatorSet = new AnimatorSet();

    private View target;

    private int repeatCount;

    private SimpleAnim(){

    }

    public static SimpleAnim with(View target){
        SimpleAnim simpleAnim = new SimpleAnim();
        simpleAnim.target = target;
        return simpleAnim;
    }

    public SimpleAnim alpha(float... values){
        return addAnim(View.ALPHA,values);
    }

    public SimpleAnim alphaFinal(float value){
        return alpha(target.getAlpha(),value);
    }

    public SimpleAnim translationX(float... values){
        return addAnim(View.TRANSLATION_X,values);
    }

    public SimpleAnim translationXFinal(float value){
        return translationX(target.getTranslationX(),value);
    }

    public SimpleAnim translationY(float... values){
        return addAnim(View.TRANSLATION_Y,values);
    }

    public SimpleAnim translationYFinal(float value){
        return translationY(target.getTranslationY(),value);
    }

    public SimpleAnim translationZ(float... values){
        return addAnim(View.TRANSLATION_Z,values);
    }

    public SimpleAnim translationZ(float value){
        return translationZ(target.getTranslationZ(),value);
    }

    public SimpleAnim scaleX(float... values){
        return addAnim(View.SCALE_X,values);
    }

    public SimpleAnim scaleXFinal(float value){
        return scaleX(target.getScaleX(),value);
    }

    public SimpleAnim scaleY(float... values){
        return addAnim(View.SCALE_Y,values);
    }

    public SimpleAnim scaleYFinal(float value){
        return scaleY(target.getScaleY(),value);
    }

    public SimpleAnim rotation(float... values){
        return addAnim(View.ROTATION,values);
    }

    public SimpleAnim rotationFinal(float value){
        return rotation(target.getRotation(),value);
    }

    public SimpleAnim rotationX(float... values){
        return addAnim(View.ROTATION_X,values);
    }

    public SimpleAnim rotationXFinal(float value){
        return rotationX(target.getRotationX(),value);
    }

    public SimpleAnim rotationY(float... values){
        return addAnim(View.ROTATION_Y,values);
    }

    public SimpleAnim rotationYFinal(float value){
        return rotationY(target.getRotationY(),value);
    }

    public SimpleAnim addAnim(Animator animator){
        animators.add(animator);
        return this;
    }

    public SimpleAnim addAnim(float... values){
        ValueAnimator animator = ValueAnimator.ofFloat(values);
        animators.add(animator);
        return this;
    }

    public SimpleAnim addAnim(String property,float... values){
        ObjectAnimator animator = ObjectAnimator.ofFloat(target,property,values);
        animators.add(animator);
        return this;
    }

    public SimpleAnim addAnim(Property property,float... values){
        ObjectAnimator animator = ObjectAnimator.ofFloat(target,property,values);
        animators.add(animator);
        return this;
    }

    public SimpleAnim repeatCount(final int repeatCount){
        this.repeatCount = repeatCount;
        if(repeatCount > 0){
            animatorSet.addListener(new AnimatorListenerAdapter() {
                int currentRepeatCount = -1;
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    currentRepeatCount++;
                    if(currentRepeatCount<repeatCount){
                        animation.start();
                    }
                }
            });
        }
        return this;
    }

    public SimpleAnim listener(Animator.AnimatorListener animatorListener){
        this.animatorSet.addListener(animatorListener);
        return this;
    }

    public SimpleAnim delay(long delay){
        this.animatorSet.setStartDelay(delay);
        return this;
    }

    public SimpleAnim duration(long duration){
        this.animatorSet.setDuration(duration);
        return this;
    }

    public SimpleAnim interpolator(TimeInterpolator interpolator){
        this.animatorSet.setInterpolator(interpolator);
        return this;
    }

    public Animator play(){
        animatorSet.playTogether(animators);
        animatorSet.start();
        return animatorSet;
    }




}
