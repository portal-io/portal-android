package com.whaley.biz.program.playersupport.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.whaley.biz.playerui.PlayerView;

/**
 * Created by YangZhi on 2017/8/22 11:35.
 */

public class LivePlayerView extends PlayerView {

    public LivePlayerView(Context context) {
        this(context, null);
    }

    public LivePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LivePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        getComponentManager().registAll(LiveComponentsUtil.getLiveComponent());
    }

}
