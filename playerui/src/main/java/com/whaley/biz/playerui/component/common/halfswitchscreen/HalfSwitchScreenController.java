package com.whaley.biz.playerui.component.common.halfswitchscreen;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.common.backpress.BackPressController;
import com.whaley.biz.playerui.event.ScreenChangedEvent;
import com.whaley.biz.playerui.event.SwitchScreenEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yangzhi on 2017/8/4.
 */

public class HalfSwitchScreenController extends BaseController implements BackPressController.SwitchScreenHandle{


    @Subscribe
    public void onSwitchScreen(SwitchScreenEvent switchScreenEvent){
        Activity activity = getActivity();
        if (activity != null) {
            int requestOrientation;
            if(activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                requestOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }else {
                requestOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            }
            activity.setRequestedOrientation(requestOrientation);
            ScreenChangedEvent screenChangedEvent = new ScreenChangedEvent();
            screenChangedEvent.setRequestOrientation(requestOrientation);
            emitStickyEvent(screenChangedEvent);
        }
    }

    @Override
    public boolean shouldSwitchOnBack() {
        Activity activity = getActivity();
        if (activity != null && activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return true;
        }
        return false;
    }
}
