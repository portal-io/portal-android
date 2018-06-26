package com.whaley.biz.program.playersupport.component.splitplayer.initloading;

import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.simpleplayer.initloading.InitLoadingUIAdapter;
import com.whaley.biz.program.playersupport.component.normalplayer.normalinitloading.NormalInitLoadingComponent;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitInitLoadingComponent extends NormalInitLoadingComponent {

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SplitInitLoadingUIAdapter();
    }

}
