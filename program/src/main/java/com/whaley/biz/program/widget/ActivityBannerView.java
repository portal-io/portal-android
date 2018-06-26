package com.whaley.biz.program.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whaley.biz.program.R;
import com.whaley.core.widget.banner.WhaleyBanner;
import com.whaley.core.widget.viewpager.ViewPager;

/**
 * Author: qxw
 * Date:2018/1/29
 * Introduction:
 */

public class ActivityBannerView extends WhaleyBanner {

    TextView textView;

    public ActivityBannerView(Context context) {
        super(context);
        initActivityBannerView();
    }


    public ActivityBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initActivityBannerView();
    }

    private void initActivityBannerView() {
        textView = (TextView) findViewById(R.id.tv_name);
    }

    public TextView getTextView() {
        return textView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_banner;
    }

    @Override
    public ViewGroup getViewGroup() {
        return (ViewGroup) findViewById(R.id.layout_indicators);
    }

    @Override
    public ViewPager getLayoutPager() {
        return (ViewPager) findViewById(R.id.layout_pager);
    }
}
