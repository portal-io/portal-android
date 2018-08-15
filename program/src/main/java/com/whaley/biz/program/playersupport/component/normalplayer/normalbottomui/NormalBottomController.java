package com.whaley.biz.program.playersupport.component.normalplayer.normalbottomui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v4.util.SimpleArrayMap;
import android.widget.Toast;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.playerui.component.simpleplayer.bottomcontrol.BottomControlController;
import com.whaley.biz.playerui.event.ActivityResumeEvent;
import com.whaley.biz.playerui.event.BlankShowEvent;
import com.whaley.biz.playerui.event.BufferingUpdateEvent;
import com.whaley.biz.playerui.event.ScreenChangedEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.utils.StringFormatUtil;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.playersupport.event.DefinitionChangedEvent;
import com.whaley.biz.program.playersupport.event.SwitchDefinitionEvent;
import com.whaley.biz.program.playersupport.event.ToggleCameraListVisibleEvent;
import com.whaley.biz.program.playersupport.model.DefinitionModel;
import com.whaley.biz.program.playersupport.model.MediaResultInfo;
import com.whaley.biz.program.utils.UnityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.disposables.Disposable;

/**
 * Created by yangzhi on 2017/8/5.
 */

public class NormalBottomController<T extends NormalBottomUIAdapter> extends BottomControlController<T> {

    boolean isFootball;

    boolean hasNextDefinition;

    protected Disposable disposable;

    boolean isbanner;

    long progress = -1;

    public NormalBottomController(boolean isbanner){
        this.isbanner = isbanner;
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        super.onVideoPrepared(videoPreparedEvent);
        PlayData playData = videoPreparedEvent.getPlayData();

        isFootball = playData.getBooleanCustomData(PlayerDataConstants.FOOTBALL_TYPE);
        if (isFootball && isLandScape()) {
            getUIAdapter().showCameraButton();
        }else {
            getUIAdapter().hideCameraButton();
        }

        SimpleArrayMap<Integer, DefinitionModel> definitionMap = playData.getCustomData(PlayerDataConstants.RESOLVED_DEFINITION_MAP);
        hasNextDefinition = definitionMap.size() > 1;
//        getUIAdapter().changeSwitchDefinitionEnable(definitionMap.size() > 1);

        int type = playData.getType();
        getUIAdapter().changeOtherPlayerButtonVisible(type != PlayerType.TYPE_MORETV_TV && type != PlayerType.TYPE_MORETV_2D);
    }

    @Subscribe(sticky = true)
    public void onDefinitionChangedEvent(DefinitionChangedEvent definitionChangedEvent) {
        String text;
        switch (definitionChangedEvent.getDefinitionModel().getDefinition()) {
            case VideoBitType.SD:
            case VideoBitType.SDB:
            case VideoBitType.TDA:
            case VideoBitType.TDB:
                text = "超清";
                break;
            case VideoBitType.HD:
                text = "原画";
                break;
            default:
                text = "高清";
                break;
        }
        getUIAdapter().updateDefinitionText(text);
    }

    @Subscribe
    public void onExitSplit(BaseEvent baseEvent){
        if(baseEvent.getEventType().equals("exitSplit")) {
            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
            if (playData != null && playData.getId().equals(baseEvent.getObject())) {
                Activity activity = getActivity();
                if (activity != null) {
                    int requestOrientation;
                    if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        requestOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    } else {
                        return;
                    }
                    activity.setRequestedOrientation(requestOrientation);
                    ScreenChangedEvent screenChangedEvent = new ScreenChangedEvent();
                    screenChangedEvent.setRequestOrientation(requestOrientation);
                    emitStickyEvent(screenChangedEvent);
                }
            }
        }
    }

    @Subscribe(sticky = true)
    public void onChangeProgress(BaseEvent baseEvent){
        if(baseEvent.getEventType().equals("changeVideoProgress") || baseEvent.getEventType().equals("changeDramaProgress")){
            EventBus.getDefault().removeStickyEvent(baseEvent);
            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
            if(playData == null)
                return;
            MediaResultInfo mediaResultInfo = baseEvent.getObject(MediaResultInfo.class);
            DefinitionModel currentDefinitionModel = playData.getCustomData(PlayerDataConstants.CURRENT_DEFINITION_MODEL);
            int split_definition = VideoBitType.covert(mediaResultInfo.getCurrentQuality());
            if(currentDefinitionModel!=null&&currentDefinitionModel.getDefinition()!=split_definition){
                playData.setProgress(Long.valueOf(mediaResultInfo.getCurrentPosition()));
                int definition = split_definition;
                SwitchDefinitionEvent switchDefinitionEvent = new SwitchDefinitionEvent();
                switchDefinitionEvent.setDefinition(definition);
                emitEvent(switchDefinitionEvent);
            }else {
                if (playData.getId().equals(mediaResultInfo.getCode())) {
                    if(isbanner){
                        long currentProgress = Long.valueOf(mediaResultInfo.getCurrentPosition());
                        getUIAdapter().changeSeekProgress(currentProgress);
                        getUIAdapter().updateCurrentTimeText(StringFormatUtil.formatTime(currentProgress));
                        onSeekChanged(currentProgress);
                    }else {
                        progress = Long.valueOf(mediaResultInfo.getCurrentPosition());
                    }
                }
            }
            emitEvent(new BlankShowEvent());
        }
    }

    @Override
    public void onBufferingUpdate(BufferingUpdateEvent bufferingUpdateEvent) {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData != null && playData.getType() != PlayerType.TYPE_LOCALVIDEO) {
            super.onBufferingUpdate(bufferingUpdateEvent);
        }
    }

    @Subscribe
    public void onActivityResume(ActivityResumeEvent activityResumeEvent){
        resetProgress();
    }

    private void resetProgress(){
        if(progress >= 0) {
            getUIAdapter().changeSeekProgress(progress);
            getUIAdapter().updateCurrentTimeText(StringFormatUtil.formatTime(progress));
            onSeekChanged(progress);
        }
        progress = -1;
    }

    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    @Override
    protected void checkInitOrientation() {
        super.checkInitOrientation();
    }

    @Override
    protected void onSwitchToLandscape() {
        super.onSwitchToLandscape();
        getUIAdapter().showSwitchDefinition();
        if (isFootball) {
            getUIAdapter().showCameraButton();
        }
    }

    @Override
    protected void onSwitchToProtrait() {
        super.onSwitchToProtrait();
        getUIAdapter().showSwitchScreen();
        getUIAdapter().hideCameraButton();
    }

    @Override
    protected void onSwitchToOtherOrientation(int orientation) {
        super.onSwitchToOtherOrientation(orientation);
    }

    public void onSwitchDefinitionClick() {
        if (!hasNextDefinition) {
            Toast.makeText(getContext(), "当前视频没有可切换的清晰度", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isForbidClick()) {
            return;
        }
        emitEvent(new SwitchDefinitionEvent());
    }

    public void onCameraClick() {
        if (isForbidClick()) {
            return;
        }
        emitEvent(new ToggleCameraListVisibleEvent());
    }

    public boolean onSplitClick() {
        if (isForbidClick()) {
            return false;
        }
        dispose();
        disposable = UnityUtil.goPageProgram(getPlayerController(), false);
        return true;
    }

    protected void dispose(){
        if(disposable!=null){
            disposable.dispose();
            disposable=null;
        }
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        dispose();
    }

    @Override
    public void registEvents() {
        super.registEvents();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
