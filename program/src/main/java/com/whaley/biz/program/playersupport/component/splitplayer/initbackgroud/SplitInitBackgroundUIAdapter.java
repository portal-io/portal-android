package com.whaley.biz.program.playersupport.component.splitplayer.initbackgroud;

import android.view.View;

import com.whaley.biz.playerui.component.simpleplayer.initbackground.InitBackgroundUIAdapter;
import com.whaley.biz.program.R;

/**
 * Created by dell on 2017/10/31.
 */

public class SplitInitBackgroundUIAdapter extends InitBackgroundUIAdapter {

    protected void show() {
        checkInflatedView();
        inflatedView.setVisibility(View.VISIBLE);
        inflatedView.setAlpha(1f);
        inflatedView.setImageResource(R.color.color2);
    }

}
