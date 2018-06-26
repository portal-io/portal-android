package com.whaley.biz.program.playersupport.component.bannerplayer;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.playerui.component.Component;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.playersupport.widget.BannerPlayerView;
import com.whaley.biz.program.playersupport.widget.ComponentUtil;
import com.whaley.biz.program.playersupport.widget.LiveComponentsUtil;
import com.whaley.biz.program.playersupport.widget.NormalComponentsUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by YangZhi on 2017/9/8 19:12.
 */

public class BannerComponentsSwitcher {

    BannerPlayerView bannerPlayerView;

    List<Component> bannerUIComponents = ComponentUtil.getBannerUIComponents();

    List<Component> normalUIComponents = NormalComponentsUtil.getBannerNormalUIComponents();

    List<Component> liveNormalUIComponents = LiveComponentsUtil.getBannerLiveUIComponents();

    List<Component> liveControlComponents = LiveComponentsUtil.getBannerLiveControlComponents();

    List<Component> normalControlComponents = NormalComponentsUtil.getBannerNormalControlComponents();

    List<Component> currentControlComponents;

    List<Component> currentUIComponents;

    public BannerComponentsSwitcher(BannerPlayerView bannerPlayerView) {
        this.bannerPlayerView = bannerPlayerView;
    }

    public void switchByPlayData(PlayData playData) {
        switch (playData.getType()) {
            case PlayerType.TYPE_LIVE:
                changeToLive();
                break;
            case PlayerType.TYPE_PANO:
                changeToNormal();
                break;
            case PlayerType.TYPE_3D:
            case PlayerType.TYPE_MORETV_2D:
            case PlayerType.TYPE_MORETV_TV:
                changeToNormal();
                break;
        }

    }

    private void changeToLive() {
        changeToControlComponents(liveControlComponents);
    }

    private void changeToNormal() {
        changeToControlComponents(normalControlComponents);
    }

    private void changeToControlComponents(List<Component> controlComponents) {
        if (currentControlComponents == controlComponents)
            return;
        if (currentControlComponents != null) {
            for (Component component : currentControlComponents) {
                bannerPlayerView.getComponentManager().unRegist(component);
            }
        }
        for (Component component : controlComponents) {
            bannerPlayerView.getComponentManager().regist(component);
        }
        currentControlComponents = controlComponents;
    }

    public void init() {

    }

    public void regist() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if(!bannerPlayerView.getPlayerController().getEventBus().isRegistered(this)){
            bannerPlayerView.getPlayerController().getEventBus().register(this);
        }
    }

    public void unRegist() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if(bannerPlayerView.getPlayerController().getEventBus().isRegistered(this)){
            bannerPlayerView.getPlayerController().getEventBus().unregister(this);
        }
    }

    @Subscribe
    public void onEvent(BaseEvent event) {
        if (event.getEventType().equals("program/event/switchbannertofullscreen")) {
            changeToFullScreen();
        } else if (event.getEventType().equals("program/event/switchfullscreentobanner")) {
            changeToBanner();
        }
    }

    private void updatePlayerViewSize(int[] size){
        View realPlayerView = bannerPlayerView.getRealPlayerView();
        ViewGroup.LayoutParams layoutParams = realPlayerView.getLayoutParams();
        if (layoutParams.width != size[0] || layoutParams.height!=size[1]) {
            layoutParams.width = size[0];
            layoutParams.height = size[1];
            realPlayerView.requestLayout();
        }
    }

    public static int[] getScreenSize() {
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        WindowManager windowManager = (WindowManager) AppContextProvider.getInstance().getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class c = Class.forName("android.view.Display");
            Method e = c.getMethod("getRealMetrics", new Class[]{DisplayMetrics.class});
            e.invoke(display, new Object[]{displayMetrics});
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
        } catch (Exception var7) {
            Logger.e(var7, "getDpi", new Object[0]);
        }
        int[] size = new int[]{width, height};
        return size;
    }

    public void changeToFullScreen() {
        if (currentControlComponents == null)
            return;
        List<Component> uiComponents;
        if (currentControlComponents == liveControlComponents) {
            uiComponents = liveNormalUIComponents;
        } else {
            uiComponents = normalUIComponents;
        }
        changeToUIComponents(uiComponents);
    }

    public void changeToBanner() {
        changeToUIComponents(bannerUIComponents);
    }

    private void changeToUIComponents(List<Component> uiComponents) {
        if (uiComponents != currentUIComponents) {
            if (currentUIComponents != null) {
                for (Component component : currentUIComponents) {
                    View view = null;
                    if (component.getUIAdapter() != null) {
                        view = component.getUIAdapter().getRootView();
                    }
                    if (view != null) {
                        bannerPlayerView.removeView(view);
                    }
                    bannerPlayerView.getComponentManager().unRegist(component);
                }
            }
            for (Component component : uiComponents) {
                bannerPlayerView.getComponentManager().regist(component);
                bannerPlayerView.buildUIComponent(component);
            }
            currentUIComponents = uiComponents;
        }
    }

}
