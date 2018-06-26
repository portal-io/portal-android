package com.whaley.biz.program.playersupport.component.splitplayer.initbackgroud;

import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.simpleplayer.initbackground.InitBackgroundComponent;

/**
 * Created by dell on 2017/10/31.
 */

public class SplitInitBackgroundComponent extends InitBackgroundComponent {

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SplitInitBackgroundUIAdapter();
    }

}
