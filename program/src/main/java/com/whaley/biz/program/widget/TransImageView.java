package com.whaley.biz.program.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import com.whaley.core.debug.logger.Log;
import com.whaley.core.widget.imageview.WhaleyImageView;

/**
 * Created by YangZhi on 2017/3/22 15:08.
 */

public class TransImageView extends WhaleyImageView{

    RectF rectF;


    View view;


    public TransImageView(Context context) {
        this(context,null);
    }

    public TransImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TransImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setTrans(RectF rectF){
        this.rectF=rectF;
    }

    public void setTrans(View view){
        this.view=view;
        this.rectF=null;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            if(rectF==null&&view!=null){
                rectF=new RectF(view.getLeft(),view.getTop(),view.getRight(),view.getBottom());
            }
            if(rectF!=null){
                canvas.clipRect(rectF, Region.Op.DIFFERENCE);
            }
            super.onDraw(canvas);
        }catch (Exception e){
            Log.e(e, "");
        }

    }


}
