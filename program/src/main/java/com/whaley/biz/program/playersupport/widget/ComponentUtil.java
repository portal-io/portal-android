package com.whaley.biz.program.playersupport.widget;

import com.whaley.biz.playerui.component.Component;
import com.whaley.biz.playerui.component.common.backpress.BackPressComponent;
import com.whaley.biz.playerui.component.common.backpress.BackPressController;
import com.whaley.biz.playerui.component.common.delaytorestart.DelayToRestartComponent;
import com.whaley.biz.playerui.component.common.loading.LoadingComonent;
import com.whaley.biz.playerui.component.common.networkchanged.NetworkChangedComponent;
import com.whaley.biz.playerui.component.common.networkcompute.NetworkComputeComponent;
import com.whaley.biz.playerui.component.common.polling.PollingCommonent;
import com.whaley.biz.playerui.component.common.touchable.TouchAbleComponent;
import com.whaley.biz.playerui.component.simpleplayer.initCompleted.InitCompletedComponent;
import com.whaley.biz.playerui.component.simpleplayer.initbackground.InitBackgroundComponent;
import com.whaley.biz.playerui.component.simpleplayer.initloading.InitLoadingComponent;
import com.whaley.biz.playerui.component.simpleplayer.lock.LockComponent;
import com.whaley.biz.playerui.component.simpleplayer.resetscreen.ResetComponent;
import com.whaley.biz.playerui.component.simpleplayer.shawdow.BottomShadowComponent;
import com.whaley.biz.playerui.component.simpleplayer.shawdow.TopShadowComponent;
import com.whaley.biz.playerui.component.simpleplayer.title.TitleComponent;
import com.whaley.biz.program.playersupport.component.bannerplayer.bannerorientation.BannerOrientationComponent;
import com.whaley.biz.program.playersupport.component.bannerplayer.bannerstatusbar.BannerStatusBarComponent;
import com.whaley.biz.program.playersupport.component.bannerplayer.bottomui.BannerBottomUIComponent;
import com.whaley.biz.program.playersupport.component.bannerplayer.typeswitcher.TypeSwitcherComponent;
import com.whaley.biz.program.playersupport.component.cameralist.CameraGuideComponent;
import com.whaley.biz.program.playersupport.component.cameralist.CameraListComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.playcount.PlayCountComponent;
import com.whaley.biz.program.playersupport.component.localplayer.localbottomui.LocalBottomUIComponent;
import com.whaley.biz.program.playersupport.component.localplayer.renderbox.RenderBoxComponent;
import com.whaley.biz.program.playersupport.component.mobilenetwork.MobileNetworkComponent;
import com.whaley.biz.program.playersupport.component.newplaydatacontinue.NewPlayDataContinueComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normaliniterror.NormalInitErrorComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normalinitloading.NormalInitLoadingComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normalstatusbar.NormalStatusBarComponent;
import com.whaley.biz.program.playersupport.component.playerbitmap.PlayerBitmapComponent;
import com.whaley.biz.program.playersupport.component.splitplayer.bottom.SplitBottomUIComponent;
import com.whaley.biz.program.playersupport.component.splitplayer.close.SplitCloseComponent;
import com.whaley.biz.program.playersupport.component.splitplayer.initbackgroud.SplitInitBackgroundComponent;
import com.whaley.biz.program.playersupport.component.splitplayer.initcomplete.SplitInitCompleteComponent;
import com.whaley.biz.program.playersupport.component.splitplayer.initerror.SplitInitErrorComponent;
import com.whaley.biz.program.playersupport.component.splitplayer.initloading.SplitInitLoadingComponent;
import com.whaley.biz.program.playersupport.component.splitplayer.initpause.SplitInitPauseComponent;
import com.whaley.biz.program.playersupport.component.splitplayer.loading.SplitLoadingComponent;
import com.whaley.biz.program.playersupport.component.splitplayer.playpause.SplitPlayPauseComponent;
import com.whaley.biz.program.playersupport.component.statisticsdata.BIStatisticsComponent;
import com.whaley.biz.program.playersupport.component.switchdefinition.SwitchDefinitionComponent;
import com.whaley.core.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangZhi on 2017/9/8 19:02.
 */

public class ComponentUtil {

    public static List<Component> getCommonComponents() {
        final List<Component> components = new ArrayList<>();
        components.add(new MobileNetworkComponent());
        // 设置新数据组件
        components.add(new NewPlayDataContinueComponent());

        components.add(new TypeSwitcherComponent());

        // 播放器底图和背景图加载组件
        components.add(new PlayerBitmapComponent());

        // 切换清晰度组件
        components.add(new SwitchDefinitionComponent());

        // 每间隔1秒发送事件的定时器(轮询事件定时器)组件
        components.add(new PollingCommonent());

        // 网络速度计算组件
        components.add(new NetworkComputeComponent());

        // 网络状态改变监听组件
        components.add(new NetworkChangedComponent());

        // 网络状态改变后延迟重新设置播放的组件
        components.add(new DelayToRestartComponent());

        // Banner 屏幕方向控制组件
        final BannerOrientationComponent bannerOrientationComponent = new BannerOrientationComponent();
        components.add(bannerOrientationComponent);

        // 返回组件
        components.add(new BackPressComponent(new BackPressController.SwitchScreenHandle() {
            @Override
            public boolean shouldSwitchOnBack() {
                if (bannerOrientationComponent.getController() != null) {
                    return ((BackPressController.SwitchScreenHandle) bannerOrientationComponent.getController()).shouldSwitchOnBack();
                }
                return false;
            }
        }));

        // 触摸改变控件显示状态组件
        components.add(new TouchAbleComponent());

        return components;
    }

    public static List<Component> getBannerUIComponents() {
        final List<Component> components = new ArrayList<>();
        components.add(new LoadingComonent());
        components.add(new BannerBottomUIComponent());
        components.add(new InitBackgroundComponent());
        components.add(new NormalInitLoadingComponent());
        components.add(new NormalInitErrorComponent());
        components.add(new InitCompletedComponent());
        components.add(new BannerStatusBarComponent());
        // 播放次数轮询组件
        components.add(new PlayCountComponent());
        return components;
    }

    public static List<Component> getSplitUIComponents(boolean isLive) {
        final List<Component> components = new ArrayList<>();

        // 状态栏 UI 改变组件
        components.add(new NormalStatusBarComponent());

        // 顶部阴影 UI 组件
        components.add(new TopShadowComponent());

        // 底部阴影 UI 组件
        components.add(new BottomShadowComponent());

        if(!isLive) {
            components.add(new SplitPlayPauseComponent());
        }

        // 普通视频 底部操作 UI 组件
        components.add(new SplitBottomUIComponent(isLive));

        // 足球机位列表 UI 组件
        components.add(new CameraListComponent(DisplayUtil.convertDIP2PX(10), DisplayUtil.convertDIP2PX(45)));

        // 卡顿加载转圈 UI 组件
        components.add(new SplitLoadingComponent());

        if (!isLive) {
            components.add(new SplitInitPauseComponent());
        }

        // 足球机位提示UI
        components.add(new CameraGuideComponent());

        components.add(new SplitInitBackgroundComponent());

        // 初始化 加载状态 UI 组件
        components.add(new SplitInitLoadingComponent());

        // 带初始化背景的 错误 UI 组件
        components.add(new SplitInitErrorComponent());

        // 带初始化背景的 播放完成 UI 组件
        components.add(new SplitInitCompleteComponent());

        components.add(new SplitCloseComponent(isLive));

        return components;
    }

    public static List<Component> getLocalComponents() {
        final List<Component> components = new ArrayList<>();
        // 状态栏显示控制 组件
        components.add(new NormalStatusBarComponent());

        // 每间隔1秒发送事件的定时器(轮询事件定时器)组件
        components.add(new PollingCommonent());

        // 返回按钮监听组件
        components.add(new BackPressComponent(new BackPressController.SwitchScreenHandle() {
            @Override
            public boolean shouldSwitchOnBack() {
                return false;
            }
        }));

        // 触摸改变控件显示状态组件
        components.add(new TouchAbleComponent());

        // 顶部阴影 UI 组件
        components.add(new TopShadowComponent());

        // 底部阴影 UI 组件
        components.add(new BottomShadowComponent());

        // 本地视频 底部操作 UI 组件
        components.add(new LocalBottomUIComponent());

        // render 切换 UI 组件
        components.add(new RenderBoxComponent());

        // 锁屏 UI 组件
        components.add(new LockComponent());

        // 重置 视角 UI 组件
        components.add(new ResetComponent());

        // 视频 Title UI 组件
        components.add(new TitleComponent());

        // 初始化背景 UI 组件
        components.add(new InitBackgroundComponent());

        // 初始化 加载状态 UI 组件
        components.add(new InitLoadingComponent());

        // 带初始化背景的 错误 UI 组件
        components.add(new com.whaley.biz.playerui.component.simpleplayer.initError.InitErrorComponent());

        // 带初始化背景的 播放完成 UI 组件
        components.add(new InitCompletedComponent());

        components.add(new BIStatisticsComponent());
        return components;
    }

}
