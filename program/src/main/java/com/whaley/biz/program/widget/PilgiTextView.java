package com.whaley.biz.program.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.whaley.core.appcontext.AppContextProvider;

/**
 * Created by dell on 2017/8/17.
 */

public class PilgiTextView extends TextView {

    static final Typeface TYPEFACE=Typeface.createFromAsset(AppContextProvider.getInstance().getContext().getAssets(), "DINOT-Medium.otf");

    public PilgiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTypeface();
    }

    public PilgiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface();
    }

    public PilgiTextView(Context context) {
        super(context);
        initTypeface();
    }

    private void initTypeface() {
        this.setTypeface(TYPEFACE);
    }



}
