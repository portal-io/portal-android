package com.whaley.biz.program.playersupport.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.whaley.biz.playerui.PlayerView;

/**
 * Created by yangzhi on 2017/8/22.
 */

public class LocalPlayerView extends PlayerView {

    public LocalPlayerView(Context context) {
        this(context, null);
    }

    public LocalPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocalPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        getComponentManager().registAll(ComponentUtil.getLocalComponents());
    }

}
