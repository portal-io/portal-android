package com.whaley.biz.playerui.playercontroller;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.playerui.event.EventManager;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.playerui.model.IRepository;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.model.Setting;
import com.whaley.biz.playerui.model.State;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by YangZhi on 2017/4/17 17:34.
 */

public interface IPlayerController {


    /**
     * 设置播放器单个数据并准备开始播放
     * @param playData 单个播放数据
     */
    void setNewPlayData(PlayData playData);

    /**
     * 设置播放数据并准备开始播放
     * 如果已有正在准备好的数据和新设置的数据一致 则继续播放而不重新开始播放
     * @param playData 单个播放数据
     */
    void setNewPlayDataContinue(PlayData playData);

    /**
     * 设置播放器数据 播放一组数据
     * 当单个视频播放完成将会播放下一条数据
     * @param playDatas 播放数据集合
     */
    void setNewPlayData(List<PlayData> playDatas);

    /**
     * 设置播放器数据 播放一组数据
     * 当单个视频播放完成将会播放下一条数据
     * @param playDatas 播放数据集合
     * @param startIndex 开始播放的下标
     */
    void setNewPlayData(List<PlayData> playDatas,int startIndex);

    /**
     * 准备开始播放（调用该方法会触发 {@link com.whaley.biz.playerui.event.PrepareStartPlayEvent} 事件）
     * 需要从最高优先级开始发送 {@link com.whaley.biz.playerui.event.PrepareStartPlayEvent} 事件时可以主动调用该方法
     */
    void prepareStartPlay();

    /**
     * 给播放器设置数据并正式开始播放
     * 通常情况下不需要自己调用该方法
     * @param playData
     */
    void startPlay(PlayData playData);

    /**
     * 重新设置数据并由当前进度开始播放
     * 不会重新发送 {@link com.whaley.biz.playerui.event.PrepareStartPlayEvent} 事件
     */
    void reStartPlay();

    /**
     * 重新设置数据并由当前进度开始播放
     * @param isResetState 是否重置状态, 如果为 true 则会重新发送 {@link com.whaley.biz.playerui.event.PrepareStartPlayEvent} 事件,否则不会重新发送该事件
     */
    void reStartPlay(boolean isResetState);

    /**
     * 主动完成播放
     * 会将播放状态改变为 {@link State#STATE_COMPLETED} 并发送黏性的 {@link com.whaley.biz.playerui.event.CompletedEvent} 事件
     */
    void complete();

    /**
     * 设置播放器出现错误
     * 会将播放状态改变为 {@link State#STATE_ERROR} 并发送黏性的 {@link com.whaley.biz.playerui.event.ErrorEvent} 事件
     * @param erroCode 错误码
     * @param msg 错误描述
     */
    void setError(int erroCode, String msg);

    /**
     * 设置播放器出现错误
     * 会将播放状态改变为 {@link State#STATE_ERROR} 并发送黏性的 {@link com.whaley.biz.playerui.event.ErrorEvent} 事件
     * @param error
     */
    void setError(PlayerException error);

    /**
     * 重置 Error
     * 会将 {@link com.whaley.biz.playerui.model.Repository#playerException} 置空
     * 并移除黏性 {@link com.whaley.biz.playerui.event.ErrorEvent} 事件
     */
    void restError();

    /**
     * 开始播放
     * 会将播放状态改变为 {@link State#STATE_STARTED} 并发送黏性的 {@link com.whaley.biz.playerui.event.StartedEvent} 事件
     */
    void start();

    /**
     * 将播放进度重置后再次播放
     * 会重新发送 {@link com.whaley.biz.playerui.event.PrepareStartPlayEvent} 事件
     */
    void replay();

    /**
     * 如果通过 {@link #setNewPlayData(List)} 或 {@link #setNewPlayData(List, int)} 设置了播放数据
     * 则会播放下一个视频，否则不起任何作用
     */
    void playNext();

    /**
     * 暂停播放
     * 会将播放状态改变为 {@link State#STATE_PAUSED} 并发送黏性的 {@link com.whaley.biz.playerui.event.PausedEvent} 事件
     */
    void pause();

    /**
     * 清空播放器
     * 会将播放状态改变为 {@link State#STATE_INIT} 清空所有黏性事件并将 {@link com.whaley.biz.playerui.model.Repository} 的数据重置
     */
    void release();

    /**
     * 判断播放器是否被 Release
     * @return 返回是否被 Release 初始化时也返回 true
     */
    boolean isReleased();

    /**
     * 判断播放器是否在播放
     * @return true 代表正在播放, false 代表没有播放
     */
    boolean isStarted();

    /**
     * 判断播放器是否真实暂停（不包括 Activity Pause 的时候的自动暂停）
     * @return
     */
    boolean isRealPaused();

    /**
     * 判断播放器是否暂停（包括 Activity Pause 的时候的自动暂停）
     * @return
     */
    boolean isPaused();


    /**
     * 拿到播放器的状态机
     * @return
     */
    State getState();

    /**
     * 拿到播放器的数据仓储
     * @return
     */
    IRepository getRepository();

    /**
     * 改变当前播放进度
     * @param progress
     */
    void changeCurentProgress(long progress);

//    IPlayer getIPlayer();

    /**
     * 拿到设置对象
     * @return
     */
    Setting getSetting();

    /**
     * 拿到帧率
     * @return
     */
    float getFps();

    /**
     * 拿到网络速度
     * @return
     */
    long getTcpSpeed();

    /**
     * 重置播放视角
     */
    void orientationReset();

    /**
     * 获得设备方向
     * @return
     */
    int getDeviceOrientation();

    /**
     * 设置 debug 模式
     * @param debug
     */
    void setDebug(boolean debug);

    /**
     * 设置视场大小
     * @param var1
     */
    void setFov(float var1);//设置视场角大小

    /**
     * 获取视场大小
     * @return
     */
    float getFov();//获取视场角大小

    /**
     * 获取当前进度
     * @return
     */
    long getCurrentProgress();

    /**
     * 设置 VR 视频底部图片
     * @param scale
     * @param bitmap
     */
    void setBottomOverlayView(float scale, Bitmap bitmap);

    /**
     * 设置 VR 视频内背景图片
     * @param backgroundImage
     */
    void setBackgroundImage(Bitmap backgroundImage);

    /**
     * 设置播放的渲染模式
     * @param renderType
     */
    void setRenderType(int renderType);//renderType值如RenderConstantValue.MODE_RECTANGLE(这里后续)

    /**
     * 设置是否分屏模式
     * @param isMonocular
     */
    void setMonocular(boolean isMonocular);

    /**
     * 获取是否分屏模式
     */
    boolean getMonocular();

    /**
     * 设置播放器渲染 UI View 对象
     * @param playerView
     */
    void setPlayerView(View playerView);

    /**
     * 拿到所设置的播放器渲染 UI View 对象
     * @return
     */
    View getPlayerView();

    /**
     * 创建一个播放器渲染的 UI View 对象
     * @param parent
     * @return
     */
    View onCreatePlayerView(ViewGroup parent);

    /**
     * 拿到事件管理者对象
     * @return
     */
    EventBus getEventBus();

    /**
     * 设置自定义事件管理者对象
     * @param eventBus
     */
    void setEventBus(EventBus eventBus);

}
