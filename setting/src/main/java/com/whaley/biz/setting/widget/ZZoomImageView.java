package com.whaley.biz.setting.widget;

/**
 * Created by dell on 2017/10/17.
 */

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class ZZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener {

    public static final float SCALE_MAX = 2f;

    private float initScale = 1.0f;

    Matrix scaleMatrix = new Matrix();

    float[] martixValue = new float[9];

    public ZZoomImageView(Context context) {
        this(context, null);
    }

    public ZZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    //suppress deprecate warning because i have dealt with it
    @Override
    @SuppressWarnings("deprecation")
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 获取当前缩放比例
     */
    public float getScale() {
        scaleMatrix.getValues(martixValue);
        return martixValue[Matrix.MSCALE_X];
    }

    /**
     * 在缩放时，控制范围
     */
    private void checkBorderAndCenterWhenScale() {
        Matrix matrix = scaleMatrix;
        RectF rectF = new RectF();
        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }

        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();
        // 如果宽或高大于屏幕，则控制范围
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left;
            }
            if (rectF.right < width) {
                deltaX = width - rectF.right;
            }
        }
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom;
            }
        }
        // 如果宽或高小于屏幕，则让其居中
        if (rectF.width() < width) {
            deltaX = width * 0.5f - rectF.right + 0.5f * rectF.width();
        }
        if (rectF.height() < height) {
            deltaY = height * 0.5f - rectF.bottom + 0.5f * rectF.height();
        }
        scaleMatrix.postTranslate(deltaX, deltaY);
    }

    public void setScaleMatrix(float scaleFactor){
        float scale = getScale();
        if ((scale < SCALE_MAX && scaleFactor > 1.0f)
                || (scale > initScale && scaleFactor < 1.0f)) {
            if (scaleFactor * scale < initScale)
                scaleFactor = initScale / scale;
            if (scaleFactor * scale > SCALE_MAX)
                scaleFactor = SCALE_MAX / scale;
            scaleMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
            checkBorderAndCenterWhenScale();
            setImageMatrix(scaleMatrix);
        }
    }

    boolean once = true;

    @Override
    public void onGlobalLayout() {
        if (!once)
            return;
        Drawable d = getDrawable();
        if (d == null)
            return;
        //获取imageview宽高
        int width = getWidth();
        int height = getHeight();

        //获取图片宽高
        int imgWidth = d.getIntrinsicWidth();
        int imgHeight = d.getIntrinsicHeight();

        float scale = 1.0f;

        //如果图片的宽或高大于屏幕，缩放至屏幕的宽或者高
        if (imgWidth > width && imgHeight <= height)
            scale = (float) width / imgWidth;
        else if (imgHeight > height && imgWidth <= width)
            scale = (float) height / imgHeight;

        //如果图片的宽或高小于屏幕，放大至屏幕的宽或者高
        else if (imgWidth < width && imgHeight >= height)
            scale = (float) width / imgWidth;
        else if (imgHeight < height && imgWidth >= width)
            scale = (float) height / imgHeight;

        //如果图片宽高都大于屏幕，按比例缩小
        else if (imgWidth > width && imgHeight > height)
            scale = Math.min((float) imgWidth / width, (float) imgHeight / height);

            //如果图片宽高都小于屏幕，按比例放大
        else if (imgWidth < width && imgHeight < height)
            scale = Math.max((float) imgWidth / width, (float) imgHeight / height);

        initScale = scale;
        //将图片移动至屏幕中心
        scaleMatrix.postTranslate((width - imgWidth) / 2, (height - imgHeight) / 2);
        scaleMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
        setImageMatrix(scaleMatrix);
        once = false;
    }

}
