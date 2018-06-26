package com.whaley.biz.setting.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.whaley.core.utils.DisplayUtil;

/**
 * Created by dell on 2017/10/11.
 */

public class BounceScrollView extends ScrollView {

    private float lastX = 0;
    private float lastY = 0;
    private float currentX = 0;
    private float currentY = 0;
    private float distanceX = 0;
    private float distanceY = 0;

    private boolean upDownSlide;
    private boolean isCount;
    private ValueAnimator mAnimator;
    private boolean isMoved;
    private float finalY;

    private int maxDeltaY = DisplayUtil.convertDIP2PX(120);
    private View child;
    private float y;

    public BounceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            child = getChildAt(0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        currentX = ev.getX();
        currentY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                distanceX = currentX - lastX;
                distanceY = currentY - lastY;
                if (Math.abs(distanceX) < Math.abs(distanceY) && distanceY > 12) {
                    upDownSlide = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        lastX = currentX;
        lastY = currentY;
        if (upDownSlide && child != null) commOnTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if (isMoved) {
                    animation();
                    isCount = false;
                }
                clear();
                break;
            case MotionEvent.ACTION_MOVE:
                if(mAnimator!=null&&mAnimator.isStarted()){
                    mAnimator.cancel();
                }
                final float preY = y;
                float nowY = ev.getY();
                int deltaY = (int) (preY - nowY);
                if (!isCount) {
                    deltaY = 0;
                }
                y = nowY;
                if (isNeedMove()) {
                    isMoved = true;
                    float t = getTranslationY()-deltaY/2;
                    if(t<0){
                        t = 0;
                    }else if(t > maxDeltaY){
                        t = maxDeltaY;
                    }
                    if(t != getTranslationY()) {
                        setTranslationY(t);
                        if (scrollListener != null)
                            scrollListener.onScrolled(deltaY);
                    }
                }
                isCount = true;
                break;
            default:
                break;
        }
    }

    public void animation() {
        if(mAnimator==null) {
            mAnimator = ValueAnimator.ofFloat(0f,1f);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatorValue = (float) animation.getAnimatedValue();
                    float a = finalY - finalY *animatorValue;
                    setTranslationY(a);
                }
            });
            mAnimator.addListener(new SimpleAnimatorListener(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setTranslationY(0);
                    isMoved=false;
                }
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    setTranslationY(0);;
                    isMoved=false;
                }
            });
            mAnimator.setDuration(500);
        }
        if(mAnimator.isStarted()){
            mAnimator.cancel();
        }
        finalY = getTranslationY();
        mAnimator.start();
        if(scrollListener!=null)
            scrollListener.onReleased();
    }

    private boolean isNeedMove() {
        int offset = child.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

    private void clear() {
        lastX = 0;
        lastY = 0;
        distanceX = 0;
        distanceY = 0;
        upDownSlide = false;
    }

    private ScrollListener scrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public void setMaxDeltaY(int maxDeltaY) {
        this.maxDeltaY = maxDeltaY;
    }

    public interface ScrollListener{
        void onScrolled(int deltaY);
        void onReleased();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mAnimator!=null){
            if(mAnimator.getListeners()!=null) {
                mAnimator.getListeners().clear();
            }
            if (mAnimator.isStarted()) {
                mAnimator.cancel();
            }
            mAnimator = null;
        }
    }

}
