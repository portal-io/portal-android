package com.whaley.biz.program.playersupport.component.normalplayer.normalstatusbar;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.ui.view.SystemBarView;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.ActivityResumeEvent;
import com.whaley.biz.playerui.event.HideEvent;
import com.whaley.biz.playerui.event.ShowEvent;
import com.whaley.core.debug.logger.Log;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yangzhi on 2017/8/25.
 */

public class NormalStatusBarController extends ControlController{

    private boolean isShowUI;

    @Override
    protected void onInit() {
        super.onInit();
        checkInitOrientation();
        showUI(true);
    }

    @Override
    protected boolean isHasUI() {
        return false;
    }

    @Override
    public void onShowEvent(ShowEvent showEvent) {
        super.onShowEvent(showEvent);
    }

    @Override
    public void onHideEvent(HideEvent hideEvent) {
        super.onHideEvent(hideEvent);
    }

    @Override
    protected void showUI(boolean anim) {
        isShowUI = true;
        SystemBarTintManager systemBarTintManager = getSystemBarManager();
        if(systemBarTintManager == null)
            return;
        systemBarTintManager.setStatusBarTintEnabled(false);
        systemBarTintManager.setNavigationBarTintEnabled(true);
        StatusBarUtil.changeStatusBar(getActivity().getWindow(), false, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //添加Flag把状态栏设为可绘制模式
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setNavigationBarColor(Color.BLACK);
        }
    }

    @Override
    protected void hideUI(boolean anim) {
        isShowUI = false;
        SystemBarTintManager systemBarTintManager = getSystemBarManager();
        if(systemBarTintManager == null)
            return;
        if (isLandScape()) {
            systemBarTintManager.setStatusBarTintEnabled(false);
            systemBarTintManager.setNavigationBarTintEnabled(false);
            StatusBarUtil.changeStatusBar(getActivity().getWindow(), false, false);
        } else {
            systemBarTintManager.setStatusBarTintEnabled(false);
            systemBarTintManager.setNavigationBarTintEnabled(true);
            StatusBarUtil.changeStatusBar(getActivity().getWindow(), false, true);
        }
    }

    @Subscribe
    public void onActivityResume(ActivityResumeEvent activityResumeEvent){
        if(isShowUI){
            showUI(false);
        }else{
            hideUI(false);
        }
    }

    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    @Override
    public boolean isLandScape() {
        Activity activity = getActivity();
        if(activity==null)
            return false;
        return activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    @Override
    protected void onSwitchToLandscape() {
        super.onSwitchToLandscape();
    }

    @Override
    protected void onSwitchToProtrait() {
        super.onSwitchToProtrait();
    }

    private SystemBarTintManager getSystemBarManager(){
        if(getActivity()!=null&&getActivity() instanceof SystemBarView){
            return ((SystemBarView) getActivity()).getSystemBarManager();
        }
        return null;
    }

}
