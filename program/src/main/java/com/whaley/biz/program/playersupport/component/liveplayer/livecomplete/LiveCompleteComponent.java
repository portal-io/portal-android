package com.whaley.biz.program.playersupport.component.liveplayer.livecomplete;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/9/30 18:42.
 */

public class LiveCompleteComponent extends BaseComponent{
    boolean isOnBanner;

    public LiveCompleteComponent(boolean isOnBanner){
        this.isOnBanner = isOnBanner;
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }

    @Override
    protected BaseController onCreateController() {
        return new LiveCompleteController(isOnBanner);
    }
}
