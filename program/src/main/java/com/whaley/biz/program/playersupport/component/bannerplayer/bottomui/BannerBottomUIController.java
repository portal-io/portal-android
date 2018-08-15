package com.whaley.biz.program.playersupport.component.bannerplayer.bottomui;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.playerui.component.simpleplayer.bottomcontrol.BottomControlController;
import com.whaley.biz.playerui.event.BlankShowEvent;
import com.whaley.biz.playerui.event.OtherPlayerEnableEvent;
import com.whaley.biz.playerui.event.ScreenChangedEvent;
import com.whaley.biz.playerui.event.SwitchScreenEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.utils.StringFormatUtil;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.playersupport.event.SplitEvent;
import com.whaley.biz.program.playersupport.event.SwitchBannerTypeEvent;
import com.whaley.biz.program.playersupport.event.SwitchDefinitionEvent;
import com.whaley.biz.program.playersupport.model.DefinitionModel;
import com.whaley.biz.program.playersupport.model.MatchInfo;
import com.whaley.biz.program.playersupport.model.MediaInfo;
import com.whaley.biz.program.playersupport.model.MediaResultInfo;
import com.whaley.biz.program.utils.UnityUtil;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Router;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by YangZhi on 2017/8/28 16:56.
 */

public class BannerBottomUIController extends BottomControlController<BannerBottomUIAdapter> {

    private int currentType;

    Disposable disposable;

    @Subscribe(sticky = true)
    public void onSwitchBannerType(SwitchBannerTypeEvent switchBannerTypeEvent){
        switch (switchBannerTypeEvent.getType()) {
            case SwitchBannerTypeEvent.TYPE_VR:
                getUIAdapter().changeToVR();
                break;
            case SwitchBannerTypeEvent.TYPE_MOVIE:
                getUIAdapter().changeToMovie();
                break;
            case SwitchBannerTypeEvent.TYPE_LIVE:
                getUIAdapter().changeToLive();
                break;
        }
        currentType = switchBannerTypeEvent.getType();
    }

    @Override
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent){
        super.onVideoPrepared(videoPreparedEvent);
        int type = videoPreparedEvent.getPlayData().getType();
        getUIAdapter().changeOtherPlayerButtonVisible(type != PlayerType.TYPE_MORETV_TV && type != PlayerType.TYPE_MORETV_2D);
    }

    @Subscribe(sticky = true)
    public void onChangeProgress(BaseEvent baseEvent){
        if(baseEvent.getEventType().equals("changeVideoProgress")){
            EventBus.getDefault().removeStickyEvent(baseEvent);
            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
            if(playData == null)
                return;
            MediaResultInfo mediaResultInfo = baseEvent.getObject(MediaResultInfo.class);
            DefinitionModel currentDefinitionModel = playData.getCustomData(PlayerDataConstants.CURRENT_DEFINITION_MODEL);
            int definition = VideoBitType.covert(mediaResultInfo.getCurrentQuality());
            if(currentDefinitionModel!=null&&currentDefinitionModel.getDefinition()!=definition){
                playData.setProgress(Long.valueOf(mediaResultInfo.getCurrentPosition()));
                SwitchDefinitionEvent switchDefinitionEvent = new SwitchDefinitionEvent();
                switchDefinitionEvent.setDefinition(definition);
                emitEvent(switchDefinitionEvent);
            }else {
                if (playData.getId().equals(mediaResultInfo.getCode()) && currentType != SwitchBannerTypeEvent.TYPE_LIVE) {
                    long progress = Long.valueOf(mediaResultInfo.getCurrentPosition());
                    getUIAdapter().changeSeekProgress(progress);
                    getUIAdapter().updateCurrentTimeText(StringFormatUtil.formatTime(progress));
                    onSeekChanged(progress);
                }
            }
            emitEvent(new BlankShowEvent());
        }
    }

    public void onSplitClick(){
        if (isForbidClick()) {
            return;
        }
        dispose();
        boolean isLive = currentType == SwitchBannerTypeEvent.TYPE_LIVE;
        disposable = UnityUtil.goPageProgram(getPlayerController(), isLive);
//        onSwitchScreenClick();
//        emitStickyEvent(new SplitEvent());
    }

    private void dispose(){
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
