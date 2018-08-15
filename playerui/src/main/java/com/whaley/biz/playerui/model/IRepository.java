package com.whaley.biz.playerui.model;

import android.support.v4.util.SimpleArrayMap;

import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.playerui.playercontroller.IPlayerController;

/**
 * Created by YangZhi on 2017/4/23 6:52.
 */

public interface IRepository {

    /**
     * 获得当前播放的播放数据
     * @return
     */
    PlayData getCurrentPlayData();

    /**
     * 获得是否有下一个视频
     * @return
     */
    boolean hasNextPlayData();

    /**
     * 获得下一个视频的播放数据
     * @return
     */
    PlayData getNextPlayData();

    /**
     * 判断当前是否在缓冲状态
     * @return
     */
    boolean isOnBuffering();

    /**
     * 获得当前视频的总进度
     * @return
     */
    long getDuration();

    /**
     * 获得当前视频的缓冲进度
     * @return
     */
    long getSecondProgress();

    /**
     * 获得当前的播放进度,该方法暂不可用,可通过 {@link IPlayerController#getCurrentProgress()} 获取
     * @return
     */
    long getCurrentProgress();

    /**
     * 判断当前是否是 start 状态
     * @return
     */
    boolean isStarted();

    /**
     * 判断当前视频是否已经数据准备完成
     * @return
     */
    boolean isVideoPrepared();

    /**
     * 获得错误的 Exception
     * @return
     */
    PlayerException getPlayerException();

    /**
     * 设置当前视频是否正在缓冲
     * @return
     */
    void setOnBuffering(boolean onBuffering);

}
