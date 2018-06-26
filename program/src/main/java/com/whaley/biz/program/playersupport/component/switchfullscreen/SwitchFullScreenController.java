package com.whaley.biz.program.playersupport.component.switchfullscreen;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.SwitchScreenEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yangzhi on 2017/8/20.
 */

public class SwitchFullScreenController extends BaseController{

    @Override
    protected void onInit() {
        super.onInit();
    }


    @Subscribe
    public void onSwitchScreenEvent(SwitchScreenEvent switchScreenEvent){

    }
}
