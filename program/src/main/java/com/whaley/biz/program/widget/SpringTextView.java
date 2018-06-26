package com.whaley.biz.program.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.whaley.core.appcontext.AppContextProvider;

/**
 * Created by dell on 2018/1/31.
 */

public class SpringTextView extends TextView {

    static final Typeface TYPEFACE=Typeface.createFromAsset(AppContextProvider.getInstance().getContext().getAssets(), "spring.ttf");

    public SpringTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTypeface();
    }

    public SpringTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface();
    }

    public SpringTextView(Context context) {
        super(context);
        initTypeface();
    }

    private void initTypeface() {
        this.setTypeface(TYPEFACE);
    }

}
