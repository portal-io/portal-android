package com.whaley.biz.playerui.playercontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.BufferingOffEvent;
import com.whaley.biz.playerui.event.BufferingUpdateEvent;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.InitEvent;
import com.whaley.biz.playerui.event.NewPlayDataContinueEvent;
import com.whaley.biz.playerui.event.PausedEvent;
import com.whaley.biz.playerui.event.PlayNextEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.ReplayEvent;
import com.whaley.biz.playerui.event.StartedEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.exception.IJKPlayerException;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.model.Repository;
import com.whaley.biz.playerui.model.Setting;
import com.whaley.biz.playerui.model.State;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.logger.Log;
import com.whaley.wvrplayer.vrplayer.external.VRMediaPlayer;
import com.whaley.wvrplayer.vrplayer.external.event.AbsVRPlayer;
import com.whaley.wvrplayer.vrplayer.external.event.DataParam;
import com.whaley.wvrplayer.vrplayer.external.event.callback.MediaPlayerListener;
import com.whaley.wvrplayer.vrplayer.external.view.VRPlayerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by YangZhi on 2017/8/1 11:27.
 */

public class PlayerController implements IPlayerController, MediaPlayerListener {

    private static volatile PlayerController INSTANCE;

    private VRMediaPlayer mediaPlayer;

    private EventBus eventBus;

    private State state;

    private Repository repository;

    private Context context;

    private View playerView;

    private int lastPlayerViewhashCode;

    public static IPlayerController getInstance(Context context) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (PlayerController.class) {
            if (INSTANCE == null) {
                INSTANCE = new PlayerController(context);
            }
        }
        return INSTANCE;
    }

    public static IPlayerController getInstance(Context context, EventBus eventBus) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (PlayerController.class) {
            if (INSTANCE == null) {
                INSTANCE = new PlayerController(context, eventBus);
            }
        }
        return INSTANCE;
    }

    private PlayerController(Context context) {
        this.context = context;
        repository = new Repository();
        state = new State();
        eventBus = EventBus.builder().build();
        eventBus.register(this);
        initMediaPlayer(context);
    }

    private PlayerController(Context context, EventBus eventBus) {
        this.context = context;
        repository = new Repository();
        state = new State();
        this.eventBus = eventBus;
        eventBus.register(this);
        initMediaPlayer(context);
    }

    private void initMediaPlayer(Context context) {
        mediaPlayer = new VRMediaPlayer(context);
        setDebug(Debug.isDebug());
        mediaPlayer.setMediaPlayerListener(this);
    }

    public VRMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void onFirstFrame(AbsVRPlayer absVRPlayer, String s) {

    }

    @Override
    public void onVideoPrepared(AbsVRPlayer absVRPlayer, String path) {
        PlayData playData = getRepository().getCurrentPlayData();
        if (playData == null || playData.getRealUrl() == null || !playData.getRealUrl().equals(path)) {
            // 如果准备完成的视频地址 和 设置的地址不一致 则暂停
            // 为了解决线程冲突造成的错误
            pause();
            return;
        }
        if (getPlayerView() != null) {
            lastPlayerViewhashCode = getPlayerView().hashCode();
        }
        getRepository().setVideoPrepared(true);
        getRepository().setDuration(getMediaPlayer().getDuration());
//        getRepository().setOnPreparedNetworkState(NetworkUtils.getNetworkState());
//        getRepository().setNetworkChangedAfterPrepared(false);
//        final boolean isCanStart=emitEvent(VideoPreparedEvent.class,path);
//
//        if (!isCanStart) {
//            //如果非显示状态则不播放
//            pause();
//            return;
//        }

        getRepository().setOnBuffering(false);
        VideoPreparedEvent videoPreparedEvent = new VideoPreparedEvent();
        videoPreparedEvent.setPlayData(playData);
        emitStickyEvent(videoPreparedEvent);

    }


    @Subscribe
    public void onVideoPreparedEvent(VideoPreparedEvent videoPreparedEvent) {
        changeState(State.STATE_STARTED);
    }

    @Override
    public void onBufferingUpdate(AbsVRPlayer absVRPlayer, int percent) {
        long duration = getRepository().getDuration();
        long secondProgress = (long) (duration * 1f * percent / 100);
        getRepository().setSecondProgress(secondProgress);
        if (getEventBus().hasSubscriberForEvent(BufferingUpdateEvent.class)) {
            BufferingUpdateEvent bufferingUpdateEvent = new BufferingUpdateEvent();
            bufferingUpdateEvent.setBufferProgress(secondProgress);
            emitEvent(bufferingUpdateEvent);
        }
    }

    @Override
    public void onBuffering(AbsVRPlayer absVRPlayer) {
        getRepository().setOnBuffering(true);
        BufferingEvent bufferingEvent = new BufferingEvent();
        emitEvent(bufferingEvent);
    }

    @Override
    public void onBufferingOff(AbsVRPlayer absVRPlayer) {
        getRepository().setOnBuffering(false);
        BufferingOffEvent bufferingOffEvent = new BufferingOffEvent();
        emitEvent(bufferingOffEvent);
        if (getRepository().isVideoPrepared() && !isStarted()) {
            changeState(State.STATE_STARTED);
        }
    }

    @Override
    public void onCompletion(AbsVRPlayer absVRPlayer) {
        if (getState().getCurrentState() == State.STATE_ERROR) {
            return;
        }
        complete();
    }

    @Override
    public boolean onError(AbsVRPlayer absVRPlayer, int i, String s) {
        setError(new IJKPlayerException(s + ",what type is " + i));
        return false;
    }


    //============================================================================================


    private void reset() {
        getRepository().reset();
        getEventBus().removeAllStickyEvents();
        changeState(State.STATE_INIT);

    }

    @Override
    public void setNewPlayData(PlayData playData) {
        reset();
        getRepository().convertCurrentPlayData(playData);
        changeState(State.STATE_PREPARING);
        prepareStartPlay();
    }

    @Subscribe
    public void onNewPlayDataEvent(NewPlayDataContinueEvent newPlayDataEvent) {
        start();
    }

    @Override
    public void setNewPlayDataContinue(PlayData playData) {
        if (getRepository().isVideoPrepared()) {
            PlayData currentPlayData = getRepository().getCurrentPlayData();
            if (currentPlayData != null && currentPlayData.equals(playData)) {
                NewPlayDataContinueEvent newPlayDataEvent = new NewPlayDataContinueEvent(playData);
                emitEvent(newPlayDataEvent);
                return;
            }
        }
        setNewPlayData(playData);
    }

    @Override
    public void setNewPlayData(List<PlayData> playDatas) {
        setNewPlayData(playDatas, 0);
    }

    @Override
    public void setNewPlayData(List<PlayData> playDatas, int startIndex) {
        reset();
        getRepository().convertCurrentPlayData(playDatas, startIndex);
        changeState(State.STATE_PREPARING);
        prepareStartPlay();
    }

    @Override
    public void prepareStartPlay() {
        PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
        prepareStartPlayEvent.setPlayData(getRepository().getCurrentPlayData());
        emitStickyEvent(prepareStartPlayEvent);
    }

    @Subscribe
    public void onPrepareStartEvent(PrepareStartPlayEvent prepareStartPlayEvent) {
        startPlay(prepareStartPlayEvent.getPlayData());
    }

    @Override
    public void startPlay(PlayData playData) {
        Log.d("PlayerLog", "startPlay playdata =" + playData.toString());
        DataParam dataParam = playData.convert();
        getMediaPlayer().setParamAndPlay(dataParam);
    }

    @Override
    public void reStartPlay() {
        reStartPlay(false);
    }

    @Override
    public void reStartPlay(boolean isResetState) {
        changeState(State.STATE_PREPARING);
        if (isResetState || !getRepository().isVideoPrepared()) {
            prepareStartPlay();
        } else {
            PlayData playData = getRepository().getCurrentPlayData();
            playData.setProgress(getMediaPlayer().getCurrentPosition());
            prepareStartPlay();
        }
    }

    @Override
    public void complete() {
        changeState(State.STATE_COMPLETED);
    }

    @Override
    public void setError(int erroCode, String msg) {
        setError(new PlayerException(erroCode, msg));
    }

    @Override
    public void setError(PlayerException error) {
        getRepository().setPlayerException(error);
        changeState(State.STATE_ERROR);
    }

    @Override
    public void restError() {
        getRepository().setPlayerException(null);
        getEventBus().removeStickyEvent(ErrorEvent.class);
    }

    @Override
    public void start() {
        if (!getRepository().isVideoPrepared())
            return;
        if (getPlayerView() != null) {
            int hashCode = getPlayerView().hashCode();
            if (hashCode != lastPlayerViewhashCode) {
                getMediaPlayer().seekTo(getMediaPlayer().getCurrentPosition());
                lastPlayerViewhashCode = hashCode;
            } else {
                getMediaPlayer().start();
            }
        } else {
            getMediaPlayer().start();
        }
        changeState(State.STATE_STARTED);
    }

    @Override
    public void replay() {
        changeState(State.STATE_PREPARING);
        getRepository().getCurrentPlayData().setProgress(0);
        getRepository().setOnBuffering(false);
        prepareStartPlay();
    }

    @Override
    public void playNext() {
        if (getRepository().hasNextPlayData()) {
            getRepository().changeToNextPlayData();
            changeState(State.STATE_PREPARING);
            prepareStartPlay();
        }
    }

    @Override
    public void pause() {
        if (!getRepository().isVideoPrepared())
            return;
        getMediaPlayer().pause();
        changeState(State.STATE_PAUSED);
    }

    @Override
    public void release() {
        reset();
        getMediaPlayer().releasePlayer();
        this.playerView = null;
    }

    @Override
    public boolean isReleased() {
        return getState().getCurrentState() == State.STATE_INIT;
    }

    @Override
    public boolean isStarted() {
        return getState().getCurrentState() == State.STATE_STARTED;
    }

    @Override
    public boolean isRealPaused() {
        return getState().getCurrentState() == State.STATE_PAUSED;
    }

    @Override
    public boolean isPaused() {
        return getMediaPlayer().isPaused();
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public Repository getRepository() {
        return repository;
    }

    @Override
    public Setting getSetting() {
        return null;
    }

    @Override
    public float getFps() {
        return getMediaPlayer().getFps();
    }

    @Override
    public long getTcpSpeed() {
        return getMediaPlayer().getTcpSpeed();
    }

    @Override
    public void orientationReset() {
        getMediaPlayer().orientationReset();
    }

    @Override
    public int getDeviceOrientation() {
        return getMediaPlayer().getDeviceOrientation();
    }

    @Override
    public void setDebug(boolean debug) {
        getMediaPlayer().isLogger(debug);
    }

    @Override
    public void setFov(float var1) {

    }

    @Override
    public float getFov() {
        return 0;
    }

    @Override
    public long getCurrentProgress() {
        return getMediaPlayer().getCurrentPosition();
    }

    @Override
    public void changeCurentProgress(long progress) {
        getMediaPlayer().seekTo(progress);
    }

    @Override
    public void setBottomOverlayView(float scale, Bitmap bitmap) {
        getMediaPlayer().setBottomOverlayView(scale, bitmap);
    }

    @Override
    public void setBackgroundImage(Bitmap backgroundImage) {
        getMediaPlayer().setBackImageView(backgroundImage);
    }

    @Override
    public void setRenderType(int renderType) {
        getMediaPlayer().setRenderType(renderType);
    }

    @Override
    public void setMonocular(boolean isMonocular) {
        getMediaPlayer().setMonocular(isMonocular);
    }

    @Override
    public void setPlayerView(View playerView) {
        getMediaPlayer().setPlayerView((VRPlayerView) playerView);
        this.playerView = playerView;
    }

    @Override
    public View getPlayerView() {
        return playerView;
    }

    @Override
    public View onCreatePlayerView(ViewGroup parent) {
        VRPlayerView playerView = new VRPlayerView(parent.getContext());
        playerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return playerView;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public void setEventBus(EventBus eventBus) {
        if (this.eventBus.isRegistered(this)) {
            this.eventBus.unregister(this);
        }
        this.eventBus = eventBus;
        if (!this.eventBus.isRegistered(this)) {
            this.eventBus.register(this);
        }
    }

    private void emitEvent(Object event) {
        getEventBus().post(event);
    }

    private void emitStickyEvent(Object event) {
        getEventBus().postSticky(event);
    }

    private void changeState(int state) {
        if (!getState().changeState(state)) {
            return;
        }

        Object event = null;
        getEventBus().removeStickyEvent(InitEvent.class);
        getEventBus().removeStickyEvent(PreparingEvent.class);
        getEventBus().removeStickyEvent(StartedEvent.class);
        getEventBus().removeStickyEvent(PausedEvent.class);
        getEventBus().removeStickyEvent(PlayNextEvent.class);
        getEventBus().removeStickyEvent(ReplayEvent.class);
        getEventBus().removeStickyEvent(CompletedEvent.class);
        getEventBus().removeStickyEvent(ErrorEvent.class);
        switch (state) {
            case State.STATE_INIT:
                InitEvent initEvent = new InitEvent();
                event = initEvent;
                break;
            case State.STATE_PREPARING:
                PreparingEvent preparingEvent = new PreparingEvent();
                preparingEvent.setPlayData(getRepository().getCurrentPlayData());
                event = preparingEvent;
                break;
            case State.STATE_STARTED:
                StartedEvent startedEvent = new StartedEvent();
                startedEvent.setPlayData(getRepository().getCurrentPlayData());
                event = startedEvent;
                break;
            case State.STATE_PAUSED:
                PausedEvent pausedEvent = new PausedEvent();
                pausedEvent.setPlayData(getRepository().getCurrentPlayData());
                event = pausedEvent;
                break;
//            case State.STATE_PLAY_NEXT:
//                PlayNextEvent playNextEvent = new PlayNextEvent();
//                playNextEvent.setPlayData(getRepository().getCurrentPlayData());
//                event = playNextEvent;
//                break;
//            case State.STATE_PLAY_REPLAY:
//                ReplayEvent replayEvent = new ReplayEvent();
//                replayEvent.setPlayData(getRepository().getCurrentPlayData());
//                event = replayEvent;
//                break;
            case State.STATE_COMPLETED:
                CompletedEvent completedEvent = new CompletedEvent();
                completedEvent.setPlayData(getRepository().getCurrentPlayData());
                event = completedEvent;
                break;
            case State.STATE_ERROR:
                ErrorEvent errorEvent = new ErrorEvent();
                errorEvent.setPlayerException(getRepository().getPlayerException());
                event = errorEvent;
                break;
        }

        if (event != null) {
            emitStickyEvent(event);
        }
    }

    public Context getContext() {
        return context;
    }
}
