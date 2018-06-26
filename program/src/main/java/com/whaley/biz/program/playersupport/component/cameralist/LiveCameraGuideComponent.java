package com.whaley.biz.program.playersupport.component.cameralist;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.R;

/**
 * Created by YangZhi on 2017/9/19 18:26.
 */

public class LiveCameraGuideComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new CameraGuideUIAdapter(R.layout.layout_live_player_camera_guide);
    }

    @Override
    protected BaseController onCreateController() {
        return new CameraGuideController();
    }
}
