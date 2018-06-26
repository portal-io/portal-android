package com.whaley.biz.program.playersupport.widget;

import com.whaley.biz.playerui.component.BaseModuleComponent;
import com.whaley.biz.playerui.component.Component;
import com.whaley.biz.playerui.component.common.backpress.BackPressComponent;
import com.whaley.biz.playerui.component.common.delaytorestart.DelayToRestartComponent;
import com.whaley.biz.playerui.component.common.keyboardlistener.KeyBoardListenComponent;
import com.whaley.biz.playerui.component.common.loading.LoadingComonent;
import com.whaley.biz.playerui.component.common.networkchanged.NetworkChangedComponent;
import com.whaley.biz.playerui.component.common.networkcompute.NetworkComputeComponent;
import com.whaley.biz.playerui.component.common.polling.PollingCommonent;
import com.whaley.biz.playerui.component.common.touchable.TouchAbleComponent;
import com.whaley.biz.playerui.component.simpleplayer.shawdow.TopShadowComponent;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.playersupport.component.cameralist.CameraGuideComponent;
import com.whaley.biz.program.playersupport.component.cameralist.CameraListComponent;
import com.whaley.biz.program.playersupport.component.cameralist.LiveCameraGuideComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.ad.ADImageComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.initblurbackground.InitBlurBackgroundComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.initerror.InitErrorComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.livebottomcontrol.LiveBottomControlComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.liveclose.LiveCloseComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.livecomplete.LiveCompleteComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.liveinfo.LiveInfoComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.liveinfoui.LiveInfoUIComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.liveorientation.LiveOrientationComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.liveresolvedefinition.LiveResolveDefinitionComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.livestatusbar.LiveStatusBarComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.livetitle.LiveTitleComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.lottery.LotteryComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.lotteryresult.LotteryResultComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.playcount.PlayCountComponent;
import com.whaley.biz.program.playersupport.component.liveplayer.switchlotterydanmu.SwitchLotteryDanMuComponent;
import com.whaley.biz.program.playersupport.component.mobilenetwork.MobileNetworkComponent;
import com.whaley.biz.program.playersupport.component.playerbitmap.PlayerBitmapComponent;
import com.whaley.biz.program.playersupport.component.statisticsdata.BIBannerStatisticsComponent;
import com.whaley.biz.program.playersupport.component.statisticsdata.BIStatisticsComponent;
import com.whaley.biz.program.playersupport.component.statisticsdata.BIStatisticsController;
import com.whaley.biz.program.playersupport.component.switchdefinition.SwitchDefinitionComponent;
import com.whaley.biz.program.playersupport.component.uploadplaycount.UpLoadPlayCountComponent;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangZhi on 2017/9/8 19:45.
 */

public class LiveComponentsUtil {

    public static List<Component> getBannerLiveControlComponents() {
        final List<Component> components = new ArrayList<>();

        // 直播详情获取组件
        components.add(new LiveInfoComponent());

        // 解密地址组件
        Router.getInstance().buildExecutor("/parser/service/livedecrypturlcomponentprovider")
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

        // 直播处理清晰度组件
        components.add(new LiveResolveDefinitionComponent());

        components.add(new LiveCompleteComponent(true));

        return components;
    }


    public static List<Component> getBannerLiveUIComponents() {
        final List<Component> components = new ArrayList<>();

        // 播放次数轮询组件
        components.add(new PlayCountComponent());

        // 播放次数上报组件
        components.add(new UpLoadPlayCountComponent());
        components.add(new BIBannerStatisticsComponent());

        // 状态栏显示控制 组件
        components.add(new LiveStatusBarComponent());

        // 顶部阴影 UI 组件
        components.add(new TopShadowComponent());

//        // 底部阴影 UI 组件
//        regist(new BottomShadowComponent());

        components.add(new KeyBoardListenComponent());

        // 普通视频 底部操作 UI 组件
        components.add(new LiveBottomControlComponent(true));

        // 弹幕 UI 组件
        Router.getInstance().buildExecutor("/danmu/service/danmucomponentprovider")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<BaseModuleComponent>() {
                    @Override
                    public void onCall(BaseModuleComponent component) {
                        if (component != null) {
                            components.add(component);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();
        // 礼物面板 UI 组件
        Router.getInstance().buildExecutor("/livegift/service/livegiftcomponentprovider")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<BaseModuleComponent>() {
                    @Override
                    public void onCall(BaseModuleComponent component) {
                        if (component != null) {
                            components.add(component);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();
        // 显示礼物面板
        Router.getInstance().buildExecutor("/livegift/service/livegiftrevealcomponentprovider")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<BaseModuleComponent>() {
                    @Override
                    public void onCall(BaseModuleComponent component) {
                        if (component != null) {
                            components.add(component);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();
        components.add(new SwitchLotteryDanMuComponent());
        //廣告組件
        components.add(new ADImageComponent());

        // 足球机位切换组件
        components.add(new CameraListComponent(DisplayUtil.convertDIP2PX(45), DisplayUtil.convertDIP2PX(45)));

        // 视频 Title UI 组件
        components.add(new LiveTitleComponent());

        // 宝箱 组件
        components.add(new LotteryComponent());

        // 直播详情展示UI组件
        components.add(new LiveInfoUIComponent());

        // 宝箱 抽奖结果UI组件
        components.add(new LotteryResultComponent());



        // 足球机位提示UI
        components.add(new LiveCameraGuideComponent());

        // 初始化背景 UI 组件
        components.add(new InitBlurBackgroundComponent());

        // 带初始化背景的 错误 UI 组件
        components.add(new InitErrorComponent());

        // 卡顿加载转圈 UI 组件
        components.add(new LoadingComonent());

        // 关闭 UI 组件
        components.add(new LiveCloseComponent());


        return components;
    }

    public static List<Component> getLiveComponent() {
        final List<Component> components = new ArrayList<>();

        components.add(new LiveCompleteComponent(false));
        components.add(new MobileNetworkComponent());
        // 播放次数上报组件
        components.add(new UpLoadPlayCountComponent());
        components.add(new BIStatisticsComponent());
        // 直播详情获取组件
        components.add(new LiveInfoComponent());

        // 播放次数轮询组件
        components.add(new PlayCountComponent());

        // 解密地址组件
        Router.getInstance().buildExecutor("/parser/service/livedecrypturlcomponentprovider")
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

        // 直播处理清晰度组件
        components.add(new LiveResolveDefinitionComponent());

        // 播放器底图和背景图加载组件
        components.add(new PlayerBitmapComponent());

        // 直播横竖屏切换组件
        components.add(new LiveOrientationComponent());

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
        components.add(new BackPressComponent(null));

        // 触摸改变控件显示状态组件
        components.add(new TouchAbleComponent());

        components.add(new KeyBoardListenComponent());

        components.add(new SwitchLotteryDanMuComponent());

        // 状态栏显示控制 组件
        components.add(new LiveStatusBarComponent());

        // 顶部阴影 UI 组件
        components.add(new TopShadowComponent());

        // 普通视频 底部操作 UI 组件
        components.add(new LiveBottomControlComponent(false));

        //廣告組件
        components.add(new ADImageComponent());

        // 足球机位切换组件
        components.add(new CameraListComponent(DisplayUtil.convertDIP2PX(45), DisplayUtil.convertDIP2PX(45)));


        // 视频 Title UI 组件
        components.add(new LiveTitleComponent());

        // 宝箱 组件
        components.add(new LotteryComponent());

        // 宝箱 抽奖结果UI组件
        components.add(new LotteryResultComponent());

        // 足球机位提示UI
        components.add(new LiveCameraGuideComponent());

        // 初始化背景 UI 组件
        components.add(new InitBlurBackgroundComponent());

        // 弹幕 UI 组件
        Router.getInstance().buildExecutor("/danmu/service/danmucomponentprovider")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<BaseModuleComponent>() {
                    @Override
                    public void onCall(BaseModuleComponent component) {
                        if (component != null) {
                            components.add(component);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();
        // 礼物面板 UI 组件
        Router.getInstance().buildExecutor("/livegift/service/livegiftcomponentprovider")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<BaseModuleComponent>() {
                    @Override
                    public void onCall(BaseModuleComponent component) {
                        if (component != null) {
                            components.add(component);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();
        // 显示礼物面板
        Router.getInstance().buildExecutor("/livegift/service/livegiftrevealcomponentprovider")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<BaseModuleComponent>() {
                    @Override
                    public void onCall(BaseModuleComponent component) {
                        if (component != null) {
                            components.add(component);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();
        // 支付组件
        Router.getInstance().buildExecutor("/pay/service/livepaycomponentprovider")
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

        // 直播详情展示 UI 组件
        components.add(new LiveInfoUIComponent());

        // 带初始化背景的 错误 UI 组件
        components.add(new InitErrorComponent());

        // 卡顿加载转圈 UI 组件
        components.add(new LoadingComonent());

        // 关闭 UI 组件
        components.add(new LiveCloseComponent());

        return components;
    }

}
