package com.whaley.biz.program.playersupport.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.whaley.biz.common.utils.LeakCanaryUtil;
import com.whaley.biz.playerui.PlayerView;
import com.whaley.biz.playerui.component.Component;
import com.whaley.biz.program.playersupport.component.bannerplayer.BannerComponentsSwitcher;
import com.whaley.core.debug.logger.Log;

/**
 * Created by YangZhi on 2017/8/28 16:54.
 */

public class BannerPlayerView extends PlayerView {

    BannerComponentsSwitcher bannerComponentsSwitcher;

    private boolean isComponentRegisted;

    public BannerPlayerView(Context context) {
        this(context, null);
    }

    public BannerPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        bannerComponentsSwitcher = new BannerComponentsSwitcher(this);
    }


    public void unRegistControllers() {
        bannerComponentsSwitcher.unRegist();
        for (Component component : getComponentManager().getComponents()) {
            Component.Controller controller = component.getController();
            if (controller != null) {
                controller.unRegistEvents();
            }
        }
    }

    public void registControllers() {
        bannerComponentsSwitcher.regist();
        if (registBannerComponents()) {
            return;
        }
        for (Component component : getComponentManager().getComponents()) {
            Component.Controller controller = component.getController();
            if (controller != null) {
                controller.registEvents();
            }
        }
    }

    public void changeToFullScreen(){
        bannerComponentsSwitcher.changeToFullScreen();
    }

    @Override
    protected void checkBuildComponents() {
    }

    private boolean registBannerComponents() {
        if (!isComponentRegisted()) {
            getComponentManager().registAll(ComponentUtil.getCommonComponents());
            rebuildComponents();
            bannerComponentsSwitcher.changeToBanner();
            isComponentRegisted = true;
            return true;
        }
        return false;
    }

    public boolean isComponentRegisted() {
        return isComponentRegisted;
    }

    public BannerComponentsSwitcher getBannerComponentsSwitcher() {
        return bannerComponentsSwitcher;
    }

    @Override
    public void destory() {
        bannerComponentsSwitcher.unRegist();
        super.destory();
        LeakCanaryUtil.getRefWatcher().watch(this);
    }

}
