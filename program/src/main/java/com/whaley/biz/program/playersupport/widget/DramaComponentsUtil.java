package com.whaley.biz.program.playersupport.widget;

import com.whaley.biz.playerui.component.BaseModuleComponent;
import com.whaley.biz.playerui.component.Component;
import com.whaley.biz.playerui.component.common.backpress.BackPressComponent;
import com.whaley.biz.playerui.component.common.backpress.BackPressController;
import com.whaley.biz.playerui.component.common.delaytorestart.DelayToRestartComponent;
import com.whaley.biz.playerui.component.common.loading.LoadingComonent;
import com.whaley.biz.playerui.component.common.networkchanged.NetworkChangedComponent;
import com.whaley.biz.playerui.component.common.networkcompute.NetworkComputeComponent;
import com.whaley.biz.playerui.component.common.polling.PollingCommonent;
import com.whaley.biz.playerui.component.common.touchable.TouchAbleComponent;
import com.whaley.biz.playerui.component.simpleplayer.shawdow.BottomShadowComponent;
import com.whaley.biz.playerui.component.simpleplayer.shawdow.TopShadowComponent;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.playersupport.component.dramaplayer.bottomui.DramaBottomUIComponent;
import com.whaley.biz.program.playersupport.component.dramaplayer.info.DramaInfoComponent;
import com.whaley.biz.program.playersupport.component.dramaplayer.initbackgroud.DramaInitBackgroundComponent;
import com.whaley.biz.program.playersupport.component.dramaplayer.initcompleted.DramaInitCompletedComponent;
import com.whaley.biz.program.playersupport.component.dramaplayer.lock.DramaLockComponent;
import com.whaley.biz.program.playersupport.component.dramaplayer.playerbitmap.DramaBitmapComponent;
import com.whaley.biz.program.playersupport.component.dramaplayer.playerbitmap.DramaBitmapController;
import com.whaley.biz.program.playersupport.component.dramaplayer.reset.DramaResetComponent;
import com.whaley.biz.program.playersupport.component.dramaplayer.select.DramaSelectComponent;
import com.whaley.biz.program.playersupport.component.dramaplayer.uploadplaycount.DramaUpLoadPlayCountComponent;
import com.whaley.biz.program.playersupport.component.mobilenetwork.MobileNetworkComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normaliniterror.NormalInitErrorComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normalinitloading.NormalInitLoadingComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normalstatusbar.NormalStatusBarComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normaltitle.NormalTitleComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.resolvedefinition.ResolveDefinitionComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.springfestival.SpringFestivalComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.springfestivalresult.SpringFestivalResultComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.userhistory.UserHistoryComponent;
import com.whaley.biz.program.playersupport.component.playerbitmap.PlayerBitmapComponent;
import com.whaley.biz.program.playersupport.component.statisticsdata.BIDramaStatisticsComponent;
import com.whaley.biz.program.playersupport.component.switchdefinition.SwitchDefinitionComponent;
import com.whaley.biz.program.playersupport.component.uploadplaycount.UpLoadPlayCountComponent;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/11/13.
 */

public class DramaComponentsUtil {

    public static List<Component> getDramaComponent(final BackPressController.SwitchScreenHandle switchScreenHandle) {
        final List<Component> components = new ArrayList<>();
        components.add(new MobileNetworkComponent());
        // 播放次数上报组件
        components.add(new DramaUpLoadPlayCountComponent());

        //历史浏览上报
        components.add(new UserHistoryComponent(true));

        //bi数据统计
        components.add(new BIDramaStatisticsComponent());
        // 节目数据获取组件
        components.add(new DramaInfoComponent());

        // 解密地址组件
        Router.getInstance().buildExecutor("/parser/service/decrypturlcomponentprovider")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<BaseModuleComponent>() {
                    @Override
                    public void onCall(BaseModuleComponent component) {
                        if (component != null) {
                            component.setData(PlayerConstants.PREPARE_START_PLAY_PRIORITY_DECRYPT_URL);
                            components.add(component);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();

        // 解析播放地址组件
        Router.getInstance().buildExecutor("/parser/service/parsercomponentprovider")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<BaseModuleComponent>() {
                    @Override
                    public void onCall(BaseModuleComponent component) {
                        if (component != null) {
                            component.setData(PlayerConstants.PREPARE_START_PLAY_PRIORITY_PARSER);
                            components.add(component);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();

        // 处理解析数据并获得清晰度集合组件
        components.add(new ResolveDefinitionComponent());

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

        // 返回按钮监听组件
        components.add(new BackPressComponent(new BackPressController.SwitchScreenHandle() {
            @Override
            public boolean shouldSwitchOnBack() {
                if (switchScreenHandle != null)
                    return switchScreenHandle.shouldSwitchOnBack();
                return false;
            }
        }));

        components.add(new DramaBitmapComponent());

        // 触摸改变控件显示状态组件
        components.add(new TouchAbleComponent());

        // 状态栏显示控制 组件
        components.add(new NormalStatusBarComponent());

        // 顶部阴影 UI 组件
        components.add(new TopShadowComponent());

        // 底部阴影 UI 组件
        components.add(new BottomShadowComponent());

        // 普通视频 底部操作 UI 组件
        components.add(new DramaBottomUIComponent());

        // 锁屏 UI 组件
        components.add(new DramaLockComponent());

        // 重置 视角 UI 组件
        components.add(new DramaResetComponent());

        // 卡顿加载转圈 UI 组件
        components.add(new LoadingComonent());

        components.add(new DramaSelectComponent());

        Router.getInstance().buildExecutor("/launch/service/getfestival")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<Boolean>() {
                    @Override
                    public void onCall(Boolean aBoolean) {
                        if(aBoolean){
                            // 春节 组件
                            components.add(new SpringFestivalComponent());
                            // 春节 抽奖结果UI组件
                            components.add(new SpringFestivalResultComponent());
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();

        // 初始化背景 UI 组件
        components.add(new DramaInitBackgroundComponent());

        // 初始化 加载状态 UI 组件
        components.add(new NormalInitLoadingComponent());

        // 带初始化背景的 错误 UI 组件
        components.add(new NormalInitErrorComponent());

//        // 带初始化背景的 播放完成 UI 组件
        components.add(new DramaInitCompletedComponent());

        // 视频 Title UI 组件
        components.add(new NormalTitleComponent());

        return components;
    }

}
