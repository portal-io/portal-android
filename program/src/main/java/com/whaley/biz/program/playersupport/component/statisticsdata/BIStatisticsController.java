package com.whaley.biz.program.playersupport.component.statisticsdata;

import android.content.res.Configuration;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.ActivityPauseEvent;
import com.whaley.biz.program.model.ProgramDramaDetailModel;
import com.whaley.biz.program.playersupport.event.ActivityResultEvent;
import com.whaley.biz.playerui.event.ActivityResumeEvent;
import com.whaley.biz.playerui.event.BufferingEvent;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PausedEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.StartedEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ServerRenderType;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.playersupport.exception.ProgramErrorConstants;
import com.whaley.biz.program.playersupport.model.DefinitionModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Router;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Author: qxw
 * Date:2017/9/22
 * Introduction:
 */

public class BIStatisticsController extends BaseController implements BIConstants {
    private static final String TAG = "BIStatisticsController";
    private boolean isbanner = false;
    private boolean isDrama = false;
    public static final String BUFFERING_AMOUNT = "totalBufferingAmount";


    private String id;
    /**
     * 启播时间
     */
    protected long startPlayDurationTime;

    /**
     * 播放开始时间
     */
    private long startPlayTime;

    /**
     * 暂停时的时间
     */
    private long timeOut;

    /**
     * 暂停时的总时间
     */
    private long totalTimeOut;

    /**
     * 是否视频已准备好开始播放了
     */
    private boolean isVideoPrepared;
    /**
     * 已缓冲的时间
     */
    private int bufferingTime;

    private long startBufferingTime;

    /**
     * 缓冲总时间
     */
    private long totalBufferingTime;

    /**
     * 缓冲总量
     */
    private int totalBufferingAmount;

    /**
     * 帧率计时
     */
    private int fpsTime = -1;

    private boolean isback = false;

    private boolean isPlayComplete = false;

    private boolean isBrowsed = false;

    private boolean isError = false;

    private long startBrowsedDurationTime;

    private boolean isUnity;

    private boolean isFullscreen;

    private boolean isShare;


    public BIStatisticsController(boolean isbanner) {
        this.isbanner = isbanner;
    }

    public BIStatisticsController(boolean isbanner, boolean isDrama) {
        this.isbanner = isbanner;
        this.isDrama = isDrama;
    }

    @Override
    public void registEvents() {
        if (isbanner) {
            isVideoPrepared = true;
            isPlayComplete = false;
            if (getPlayData() != null)
                id = getPlayData().getId();
            startBrowsedDurationTime = System.currentTimeMillis();
            if (!isShare) {
                browseLiveView(false);
            }
            onStartedPlay();
            if (!isFullscreen)
                started();
            isFullscreen = true;
            isUnity = false;
        }
        super.registEvents();
        isShare = false;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void unRegistEvents() {
        if (isbanner) {
            isVideoPrepared = false;
            isPlayComplete = true;
            onPlayComplete();
            browseDurationLiveView();
        }
        super.unRegistEvents();
        if (isbanner && isShare) {
            return;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onEvent(BaseEvent event) {
        if ("/unity/service/player".equals(event.getEventType())) {
            isUnity = true;
        }
        if (isbanner && "program/event/switchfullscreentobanner".equals(event.getEventType())) {
            isFullscreen = false;
        }
        if (isbanner && "/share/service/sharetemp".equals(event.getEventType())) {
            isShare = true;
        }
        if (isbanner && isShare && "/share/service/share".equals(event.getEventType())) {
            browseLiveView(false);
        }
    }

    @Subscribe
    public void onActivityResumeEvent(ActivityResumeEvent activityResumeEvent) {
        startBrowsedDurationTime = System.currentTimeMillis();
        if (!isbanner && isUnity) {
            browseLiveView(false);
        }
        isUnity = false;
        if (isVideoPrepared && !isPlayComplete) {
            onStartedPlay();
        }
    }

    @Subscribe
    public void onActivityPauseEvent(ActivityPauseEvent activityPauseEvent) {
        if (!isPlayComplete && isVideoPrepared) {
            onPlayComplete();
        }
        browseDurationLiveView();
    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent activityResultEvent) {
        browseLiveView(false);
    }

    @Subscribe(priority = Integer.MAX_VALUE)
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent) {
        if (prepareStartPlayEvent.getMaxPriority() != 99)
            return;
        browseLiveView(true);
    }

    @Subscribe
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        timeOut = 0;
        Log.e(TAG, "id=" + id + " ,playDataID=" + videoPreparedEvent.getPlayData().getId());
        if (StrUtil.isEmpty(id) || !id.equals(videoPreparedEvent.getPlayData().getId())) {
            isVideoPrepared = true;
            isPlayComplete = false;
            id = videoPreparedEvent.getPlayData().getId();
            if (!isError) {
                started();
            } else {
                isError = false;
            }
            onStartedPlay();
        }

    }


    private void started() {
        totalBufferingTime = 0;
        LogInfoParam.Builder builder = getGeneralBuilder(ACTION_TYPE_START_PLAY);
        if (builder != null) {
            setPosition(builder);
            builder.putEventPropKeyValue(EVENT_PROP_KEY_START_PLAY_DURATION, isbanner ? 0 : String.valueOf(System.currentTimeMillis() - startPlayDurationTime));
            saveLogInfo(builder);
        }
    }

    @Subscribe
    public void onCompleted(CompletedEvent completedEvent) {
        if (!isDrama) {
            id = null;
            isVideoPrepared = false;
            isPlayComplete = true;
            onPlayComplete();
        }
    }

    @Subscribe
    public void onDramaCompleted(ModuleEvent moduleEvent) {
        if ("drama_completed".equals(moduleEvent.getEventName())) {
            id = null;
            isVideoPrepared = false;
            isPlayComplete = true;
            onPlayComplete();
        }
    }

    @Subscribe
    public void onError(ErrorEvent errorEvent) {
        if (startPlayTime != 0) {
            id = null;
            isVideoPrepared = false;
            isPlayComplete = true;
            isError = true;
            onPlayComplete();
        }
    }

    @Subscribe
    public void onStarted(StartedEvent startedEvent) {
        onPlayResume();
    }

    @Subscribe
    public void onPaused(PausedEvent pausedEvent) {
        onPlayPause();
    }

    @Subscribe
    public void onBufferingOffEvent(ModuleEvent event) {
        if (BUFFERING_AMOUNT.equals(event.getEventName())) {
            totalBufferingAmount = (int) event.getParam();
            endbuffer();
        }
    }


    @Subscribe
    public void onBufferingEvent(BufferingEvent event) {
        startBuffer();
    }

//    @Subscribe
//    public void onPollingEvent(PollingEvent pollingEvent) {
//        caculatePlayFrame();
//    }

    @Override
    protected void onInit() {
        super.onInit();
        if (isbanner) {
            return;
        }
        startPlayDurationTime = System.currentTimeMillis();
        browseLiveView(false);
    }
//
//    /**
//     * @author qxw
//     * 计算播放帧率
//     * @time 2017/2/21 16:10
//     */
//
//    private void caculatePlayFrame() {
//        if (isVideoPrepared && timeOut == 0 && !isPlayComplete) {
//            if (fpsTime != -1) {
//                float v = getPlayerController().getFps();
//                if (v < 15 && fpsTime >= 5) {
//                    lowbitrate(v);
//                    fpsTime = 0;
//                }
//            }
//            if (fpsTime < 5)
//                fpsTime++;
//        }
//    }


    /**
     * 起播点埋点
     */
    private void onStartedPlay() {
//        LogInfoParam.Builder builder = getGeneralBuilder(ACTION_TYPE_START_PLAY);
//        if (builder != null) {
        startPlayTime = System.currentTimeMillis();
//            setPosition(builder);
//            builder.putEventPropKeyValue(EVENT_PROP_KEY_START_PLAY_DURATION, String.valueOf(System.currentTimeMillis() - startPlayDurationTime));
//            saveLogInfo(builder);
        Log.d(TAG, "onStartedPlay");
        isPlayComplete = false;
        totalBufferingTime = 0;
        totalTimeOut = 0;
        fpsTime = -1;
//        }
//        if (reportController != null && getViewData().getVrPanPlayerBean() != null) {
//            reportController.reportEventIn(getGoodsType(), getViewData().getVrPanPlayerBean().itemid);
//        }
    }

    /**
     * 播放完成
     */
    protected void onPlayComplete() {
        LogInfoParam.Builder builder = getGeneralBuilder(ACTION_TYPE_END_PLAY);
        if (builder != null && startPlayTime != 0) {
            setDuration(builder, playTime());
            setExitType(builder, EXIT_TYPE_SELFEND);
            saveLogInfo(builder);
            Log.d(TAG, "onPlayComplete");
            timeOut = 0;
            totalBufferingTime = 0;
            totalTimeOut = 0;
            fpsTime = -1;
            startPlayTime = 0;
        }
//        if (reportController != null && getViewData().getVrPanPlayerBean() != null) {
//            reportController.reportEventOut(getGoodsType(), getViewData().getVrPanPlayerBean().itemid);
//        }
    }


//    /**
//     * 播放出现错误的时候
//     */
//    private void onPlayError(int code) {
//        LogInfoParam.Builder builder = getGeneralBuilder(ACTION_TYPE_END_PLAY);
//        if (builder != null) {
//            if (startPlayTime != 0) {
//                setDuration(builder, playTime());
//                setExitType(builder, EXIT_TYPE_PLAYERROR + " " + code);
//            } else {
//                setExitType(builder, EXIT_TYPE_START_PLAYERROR + " " + code);
//            }
//            saveLogInfo(builder);
//            Log.d(TAG, "onPlayError");
//            id = null;
//            totalBufferingTime = 0;
//            isVideoPrepared = false;
//            isPlayComplete = true;
//            totalTimeOut = 0;
//            fpsTime = -1;
//            startPlayTime = 0;
//        }
//    }


    /**
     * 播放
     */
    public void onPlayResume() {
//        LogInfoParam.Builder builder = getGeneralBuilder(ACTION_TYPE_CONTINUE);
//        if (builder != null && timeOut != 0) {
//            timeOut = System.currentTimeMillis() - timeOut;
//            setDuration(builder, timeOut);
//            saveLogInfo(builder);
//            Log.d(TAG, "onPlayResume");
//            totalTimeOut += timeOut;
//            timeOut = 0;
//
//        }
        if (timeOut != 0) {
            timeOut = System.currentTimeMillis() - timeOut;
            Log.d(TAG, "onPlayResume");
            totalTimeOut += timeOut;
            timeOut = 0;

        }
    }

    /**
     * 暂停
     */
    public void onPlayPause() {
        if (!isback && startPlayTime != 0) {
            timeOut = System.currentTimeMillis();
//            LogInfoParam.Builder builder = getGeneralBuilder(ACTION_TYPE_PAUSE);
//            if (builder != null) {
//                setPosition(builder);
//                saveLogInfo(builder);
//                Log.d(TAG, "onPlayPause");
//            }
        }
    }

    /**
     * @author qxw
     * 开始缓冲
     * @time 2017/2/21 14:24
     */
    public void startBuffer() {
        if (getPlayData() == null || getPlayData().getType() == ProgramConstants.TYPE_PLAY_LOCALVIDEO) {
            return;
        }
        startBufferingTime = System.currentTimeMillis();
        totalBufferingAmount = 0;
//        LogInfoParam.Builder builder = getGeneralBuilder(ACTION_TYPE_START_BUFFER);
//        if (builder != null) {
//            startBufferingTime = System.currentTimeMillis();
//            setPosition(builder);
//            saveLogInfo(builder);
//            Log.d(TAG, "startBuffer");
//            totalBufferingAmount = 0;
//        }
    }

    /**
     * @author qxw
     * 缓冲结束
     * @time 2017/2/21 11:16
     */
    private void endbuffer() {
        if (getPlayData() == null || getPlayData().getType() == ProgramConstants.TYPE_PLAY_LOCALVIDEO) {
            return;
        }
        if (startBufferingTime != 0) {
            startBufferingTime = System.currentTimeMillis() - startBufferingTime;
            totalBufferingTime += startBufferingTime;
            startBufferingTime = 0;
        }
//        LogInfoParam.Builder builder = getGeneralBuilder(ACTION_TYPE_END_BUFFER);
//        if (builder != null && startBufferingTime != 0) {
//            startBufferingTime = System.currentTimeMillis() - startBufferingTime;
//            setDuration(builder, startBufferingTime);
//            builder.putEventPropKeyValue(EVENT_PROP_KEY_BUFFER_DATA, String.valueOf(totalBufferingAmount / 1024.0));
//            saveLogInfo(builder);
//            Log.d(TAG, "endbuffer");
//            totalBufferingTime += startBufferingTime;
//            startBufferingTime = 0;
//        }
    }

//    /**
//     * @author qxw
//     * 低码率
//     * @time 2017/2/21 11:16
//     */
//    private void lowbitrate(float fps) {
//        LogInfoParam.Builder builder = getGeneralBuilder(ACTION_TYPE_LOWBITRATE);
//        if (builder != null) {
//            setPosition(builder);
//            builder.putEventPropKeyValue(EVENT_PROP_KEY_BITRATE, String.valueOf(fps));
//            saveLogInfo(builder);
//            Log.d(TAG, "lowbitrate");
//        }
//    }

    /**
     * @author qxw
     * 设置视频当前位置
     * @time 2017/2/20 19:58
     */
    private void setPosition(LogInfoParam.Builder builder) {
        builder.putEventPropKeyValue(EVENT_PROP_KEY_POSITION, String.valueOf(getPlayerController().getCurrentProgress()));
    }

    private long playTime() {
        return System.currentTimeMillis() - startPlayTime - totalBufferingTime - totalTimeOut;
    }

    private void setExitType(LogInfoParam.Builder builder, String msg) {
        builder.putEventPropKeyValue(EVENT_PROP_KEY_EXIT_TYPE, msg);
    }

    /**
     * @author qxw
     * 设置时长
     * @time 2017/2/20 19:58
     */
    private void setDuration(LogInfoParam.Builder builder, long time) {
        builder.putEventPropKeyValue(EVENT_PROP_KEY_DURATION, String.valueOf(time));
    }

    /**
     * 普通事件上传
     *
     * @param actionType
     */
    private LogInfoParam.Builder getGeneralBuilder(String actionType) {
        PlayData playData = getPlayData();
        if (playData != null) {
            DefinitionModel definitionModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.CURRENT_DEFINITION_MODEL);
            int definition = -999;
            if (definitionModel != null) {
                definition = definitionModel.getDefinition();
            }
            String serverRenderType = playData.getCustomData(PlayerDataConstants.SERVER_RENDER_TYPE);
            String isChargeable = "0";
            if (getPlayData().getCustomData(PlayerDataConstants.IS_CHARGEABLE) != null) {
                isChargeable = String.valueOf(getPlayData().getCustomData(PlayerDataConstants.IS_CHARGEABLE));
            }
            LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId(ROOT_PLAY)
                    .setCurrentPageId(ROOT_PLAY)
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, playData.getTitle())
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, playData.getId())
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_TYPE, getBIVideoType(playData.getType()))
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_FORMAT, getBIVideoFormat(playData.getType()))
                    .putCurrentPagePropKeyValue(CURRENT_PROP_SCREEM_TYPE, getBIFullScreen(playData.getType()))
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_TAGS, playData.getCustomData(PlayerDataConstants.TAG))
                    .putCurrentPagePropKeyValue(CURRENT_PROP_CUR_BIT_TYPE, getBICurBitType(definition, playData.getType()))
                    .putCurrentPagePropKeyValue(CURRENT_PROP_CONTENT_TYPE, getPlayData().getCustomData(PlayerDataConstants.CONTENT_TYPE))
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_CHARGEABLE, isChargeable)
                    .putEventPropKeyValue(EVENT_PROP_KEY_ACTION_TYPE, actionType);
            if (isPlayMode(playData.getType())) {
                builder.putEventPropKeyValue(EVENT_PROP_KEY_PLAY_MODE, getPlayMode(serverRenderType, definition, playData));
            }
            if (isDrama) {
                ProgramDramaDetailModel programDramaDetailModel = getPlayData().getCustomData(PlayerDataConstants.DRAMA_INFO);
                if (programDramaDetailModel != null && ACTION_TYPE_END_PLAY.equals(actionType)) {
                    builder.putEventPropKeyValue(CURRENT_PROP_TOTAL_TIME, String.valueOf(programDramaDetailModel.getDuration() * 1000));
                }
            }
            if (actionType.equals(ACTION_TYPE_END_PLAY)) {
                builder.setNextPageId(NEXT_PAGEID_DETAIL);
            } else {
                builder.setNextPageId(NEXT_PAGEID_PLAY);
            }
            return builder;
        }
        return null;
    }


    private String getPlayMode(String renderType, int videoBitType, PlayData playData) {
        if (ProgramConstants.TYPE_PLAY_LOCALVIDEO == playData.getType()) {
            renderType = playData.getCustomData(PlayerDataConstants.CURRENT_RENDER_TYPE);
            if (StrUtil.isEmpty(renderType)) {
                renderType = ServerRenderType.RENDER_TYPE_360_2D;
            }
            return renderType;
        }
        if (videoBitType == VideoBitType.SD || videoBitType == VideoBitType.SDA || videoBitType == VideoBitType.SDB) {
            return ServerRenderType.RENDER_TYPE_360_2D_OCTAHEDRAL;
        }
        if (StrUtil.isEmpty(renderType)) {
            if (PlayerType.TYPE_3D == playData.getType()) {
                return ServerRenderType.RENDER_TYPE_PLANE_3D_LR;
            } else {
                return ServerRenderType.RENDER_TYPE_360_2D;
            }
        }
        return renderType;
    }

    private String getBICurBitType(int definition, int type) {
        String curBitType;
        if (ProgramConstants.TYPE_PLAY_LOCALVIDEO == type) {
            curBitType = CUR_BIT_TYPE_LOCAL;
        } else {
            switch (definition) {
                case VideoBitType.ST:
                    curBitType = CUR_BIT_TYPE_HD;
                    break;
                case VideoBitType.SD:
                    curBitType = CUR_BIT_TYPE_SD;
                    break;
                case VideoBitType.HD:
                    curBitType = CUR_BIT_TYPE_4K;
                    break;
                case VideoBitType.SDA:
                    curBitType = CUR_BIT_TYPE_SDA;
                    break;
                case VideoBitType.SDB:
                    curBitType = CUR_BIT_TYPE_SDB;
                    break;
                case VideoBitType.TDA:
                    curBitType = CUR_BIT_TYPE_TDA;
                    break;
                case VideoBitType.TDB:
                    curBitType = CUR_BIT_TYPE_TDB;
                    break;
                default:
                    curBitType = "";
                    break;
            }
        }
//        Log.d(TAG, "curBitType=" + curBitType + " ,type=" + type + " ,definition=" + definition);
        return curBitType;
    }

    private boolean isPlayMode(int type) {
        switch (type) {
            case ProgramConstants.TYPE_PLAY_PANO:
            case ProgramConstants.TYPE_PLAY_LOCALVIDEO:
                return true;
            default:
                return false;
        }
    }

    private String getBIVideoType(int type) {
        String videoType = "";
        if (isDrama) {
            return ProgramConstants.TYPE_DYNAMIC;
        }
        switch (type) {
            case ProgramConstants.TYPE_PLAY_LIVE:
                videoType = "live";
                break;
            case ProgramConstants.TYPE_PLAY_PANO:
                videoType = "VR";
                break;
            case ProgramConstants.TYPE_PLAY_3D:
                videoType = "3D";
                break;
            case ProgramConstants.TYPE_PLAY_MORETV_2D:
                videoType = "moretv_movie";
                break;
            case ProgramConstants.TYPE_PLAY_MORETV_TV:
                videoType = "moretv_tv";
                break;
            case ProgramConstants.TYPE_PLAY_LOCALVIDEO:
                videoType = "local_video";
                break;
        }
        return videoType;
    }

    private String getBIVideoFormat(int type) {
        String videoFormat = "";
        switch (type) {
            case ProgramConstants.TYPE_PLAY_LIVE:
            case ProgramConstants.TYPE_PLAY_PANO:
                videoFormat = "vr";
                break;
            case ProgramConstants.TYPE_PLAY_3D:
                videoFormat = "3d";
                break;
            case ProgramConstants.TYPE_PLAY_MORETV_2D:
            case ProgramConstants.TYPE_PLAY_MORETV_TV:
                videoFormat = "2d";
                break;
            default:
                videoFormat = "vr";
        }
        return videoFormat;
    }

    private PlayData getPlayData() {
        if (getPlayerController() != null && getPlayerController().getRepository() != null)
            return getPlayerController().getRepository().getCurrentPlayData();
        return null;
    }

    private String getBIFullScreen(int type) {
        String fullScreen;
        switch (type) {
            case ProgramConstants.TYPE_PLAY_LIVE:
                fullScreen = SCREEM_TYPE_1;
                break;
            case ProgramConstants.TYPE_PLAY_LOCALVIDEO:
                fullScreen = SCREEM_TYPE_1;
                break;
            case ProgramConstants.TYPE_PLAY_PANO:
            case ProgramConstants.TYPE_PLAY_3D:
            case ProgramConstants.TYPE_PLAY_MORETV_2D:
            case ProgramConstants.TYPE_PLAY_MORETV_TV:
            default:
                if (isLandscape()) {
                    fullScreen = SCREEM_TYPE_1;
                } else {
                    fullScreen = SCREEM_TYPE_2;
                }
                break;
        }
//        Log.d(TAG, "fullScreen=" + fullScreen + " ,isbanner=" + isbanner + " ,orientation=" + getActivity().getRequestedOrientation() + " ,type=" + type);
        return fullScreen;
    }

    private void saveLogInfo(LogInfoParam.Builder builder) {
        Log.d(TAG, builder.toString());
        Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder).excute();
    }

    @Override
    protected void onDestory() {
        if (!isPlayComplete && isVideoPrepared)
            onPlayComplete();
        if (!isbanner)
            browseDurationLiveView();
        super.onDestory();
    }

    private boolean isLandscape() {
        return AppContextProvider.getInstance().getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    /**
     * 直播 浏览事件
     */
    private void browseLiveView(boolean isFromFetch) {
        if (getPlayData() == null || getPlayData().getType() != ProgramConstants.TYPE_PLAY_LIVE) {
            return;
        }
        if (isFromFetch) {
            if (isBrowsed) {
                return;
            } else {
                isBrowsed = true;
            }
        }
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(BROWSE_VIEW)
                .setCurrentPageId(ROOT_LIVE_DETAILS)
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, getPlayData().getId())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, getPlayData().getTitle())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_FORMAT, "vr")
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_CHARGEABLE, String.valueOf(getPlayData().getCustomData(PlayerDataConstants.IS_CHARGEABLE)))
                .putCurrentPagePropKeyValue(CURRENT_PROP_CONTENT_TYPE, getPlayData().getCustomData(PlayerDataConstants.CONTENT_TYPE))
                .setNextPageId(ROOT_LIVE_DETAILS);
        if (builder != null) {
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    /**
     * 直播 浏览时长
     */
    private void browseDurationLiveView() {
        if (getPlayData() == null || getPlayData().getType() != ProgramConstants.TYPE_PLAY_LIVE) {
            return;
        }
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(BROWSE_DURATION)
                .setCurrentPageId(ROOT_LIVE_DETAILS)
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, getPlayData().getId())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, getPlayData().getTitle())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_FORMAT, "vr")
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_CHARGEABLE, String.valueOf(getPlayData().getCustomData(PlayerDataConstants.IS_CHARGEABLE)))
                .putCurrentPagePropKeyValue(CURRENT_PROP_CONTENT_TYPE, getPlayData().getCustomData(PlayerDataConstants.CONTENT_TYPE))
                .putEventPropKeyValue(EVENT_PROP_KEY_ACTION_TYPE, ACTION_TYPE_END_BROWSE)
                .putEventPropKeyValue(EVENT_PROP_KEY_DURATION, String.valueOf(System.currentTimeMillis() - startBrowsedDurationTime));
        if (builder != null) {
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

}