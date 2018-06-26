package com.whaley.biz.program.playersupport.component.bannerplayer.bannerorientation;

import android.content.pm.ActivityInfo;
import android.view.WindowManager;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.common.backpress.BackPressController;
import com.whaley.biz.playerui.event.ScreenChangedEvent;
import com.whaley.biz.playerui.event.SwitchScreenEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/9/13 13:03.
 */

public class BannerOrientationController extends BaseController implements BackPressController.SwitchScreenHandle {

    private boolean isFullScreen;

    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    @Subscribe
    public void onSwitchScreenEvent(SwitchScreenEvent switchScreenEvent) {
        isFullScreen = !isFullScreen;
        if (isFullScreen) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            BaseEvent baseEvent = new BaseEvent("program/event/switchbannertofullscreen");
            EventBus.getDefault().post(baseEvent);
        } else {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            BaseEvent baseEvent = new BaseEvent("program/event/switchfullscreentobanner");
            EventBus.getDefault().post(baseEvent);
        }
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData != null) {
            boolean isLandScape = isFullScreen ? playData.getBooleanCustomData(PlayerDataConstants.ORIENTATION_IS_LANDSCAPE) : false;
            checkOrientation(isLandScape);
        }
    }

    private void checkOrientation(boolean isLandScape) {
        if (getActivity() == null)
            return;
        if (!isLandScape) {
            if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ScreenChangedEvent screenChangedEvent = new ScreenChangedEvent();
                screenChangedEvent.setRequestOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                emitStickyEvent(screenChangedEvent);
            }
        } else {
            if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                ScreenChangedEvent screenChangedEvent = new ScreenChangedEvent();
                screenChangedEvent.setRequestOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                emitStickyEvent(screenChangedEvent);
            }
        }
    }

    @Override
    public boolean shouldSwitchOnBack() {
        return isFullScreen;
    }
}
