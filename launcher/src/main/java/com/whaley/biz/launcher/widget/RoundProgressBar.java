package com.whaley.biz.launcher.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;

import com.snailvr.manager.R;
import com.whaley.biz.common.utils.TimerUtil;

/**
 * Author: qxw
 * Date:2018/1/17
 * Introduction:
 */

public class RoundProgressBar extends View {

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    private int background;
    /**
     * 圆环的宽度
     */
    private float roundWidth;
    private ResultListener resultListener;

    private boolean isStrat;

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void strat() {
        if (isStrat) {
            return;
        }
        isStrat = true;
        TimerUtil.timerMilliseconds(maxTime, intervalTime, new TimerUtil.TimerNext() {
            @Override
            public void doNext(long number) {
                drawProgress((float) number / maxTime);
                if (number <= 0 && resultListener != null) {
                    resultListener.onFinish();
                    isStrat = false;
                }
            }
        });
    }

    /**
     * 最大进度
     */
    private long maxTime;
    private long intervalTime;
    private float pre = 1;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        background = mTypedArray.getColor(R.styleable.RoundProgressBar_backColor, Color.TRANSPARENT);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        maxTime = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        intervalTime = mTypedArray.getInteger(R.styleable.RoundProgressBar_interval, 100);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        //绘制背景
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(background);
        paint.setAntiAlias(true);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2, paint);
        int centre = getWidth() / 2; //获取圆心的x坐标
        int radius = (int) (centre - roundWidth / 2); //圆环的半径
        //绘制进度条
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setAntiAlias(true);
        paint.setStrokeWidth(roundWidth);
        canvas.drawArc(oval, -90, 360, false, paint);
        paint.setColor(roundProgressColor);
        paint.setStrokeWidth(roundWidth);
        paint.setAntiAlias(true);
        canvas.drawArc(oval, -90, -360 * pre, false, paint);

        super.onDraw(canvas);
    }

    private void drawProgress(float pre) {
        this.pre = pre;
        this.postInvalidate();
    }

//    private class MyCountDownTimer extends CountDownTimer {
//
//        /**
//         * @param millisInFuture    The number of millis in the future from the call
//         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
//         *                          is called.
//         * @param countDownInterval The interval along the way to receive
//         *                          {@link #onTick(long)} callbacks.
//         */
//        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            Log.e("xxxxxxxxxxx", "millisUntilFinished=" + millisUntilFinished);
//            drawProgress(1 - millisUntilFinished / maxTime);
//        }
//
//        @Override
//        public void onFinish() {
//
//        }
//    }

    public interface ResultListener {
        public void onFinish();
    }
}
