package com.whaley.biz.livegift.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.whaley.core.appcontext.AppContextProvider;

/**
 * Author: qxw
 * Date:2017/10/25
 * Introduction:
 */

public class RedoubleTextView extends TextView {

    static final Typeface TYPEFACE = Typeface.createFromAsset(AppContextProvider.getInstance().getContext().getAssets(), "NanumPen.ttf");

    public RedoubleTextView(Context context) {
        this(context, null);
    }

    public RedoubleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedoubleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTypeface();
    }


    private void initTypeface() {
        this.setTypeface(TYPEFACE);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
    }
}
