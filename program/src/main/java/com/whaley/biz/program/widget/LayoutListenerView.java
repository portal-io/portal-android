package com.whaley.biz.program.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dell on 2017/8/23.
 */

public class LayoutListenerView extends View {

    private LayoutChangeListener listener;

    public LayoutListenerView(Context context) {
        super(context);
    }

    public LayoutListenerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LayoutListenerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(LayoutChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(listener!=null)
            listener.onLayout(changed,left,top,right,bottom);
    }


    public interface LayoutChangeListener{
        void onLayout(boolean changed, int left, int top, int right, int bottom);
    }
}
