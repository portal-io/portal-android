package com.whaley.biz.program.playersupport.component.splitplayer.initcomplete;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.simpleplayer.initCompleted.InitCompletedUIAdapter;
import com.whaley.biz.program.playersupport.component.normalplayer.normalinitcompleted.NormalInitCompleteComponent;
import com.whaley.biz.program.playersupport.component.statisticsdata.BIStatisticsController;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitInitCompleteComponent extends NormalInitCompleteComponent {


    boolean isLock;

    public SplitInitCompleteComponent() {
        this(false);
    }

    public SplitInitCompleteComponent(boolean isLock) {
        this.isLock = isLock;
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SplitInitCompletedUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new SplitInitCompletedController(isLock);
    }

}
