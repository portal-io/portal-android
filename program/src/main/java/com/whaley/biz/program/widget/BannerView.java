package com.whaley.biz.program.widget;

/**
 * Created by dell on 2017/8/14.
 */

import android.content.Context;
import android.util.AttributeSet;

import com.bigkoo.convenientbanner.ConvenientBanner;

public class BannerView<T> extends ConvenientBanner<T> {

    private long autoTurningTime;

    private boolean isStartTurning=true;

    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public ConvenientBanner startTurning(long autoTurningTime) {
        this.autoTurningTime=autoTurningTime;
        this.isStartTurning=true;
        return super.startTurning(autoTurningTime);
    }

    @Override
    public void stopTurning() {
        super.stopTurning();
        this.isStartTurning=false;
    }

}
