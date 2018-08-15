package com.whaley.biz.program.playersupport.component.localplayer.localbottomui;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.playerui.component.simpleplayer.bottomcontrol.BottomControlController;
import com.whaley.biz.playerui.event.BlankShowEvent;
import com.whaley.biz.playerui.event.BufferingUpdateEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.ScreenChangedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.utils.StringFormatUtil;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.playersupport.event.RenderTypeSelectedEvent;
import com.whaley.biz.program.playersupport.event.ToggleRenderBoxVisibleEvent;
import com.whaley.biz.program.playersupport.model.MediaResultInfo;
import com.whaley.biz.program.utils.UnityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.disposables.Disposable;

/**
 * Created by yangzhi on 2017/8/22.
 */

public class LocalBottomUIController extends BottomControlController<LocalBottomUIAdapter>{

    private boolean isMobileModel = true;

    protected Disposable disposable;

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
    }

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        PlayData playData = preparingEvent.getPlayData();
        boolean isCanChangeRender = playData.getBooleanCustomData(PlayerDataConstants.IS_CAN_CHANGE_RENDER);
        if(isCanChangeRender){
            getUIAdapter().setChangeRenderVisible();
        }
    }

    @Override
    public void onBufferingUpdate(BufferingUpdateEvent bufferingUpdateEvent) {
//        super.onBufferingUpdate(bufferingUpdateEvent);
    }

    @Subscribe
    public void onRenderTypeSelected(RenderTypeSelectedEvent renderTypeSelectedEvent){
        getUIAdapter().updateChangeRenderBtnText(renderTypeSelectedEvent.getRenderTypeStr());
    }

    @Subscribe
    public void onChangeProgress(BaseEvent baseEvent){
        if(baseEvent.getEventType().equals("changeVideoProgress")){
            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
            MediaResultInfo mediaResultInfo = baseEvent.getObject(MediaResultInfo.class);
            if(playData!=null&&playData.getId().equals(mediaResultInfo.getCode())) {
                long progress = Long.valueOf(mediaResultInfo.getCurrentPosition());
                getUIAdapter().changeSeekProgress(progress);
                getUIAdapter().updateCurrentTimeText(StringFormatUtil.formatTime(progress));
                onSeekChanged(progress);
            }
            emitEvent(new BlankShowEvent());
        }
    }

    public void onChangeRenderClick(){
        emitEvent(new ToggleRenderBoxVisibleEvent());
    }

    private void toggleModel(){
        isMobileModel = !isMobileModel;
    }

    public void onSplitClick(){
        if (isForbidClick()) {
            return;
        }
        dispose();
        disposable = UnityUtil.goPageLocal(getPlayerController());
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
