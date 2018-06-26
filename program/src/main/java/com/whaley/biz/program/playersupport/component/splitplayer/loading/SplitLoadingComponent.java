package com.whaley.biz.program.playersupport.component.splitplayer.loading;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.common.loading.LoadingComonent;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitLoadingComponent extends LoadingComonent {

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SplitLoadingUIAdapter();
    }

}
