package com.whaley.biz.program.playersupport.component.cameralist;

import android.content.Context;
import android.content.SharedPreferences;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.RestTouchDurationEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/9/19 18:26.
 */

public class CameraGuideController extends BaseController<CameraGuideUIAdapter> {

    static final String PRE_FIRST_LOAD = "pre_first_load";

    static final String KEY_FIRST_CAMERA = "pre_first_camera";

    boolean isTipOnShow;

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        checkShowTip();
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
//        checkShowTip();
    }

    private void checkShowTip() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData == null)
            return;
        boolean isFootBall = playData.getBooleanCustomData(PlayerDataConstants.FOOTBALL_TYPE);
        if (getUIAdapter() != null && !isTipOnShow && isFootBall
                && getFirstCameraTip()
                && ((playData.getType() == PlayerType.TYPE_PANO && isLandScape()) || playData.getType() == PlayerType.TYPE_LIVE)) {
            isTipOnShow = true;
            getUIAdapter().show();
            RestTouchDurationEvent restTouchDurationEvent = new RestTouchDurationEvent();
            restTouchDurationEvent.setRegistTouchDuration(false);
            emitEvent(restTouchDurationEvent);
        }
    }

    public void onGuideClick() {
        setFirstCameraTip();
        hide();
    }

    private void hide() {
        getUIAdapter().hide();
        RestTouchDurationEvent restTouchDurationEvent = new RestTouchDurationEvent();
        restTouchDurationEvent.setRegistTouchDuration(true);
        emitEvent(restTouchDurationEvent);
        isTipOnShow = false;
    }

    public boolean isTipOnShow() {
        return isTipOnShow;
    }


    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    @Subscribe(priority = 100)
    public void onBackPressEvent(BackPressEvent backPressEvent) {
        if (isTipOnShow()) {
            hide();
            getEventBus().cancelEventDelivery(backPressEvent);
        }
    }

    @Override
    protected void onSwitchToLandscape() {
        super.onSwitchToLandscape();
        checkShowTip();
    }

    @Override
    protected void onSwitchToProtrait() {
        super.onSwitchToProtrait();
    }

    private void setFirstCameraTip() {
        SharedPreferences preferences = AppContextProvider.getInstance().getContext().getSharedPreferences(PRE_FIRST_LOAD,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_FIRST_CAMERA, false);
        editor.apply();
    }

    private boolean getFirstCameraTip() {
        SharedPreferences preferences = AppContextProvider.getInstance().getContext().getSharedPreferences(PRE_FIRST_LOAD,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_FIRST_CAMERA, true);
    }
}
