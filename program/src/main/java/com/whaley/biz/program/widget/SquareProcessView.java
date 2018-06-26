package com.whaley.biz.program.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.whaley.biz.program.R;

public class SquareProcessView extends View {

    private int MAX_PROGRESS = 100;
    private float PER_LINE_MAX_PROCESS_H;
    private float PER_LINE_MAX_PROCESS_W;

    private Context mContext;
    private Paint processPaint;
    private Canvas canvas;

    private int currentPogress;
    private float strokeWith = 5.0f;
    private int strokeColor = Color.BLUE;

    private boolean initCanvas;

    public SquareProcessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initValue(attrs);
    }

    private void initValue(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SquareProcessView);
        currentPogress = typedArray.getInteger(R.styleable.SquareProcessView_currentProgress, 0);
        MAX_PROGRESS = typedArray.getInteger(R.styleable.SquareProcessView_currentProgress, MAX_PROGRESS);
        strokeWith = typedArray.getDimension(R.styleable.SquareProcessView_strokeWith, strokeWith);
        strokeColor = typedArray.getColor(R.styleable.SquareProcessView_strokeColor, strokeColor);
        initProcessPaint();
    }

    private void initProcessPaint() {
        processPaint = new Paint();
        processPaint.setColor(strokeColor);
        processPaint.setStrokeWidth(strokeWith);
        processPaint.setAntiAlias(true);
        processPaint.setStyle(Paint.Style.STROKE);
    }

    public void setCurrentPogress(int currentPogress) {
        this.currentPogress = currentPogress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!initCanvas){
            initCanvas = true;
            initCanvas(canvas);
        }
        this.canvas = canvas;
        drawProcessSquare(currentPogress);
    }

    private void initCanvas(Canvas canvas){
        int canvasWidth  = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int total = canvasWidth*2+canvasHeight*2;
        PER_LINE_MAX_PROCESS_H = MAX_PROGRESS * canvasHeight/total;
        PER_LINE_MAX_PROCESS_W = MAX_PROGRESS * canvasWidth/total;
    }

    private void drawProcessSquare(int progress) {
        float topProcess = 0;
        float rightProcess = 0;
        float bottomProcess = 0;
        float leftProcess = 0;
        if (progress <= PER_LINE_MAX_PROCESS_H) {
            rightProcess = progress;
        } else if (progress <= PER_LINE_MAX_PROCESS_H+PER_LINE_MAX_PROCESS_W) {
            rightProcess = PER_LINE_MAX_PROCESS_H;
            bottomProcess = progress - PER_LINE_MAX_PROCESS_H;
        } else if (progress <= PER_LINE_MAX_PROCESS_H+PER_LINE_MAX_PROCESS_W+PER_LINE_MAX_PROCESS_H) {
            rightProcess = PER_LINE_MAX_PROCESS_H;
            bottomProcess = PER_LINE_MAX_PROCESS_W;
            leftProcess = progress - PER_LINE_MAX_PROCESS_H - PER_LINE_MAX_PROCESS_W;
        } else if (progress <= MAX_PROGRESS) {
            rightProcess = PER_LINE_MAX_PROCESS_H;
            bottomProcess = PER_LINE_MAX_PROCESS_W;
            leftProcess = PER_LINE_MAX_PROCESS_H;
            topProcess = progress-PER_LINE_MAX_PROCESS_H-PER_LINE_MAX_PROCESS_W-PER_LINE_MAX_PROCESS_H;
        }
        drawProgressRightLine(rightProcess);
        drawProgressBottomLine(bottomProcess);
        drawProgressLeftLine(leftProcess);
        drawProgressTopLine(topProcess);
    }

    private void drawProgressTopLine(float progress) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(canvas.getWidth() / PER_LINE_MAX_PROCESS_W * progress, 0);
        canvas.drawPath(path, processPaint);
    }

    private void drawProgressRightLine(float progress) {
        Path path = new Path();
        path.moveTo(canvas.getWidth(), 0);
        path.lineTo(canvas.getWidth(), canvas.getHeight() / PER_LINE_MAX_PROCESS_H * progress);
        canvas.drawPath(path, processPaint);
    }

    private void drawProgressBottomLine(float progress) {
        Path path = new Path();
        path.moveTo(canvas.getWidth(), canvas.getHeight());
        path.lineTo(canvas.getWidth() / PER_LINE_MAX_PROCESS_W * Math.abs(progress - PER_LINE_MAX_PROCESS_W), canvas.getHeight());
        canvas.drawPath(path, processPaint);
    }

    private void drawProgressLeftLine(float progress) {
        Path path = new Path();
        path.moveTo(0, canvas.getHeight());
        path.lineTo(0, canvas.getHeight() / PER_LINE_MAX_PROCESS_H * Math.abs(progress - PER_LINE_MAX_PROCESS_H));
        canvas.drawPath(path, processPaint);
    }

}
