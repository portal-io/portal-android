//package com.whaley.biz.program.playersupport.component.splitplayer;
//
//import android.content.pm.ActivityInfo;
//import android.view.View;
//
//import com.whaley.biz.playerui.PlayerView;
//import com.whaley.biz.playerui.component.BaseController;
//import com.whaley.biz.playerui.component.Component;
//import com.whaley.biz.playerui.event.ScreenChangedEvent;
//import com.whaley.biz.playerui.event.SplitChangeEvent;
//import com.whaley.biz.playerui.model.PlayData;
//import com.whaley.biz.program.constants.PlayerDataConstants;
//import com.whaley.biz.program.playersupport.event.ChangeFromSplitEvent;
//import com.whaley.biz.program.playersupport.widget.ComponentUtil;
//
//import org.greenrobot.eventbus.Subscribe;
//
//import java.util.List;
//
///**
// * Created by dell on 2017/10/30.
// */
//
//public class SplitSwitcherControl extends BaseController{
//
//    PlayerView playerView;
//
//    List<Component> normalUIComponents;
//
//    List<Component> currentUIComponents;
//
//    List<Component> lastUIComponents;
//
//    boolean isLive;
//
//    public SplitSwitcherControl(PlayerView playerView, List<Component> components, boolean isLive){
//        this.playerView = playerView;
//        this.normalUIComponents = components;
//        this.currentUIComponents = normalUIComponents;
//        this.isLive = isLive;
//    }
//
//    @Override
//    protected void onInit() {
//        super.onInit();
//    }
//
//    @Subscribe(priority = 2)
//    public void onSplitChangeEvent(SplitChangeEvent splitChangeEvent) {
//        getPlayerController().setSplit(splitChangeEvent.isSplit());
//        if (splitChangeEvent.isSplit()) {
//            changeToSplit();
//            if(isLive){
//                checkOrientation(true);
//            }
//            getPlayerController().setMonocular(false);
//        } else {
//            changeToNormal();
//            if(isLive){
//                PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
//                if (playData != null) {
//                    boolean isLandScape = playData.getBooleanCustomData(PlayerDataConstants.ORIENTATION_IS_LANDSCAPE);
//                    checkOrientation(isLandScape);
//                }
//            }
//            getPlayerController().setMonocular(true);
//            if(!isLive && !splitChangeEvent.isback()){
//                getEventBus().post(new ChangeFromSplitEvent());
//            }
//        }
//    }
//
//    private void changeToNormal() {
//        changeToUIComponents(false);
//    }
//
//    private void changeToSplit() {
//        changeToUIComponents(true);
//    }
//
//    private void changeToUIComponents(boolean isSplit) {
//        List<Component> uiComponents;
//        if(isSplit){
//            uiComponents = ComponentUtil.getSplitUIComponents(isLive);
//        }else{
//            uiComponents = normalUIComponents;
//        }
//        if (uiComponents != currentUIComponents) {
//            if (currentUIComponents != null) {
//                lastUIComponents = currentUIComponents;
//                for (Component component : currentUIComponents) {
//                    View view = null;
//                    if (component.getUIAdapter() != null) {
//                        view = component.getUIAdapter().getRootView();
//                    }
//                    if (view != null) {
//                        playerView.removeView(view);
//                    }
//                    playerView.getComponentManager().destroyView(component);
//                }
//            }
//            for (Component component : uiComponents) {
//                playerView.getComponentManager().recoveryView(component);
//                playerView.buildUIComponent(component);
//            }
//            currentUIComponents = uiComponents;
//        }
//    }
//
//    private void checkOrientation(boolean isLandScape) {
//        if (getActivity() == null)
//            return;
//       if (!isLandScape) {
//            if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                ScreenChangedEvent screenChangedEvent = new ScreenChangedEvent();
//                screenChangedEvent.setRequestOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                emitStickyEvent(screenChangedEvent);
//            }
//        } else {
//            if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                ScreenChangedEvent screenChangedEvent = new ScreenChangedEvent();
//                screenChangedEvent.setRequestOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                emitStickyEvent(screenChangedEvent);
//            }
//        }
//    }
//
//    @Override
//    protected void onDestory() {
//        super.onDestory();
//        if(lastUIComponents!=null){
//            for (Component component : lastUIComponents) {
//                if(component!=null) {
//                    component.destroy();
//                }
//            }
//        }
//    }
//
//}
