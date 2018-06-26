package com.whaley.biz.program.playersupport.component.bannerplayer.bannerstatusbar;

import android.view.WindowManager;

import com.whaley.biz.playerui.component.common.control.ControlController;

/**
 * Created by dell on 2017/9/30.
 */

public class BannerStatusBarController extends ControlController {

    @Override
    protected void onInit() {
        super.onInit();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected boolean isHasUI() {
        return false;
    }

    @Override
    protected void showUI(boolean anim) {

    }

    @Override
    protected void hideUI(boolean anim) {

    }

}
