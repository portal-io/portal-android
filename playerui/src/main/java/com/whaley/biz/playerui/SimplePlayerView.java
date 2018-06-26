package com.whaley.biz.playerui;

import android.content.Context;
import android.util.AttributeSet;

import com.whaley.biz.playerui.component.common.backpress.BackPressComponent;
import com.whaley.biz.playerui.component.common.backpress.BackPressController;
import com.whaley.biz.playerui.component.common.delaytorestart.DelayToRestartComponent;
import com.whaley.biz.playerui.component.common.halfswitchscreen.HalfSwitchScreenComponent;
import com.whaley.biz.playerui.component.common.networkchanged.NetworkChangedComponent;
import com.whaley.biz.playerui.component.simpleplayer.initCompleted.InitCompletedComponent;
import com.whaley.biz.playerui.component.simpleplayer.initError.InitErrorComponent;
import com.whaley.biz.playerui.component.simpleplayer.initbackground.InitBackgroundComponent;
import com.whaley.biz.playerui.component.simpleplayer.initloading.InitLoadingComponent;
import com.whaley.biz.playerui.component.common.loading.LoadingComonent;
import com.whaley.biz.playerui.component.simpleplayer.lock.LockComponent;
import com.whaley.biz.playerui.component.common.networkcompute.NetworkComputeComponent;
import com.whaley.biz.playerui.component.simpleplayer.bottomcontrol.BottomControlComponent;
import com.whaley.biz.playerui.component.common.polling.PollingCommonent;
import com.whaley.biz.playerui.component.simpleplayer.resetscreen.ResetComponent;
import com.whaley.biz.playerui.component.simpleplayer.shawdow.BottomShadowComponent;
import com.whaley.biz.playerui.component.simpleplayer.shawdow.TopShadowComponent;
import com.whaley.biz.playerui.component.simpleplayer.title.TitleComponent;
import com.whaley.biz.playerui.component.common.touchable.TouchAbleComponent;

/**
 * Created by YangZhi on 2017/8/2 15:04.
 */

public class SimplePlayerView extends PlayerView{

    public SimplePlayerView(Context context) {
        this(context,null);
    }

    public SimplePlayerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimplePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        // 每间隔1秒发送事件的定时器(轮询事件定时器)组件
        regist(new PollingCommonent());

        // 网络速度计算组件
        regist(new NetworkComputeComponent());

        // 网络状态改变监听组件
        regist(new NetworkChangedComponent());

        // 网络状态改变后延迟重新设置播放的组件
        regist(new DelayToRestartComponent());

        // 半屏和全屏切换的组件
        HalfSwitchScreenComponent halfSwitchScreenComponent = new HalfSwitchScreenComponent();
        regist(halfSwitchScreenComponent);

        // 返回按钮监听组件
        regist(new BackPressComponent((BackPressController.SwitchScreenHandle) halfSwitchScreenComponent.getController()));

        // 触摸改变控件显示状态组件
        regist(new TouchAbleComponent());

        // 顶部阴影 UI 组件
        regist(new TopShadowComponent());

        // 底部阴影 UI 组件
        regist(new BottomShadowComponent());

        // 普通视频 底部操作 UI 组件
        regist(new BottomControlComponent());

        // 锁屏 UI 组件
        regist(new LockComponent());

        // 重置 视角 UI 组件
        regist(new ResetComponent());

        // 视频 Title UI 组件
        regist(new TitleComponent());

        // 卡顿加载转圈 UI 组件
        regist(new LoadingComonent());

        // 初始化背景 UI 组件
        regist(new InitBackgroundComponent());

        // 初始化 加载状态 UI 组件
        regist(new InitLoadingComponent());

        // 带初始化背景的 错误 UI 组件
        regist(new InitErrorComponent());

        // 带初始化背景的 播放完成 UI 组件
        regist(new InitCompletedComponent());
    }
}
