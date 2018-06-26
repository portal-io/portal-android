package com.whaley.biz.program.playersupport.component.liveplayer.livebottomcontrol;

import android.content.pm.ActivityInfo;
import android.support.v4.util.SimpleArrayMap;
import android.view.View;
import android.widget.Toast;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.BlankShowEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.model.State;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.interactor.CheckLogin;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.playersupport.event.DefinitionChangedEvent;
import com.whaley.biz.program.playersupport.event.SwitchDefinitionEvent;
import com.whaley.biz.program.playersupport.event.ToggleCameraListVisibleEvent;
import com.whaley.biz.program.playersupport.model.DefinitionModel;
import com.whaley.biz.program.playersupport.model.MediaResultInfo;
import com.whaley.biz.program.utils.UnityUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by YangZhi on 2017/8/8 20:17.
 */

public class LiveBottomControlController extends ControlController<LiveBottomControlUIAdapter> {


    static final String OPEN_DANMU_EVENT = "/program/event/opendanmu";

    static final String CLOSE_DANMU_EVENT = "/program/event/closedanmu";

    static final String SHOW_DANMU_EDIT_EVENT = "/program/event/showdanmuedit";

    static final String HIDE_DANMU_EDIT_EVENT = "/program/event/hidedanmuedit";

    static final String LIVE_GIFT_EVENT = "/program/event/livegift";
    boolean isMultiPosition;

    boolean hasNextDefinition;

    boolean isbanner;

    public LiveBottomControlController(boolean isbanner) {
        this.isbanner = isbanner;
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        PlayData playData = videoPreparedEvent.getPlayData();
        SimpleArrayMap<Integer, DefinitionModel> definitionMap = playData.getCustomData(PlayerDataConstants.RESOLVED_DEFINITION_MAP);
//        getUIAdapter().changeSwitchDefinitionEnable(definitionMap.size() > 1);
        hasNextDefinition = definitionMap.size() > 1;

        isMultiPosition = videoPreparedEvent.getPlayData().getBooleanCustomData(PlayerDataConstants.MULTI_POSITION_TYPE);
        if (isMultiPosition) {
            getUIAdapter().showCameraButton();
        }

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

//    @Subscribe(sticky = true)
//    public void onChangeProgress(BaseEvent baseEvent){
//        if(baseEvent.getEventType().equals("changeVideoProgress")){
//            EventBus.getDefault().removeStickyEvent(baseEvent);
//            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
//            if(playData == null)
//                return;
//            MediaResultInfo mediaResultInfo = baseEvent.getObject(MediaResultInfo.class);
//            DefinitionModel currentDefinitionModel = playData.getCustomData(PlayerDataConstants.CURRENT_DEFINITION_MODEL);
//            int definition = VideoBitType.covert(mediaResultInfo.getCurrentQuality());
//            if(currentDefinitionModel!=null&&currentDefinitionModel.getDefinition()!=definition){
//                SwitchDefinitionEvent switchDefinitionEvent = new SwitchDefinitionEvent();
//                switchDefinitionEvent.setDefinition(definition);
//                emitEvent(switchDefinitionEvent);
//            }
//            emitEvent(new BlankShowEvent());
//        }
//    }

    public void onSwitchDefinitionClick() {
        if (!hasNextDefinition) {
            Toast.makeText(getContext(), "当前直播没有可切换的清晰度", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isForbidClick()) {
            return;
        }
        SwitchDefinitionEvent switchDefinitionEvent = new SwitchDefinitionEvent();
        emitEvent(switchDefinitionEvent);
    }

    public void onCameraClick() {
        if (isForbidClick()) {
            return;
        }
        ToggleCameraListVisibleEvent event = new ToggleCameraListVisibleEvent();
        emitEvent(event);
    }

    public boolean onSplitClick() {
        if (isForbidClick()){
            return false;
        }
        if(isLandScape()){
            getPlayerController().setMonocular(!getPlayerController().getMonocular());
        }else{
            getUIAdapter().showToast("竖屏模式不支持分屏");
        }
        return true;
    }

    @Override
    public boolean isLandScape() {
        return getActivity().getRequestedOrientation()== ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    protected boolean isForbidClick(){
        return getPlayerController().getRepository().isOnBuffering()||getPlayerController().getState().getCurrentState()==State.STATE_INIT||getPlayerController().getState().getCurrentState()==State.STATE_PREPARING;
    }
    public void onDanmuButtonClick() {
        CheckLogin checkLogin = new CheckLogin();
        checkLogin.buildUseCaseObservable(null)
                .subscribe(new Consumer<UserModel>() {
                    @Override
                    public void accept(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                            if (playData != null) {
                                boolean isFreeTime = playData.getBooleanCustomData(PlayerDataConstants.IS_FREE_TIME);
                                if (isFreeTime) {
                                    showPayDialog();
                                    return;
                                }
                            }
                            ModuleEvent moduleEvent = new ModuleEvent("/program/event/showdanmuedit", null);
                            emitEvent(moduleEvent);
                        } else {
                            showLoginDialog();
                        }
                    }
                });

    }

    private void showPayDialog() {
        Toast.makeText(getContext(), "试看时不支持发送弹幕", Toast.LENGTH_SHORT).show();
//        DialogUtil.showDialog(getActivity(), "发弹幕前请购买该直播", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ModuleEvent moduleEvent = new ModuleEvent("pay_program", null);
//                emitEvent(moduleEvent);
//            }
//        }, null);
    }

    private void showLoginDialog() {
        DialogUtil.showDialog(getActivity(), "您需要登录才能进行相关操作\n确定登录吗", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TitleBarActivity.goPage((Starter) getActivity(), 0, "/user/ui/login");
            }
        }, null);
    }

    @Subscribe
    public void onModuleEvent(ModuleEvent moduleEvent) {
        String eventName = moduleEvent.getEventName();
        switch (eventName) {
            case OPEN_DANMU_EVENT:
                getUIAdapter().changeBtnDanmuVisible(true);
                break;
            case CLOSE_DANMU_EVENT:
                getUIAdapter().changeBtnDanmuVisible(false);
                break;
            case SHOW_DANMU_EDIT_EVENT:
                getUIAdapter().hide(true);
                break;
            case HIDE_DANMU_EDIT_EVENT:
                getUIAdapter().show(true);
                break;
            case LIVE_GIFT_EVENT:
                getUIAdapter().showGiftButton();
                break;
            default:
                break;
        }
    }

    public void onGiftClick() {
        CheckLogin checkLogin = new CheckLogin();
        checkLogin.buildUseCaseObservable(null)
                .subscribe(new Consumer<UserModel>() {
                    @Override
                    public void accept(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
//                            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
//                            if (playData != null) {
//                                boolean isFreeTime = playData.getBooleanCustomData(PlayerDataConstants.IS_FREE_TIME);
//                                if (isFreeTime) {
//                                    showPayDialog();
//                                    return;
//                                }
//                            }
                            ModuleEvent moduleEvent = new ModuleEvent("/program/event/showlivegift", null);
                            emitEvent(moduleEvent);
                        } else {
                            showLoginDialog();
                        }
                    }
                });
    }

//    @Override
//    public void registEvents() {
//        super.registEvents();
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
//    }
//
//    @Override
//    public void unRegistEvents() {
//        super.unRegistEvents();
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
//    }

}
