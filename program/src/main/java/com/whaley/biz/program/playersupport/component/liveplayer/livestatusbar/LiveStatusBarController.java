package com.whaley.biz.program.playersupport.component.liveplayer.livestatusbar;

import android.graphics.Color;
import android.os.Build;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.ui.view.SystemBarView;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.KeyboardVisibleChangeEvent;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by YangZhi on 2017/8/28 15:21.
 */

public class LiveStatusBarController extends ControlController {

    @Override
    protected void onInit() {
        super.onInit();
//        checkInitOrientation();
        showUI(true);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected boolean isHasUI() {
        return false;
    }

    @Override
    protected void showUI(boolean anim) {
//        super.showUI(anim);
        SystemBarTintManager systemBarTintManager = getSystemBarManager();
        if(systemBarTintManager == null)
            return;
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setNavigationBarTintEnabled(true);
        StatusBarUtil.setTransparentFullStatusBar(getActivity().getWindow(),systemBarTintManager,false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //添加Flag把状态栏设为可绘制模式
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setNavigationBarColor(Color.BLACK);
        }
    }

    @Override
    protected void hideUI(boolean anim) {
//        super.hideUI(anim);
        SystemBarTintManager systemBarTintManager = getSystemBarManager();
        if(systemBarTintManager == null)
            return;
        systemBarTintManager.setStatusBarTintEnabled(false);
        systemBarTintManager.setNavigationBarTintEnabled(false);
        StatusBarUtil.changeStatusBar(getActivity().getWindow(), false, false);
    }

    private SystemBarTintManager getSystemBarManager(){
        if(getActivity()!=null&&getActivity() instanceof SystemBarView){
            return ((SystemBarView) getActivity()).getSystemBarManager();
        }
        return null;
    }

    @Subscribe
    public void onKeyboardVisibleChange(KeyboardVisibleChangeEvent keyboardVisibleChangeEvent) {
        if(keyboardVisibleChangeEvent.isVisible()){
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

}
