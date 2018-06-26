package com.whaley.biz.program.playersupport.component.cameralist;


import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.playersupport.event.ToggleCameraListVisibleEvent;
import com.whaley.biz.program.playersupport.model.CameraModel;
import com.whaley.wvrplayer.vrplayer.external.event.VideoConstantValue;

import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

/**
 * Created by YangZhi on 2017/8/21 15:52.
 */

public class CameraListController extends ControlController<CameraListUIAdapter> {
    static final String SHOW_CAMERA_LIST = "/program/event/showcameralist";
    static final String HIDE_CAMERA_LIST = "/program/event/hidecameralist";
    private Map<String, CameraModel> cameraList;

    private boolean isFirst;

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        isFirst = true;
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        PlayData playData = videoPreparedEvent.getPlayData();
        boolean isMultiPosition = playData.getBooleanCustomData(PlayerDataConstants.MULTI_POSITION_TYPE);
        if (isMultiPosition) {
            Map<String, CameraModel> cameraList = playData.getCustomData(PlayerDataConstants.KEY_MULTI_POSITION_CAMERA);
            if (cameraList != CameraListController.this.cameraList) {
                CameraListController.this.cameraList = cameraList;
                getUIAdapter().setCameraList(cameraList);
                if (getPlayerController().getRepository().getCurrentPlayData() != null) {
                    playData = getPlayerController().getRepository().getCurrentPlayData();
                    if (playData != null) {
                        String camera = playData.getCustomData(PlayerDataConstants.CURRENT_CAMERA);
                        getUIAdapter().setCameraSource(camera);
                    }
                }
            }
        }
        getUIAdapter().hideCameraList();
    }

    @Subscribe
    public void onToggleCameraListVisibleEvent(ToggleCameraListVisibleEvent toggleCameraListVisibleEvent) {
        getUIAdapter().toggleCameraList();
    }

    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    @Override
    protected void onSwitchToLandscape() {

    }

    @Override
    protected void onSwitchToProtrait() {
        getUIAdapter().hideCameraList();
    }

    public void onCameraSelected(String camera) {
        BufferingEvent bufferingEvent = new BufferingEvent();
        emitStickyEvent(bufferingEvent);
        CameraModel cameraModel = cameraList.get(camera);
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        playData.setOrginUrl(cameraModel.getUrl());
        playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA, cameraModel.getCamera());
        boolean isPublic = cameraModel.isPublic();
        playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA_IS_PUBLIC, isPublic);
        playData.putCustomData(PlayerDataConstants.BACKGROUND_FINAL_IMAGE, isPublic ? playData.getCustomData(PlayerDataConstants.BACKGROUND_IMAGE) : playData.getCustomData(PlayerDataConstants.VIP_IMAGE));
        playData.putCustomData(PlayerDataConstants.SERVER_RENDER_TYPE, cameraModel.getServerRenderType());
        PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
        prepareStartPlayEvent.setPlayData(playData);
        if (playData.getType() == PlayerType.TYPE_LIVE) {
            prepareStartPlayEvent.setMaxPriority(PlayerConstants.PREPARE_START_PLAY_PRIORITY_SWITCH_DEFINITION);
        } else {
            playData.setProgress(getPlayerController().getCurrentProgress());
            prepareStartPlayEvent.setMaxPriority(PlayerConstants.PREPARE_START_PLAY_PRIORITY_GET_INFO);
        }
        emitStickyEvent(prepareStartPlayEvent);
    }


    private void checkOrientation() {
        if (isFirst && getPlayerController().getRepository().isVideoPrepared() && getPlayerController().getDeviceOrientation() == VideoConstantValue.ORIENTATION_PORTRAIT) {
            getPlayerController().orientationReset();
            isFirst = false;
        }
    }


    @Subscribe
    public void onPolling(PollingEvent pollingEvent) {
        checkOrientation();
    }

    public void showCamera() {
        ModuleEvent moduleEvent = new ModuleEvent(SHOW_CAMERA_LIST, null);
        emitEvent(moduleEvent);
    }

    public void hideCamera() {
        ModuleEvent moduleEvent = new ModuleEvent(HIDE_CAMERA_LIST, null);
        emitEvent(moduleEvent);
    }
}
