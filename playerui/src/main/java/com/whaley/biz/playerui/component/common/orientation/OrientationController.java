package com.whaley.biz.playerui.component.common.orientation;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import com.whaley.biz.playerui.component.BaseController;

/**
 * Created by YangZhi on 2017/8/2 17:07.
 */

public class OrientationController extends BaseController {

    @Override
    protected void onInit() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }




}
