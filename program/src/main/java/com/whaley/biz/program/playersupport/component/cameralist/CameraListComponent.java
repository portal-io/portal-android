package com.whaley.biz.program.playersupport.component.cameralist;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/21 15:52.
 */

public class CameraListComponent extends BaseComponent{

    private int bottomMargin;

    private int rightMargin;

    public CameraListComponent(int rightMargin,int bottomMargin) {
        this.bottomMargin = bottomMargin;
        this.rightMargin = rightMargin;
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new CameraListUIAdapter(rightMargin,bottomMargin);
    }

    @Override
    protected BaseController onCreateController() {
        return new CameraListController();
    }
}
