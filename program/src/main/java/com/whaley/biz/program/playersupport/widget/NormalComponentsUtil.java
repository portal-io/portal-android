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
import com.whaley.biz.playerui.component.simpleplayer.initbackground.InitBackgroundComponent;
import com.whaley.biz.playerui.component.simpleplayer.lock.LockComponent;
import com.whaley.biz.playerui.component.simpleplayer.shawdow.BottomShadowComponent;
import com.whaley.biz.playerui.component.simpleplayer.shawdow.TopShadowComponent;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.playersupport.component.cameralist.CameraGuideComponent;
import com.whaley.biz.program.playersupport.component.cameralist.CameraListComponent;
import com.whaley.biz.program.playersupport.component.mobilenetwork.MobileNetworkComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.info.InfoComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normalbottomui.NormalBottomUIComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normalinitcompleted.NormalInitCompleteComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normaliniterror.NormalInitErrorComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normalinitloading.NormalInitLoadingComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normalreset.NormalResetComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normalstatusbar.NormalStatusBarComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.normaltitle.NormalTitleComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.resolvedefinition.ResolveDefinitionComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.springfestival.SpringFestivalComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.springfestivalresult.SpringFestivalResultComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.tv.TVComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.tvmore.TVMoreComponent;
import com.whaley.biz.program.playersupport.component.normalplayer.userhistory.UserHistoryComponent;
import com.whaley.biz.program.playersupport.component.playerbitmap.PlayerBitmapComponent;
import com.whaley.biz.program.playersupport.component.statisticsdata.BIBannerStatisticsComponent;
import com.whaley.biz.program.playersupport.component.statisticsdata.BIStatisticsComponent;
import com.whaley.biz.program.playersupport.component.switchdefinition.SwitchDefinitionComponent;
import com.whaley.biz.program.playersupport.component.uploadplaycount.UpLoadPlayCountComponent;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangZhi on 2017/9/8 19:43.
 */

public class NormalComponentsUtil {
    public static List<Component> getBannerNormalControlComponents() {
        final List<Component> components = new ArrayList<>();

        // 节目数据获取组件
        components.add(new InfoComponent());

        // 电视剧相关逻辑控制组件
        components.add(new TVComponent());

        // 电视猫电影和电视剧相关控制组件
        components.add(new TVMoreComponent());

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

        return components;
    }

    public static List<Component> getBannerNormalUIComponents() {
        final List<Component> components = new ArrayList<>();
        // 播放次数上报组件
        components.add(new UpLoadPlayCountComponent());
        //历史浏览上报
        components.add(new UserHistoryComponent());

        //BI数据统计
        components.add(new BIBannerStatisticsComponent());
        // 状态栏 UI 改变组件
        components.add(new NormalStatusBarComponent());

        // 顶部阴影 UI 组件
        components.add(new TopShadowComponent());

        // 底部阴影 UI 组件
        components.add(new BottomShadowComponent());

        // 普通视频 底部操作 UI 组件
        components.add(new NormalBottomUIComponent(true));

        // 锁屏 UI 组件
        components.add(new LockComponent());

        // 重置 视角 UI 组件
        components.add(new NormalResetComponent());

        // 足球机位列表 UI 组件
        components.add(new CameraListComponent(DisplayUtil.convertDIP2PX(10), DisplayUtil.convertDIP2PX(45)));

        // 卡顿加载转圈 UI 组件
        components.add(new LoadingComonent());

        // 视频 Title UI 组件
        components.add(new NormalTitleComponent());
        
        // 足球机位提示UI
        components.add(new CameraGuideComponent());

        // 初始化背景 UI 组件
        components.add(new InitBackgroundComponent());

        // 初始化 加载状态 UI 组件
        components.add(new NormalInitLoadingComponent());

        // 带初始化背景的 错误 UI 组件
        components.add(new NormalInitErrorComponent());

        // 带初始化背景的 播放完成 UI 组件
        components.add(new NormalInitCompleteComponent());

//        // 视频 Title UI 组件
//        components.add(new TitleComponent());

        return components;
    }

    public static List<Component> getNormalComponent(final BackPressController.SwitchScreenHandle switchScreenHandle) {
        final List<Component> components = new ArrayList<>();

        components.add(new MobileNetworkComponent());
        // 播放次数上报组件
        components.add(new UpLoadPlayCountComponent());

        //历史浏览上报
        components.add(new UserHistoryComponent());

        //bi数据统计
        components.add(new BIStatisticsComponent());
        // 节目数据获取组件
        components.add(new InfoComponent());

        // 电视剧相关逻辑控制组件
        components.add(new TVComponent());

        // 电视猫电影和电视剧相关控制组件
        components.add(new TVMoreComponent());

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

        components.add(new PlayerBitmapComponent());

        // 触摸改变控件显示状态组件
        components.add(new TouchAbleComponent());


        // 状态栏显示控制 组件
        components.add(new NormalStatusBarComponent());

        // 顶部阴影 UI 组件
        components.add(new TopShadowComponent());

        // 底部阴影 UI 组件
        components.add(new BottomShadowComponent());

        // 普通视频 底部操作 UI 组件
        components.add(new NormalBottomUIComponent(false));

        // 锁屏 UI 组件
        components.add(new LockComponent());

        // 重置 视角 UI 组件
        components.add(new NormalResetComponent());

        // 足球机位列表 UI 组件
        components.add(new CameraListComponent(DisplayUtil.convertDIP2PX(10), DisplayUtil.convertDIP2PX(45)));

        // 卡顿加载转圈 UI 组件
        components.add(new LoadingComonent());

        // 足球机位提示UI
        components.add(new CameraGuideComponent());

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
        components.add(new InitBackgroundComponent());

        // 支付组件
        Router.getInstance().buildExecutor("/pay/service/paycomponentprovider")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<BaseModuleComponent>() {
                    @Override
                    public void onCall(BaseModuleComponent component) {
                        if (component != null) {
                            component.setData(PlayerConstants.PREPARE_START_PLAY_PRIORITY_PAY);
                            components.add(component);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();

        // 初始化 加载状态 UI 组件
        components.add(new NormalInitLoadingComponent());

        // 带初始化背景的 错误 UI 组件
        components.add(new NormalInitErrorComponent());

        // 带初始化背景的 播放完成 UI 组件
        components.add(new NormalInitCompleteComponent());


        // 视频 Title UI 组件
        components.add(new NormalTitleComponent());


        return components;
    }

}
