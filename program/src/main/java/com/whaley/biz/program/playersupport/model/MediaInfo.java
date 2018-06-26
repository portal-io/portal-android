package com.whaley.biz.program.playersupport.model;

import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;

import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ServerRenderType;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.model.NodeModel;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.playersupport.component.normalplayer.resolvedefinition.RenderTypeUtil;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/29.
 */

public class MediaInfo {

    private String title; //节目名称
    private String sid; //节目code
    private float progress ; //节目播放时间（单位:秒）
    private String movieMode; //视频类型（NORMAL->普通，SPHERE ->全景,SEMIPHERE->半球）
    private String movieFormat; //视频格式（"2D"->平面， "3DUD"=>3D 上下，"3DLR"=>3D 左右）
    private String movieSource; //视频来源（“LIVE”->正在直播, “ONLINE”->“在线视频”，“OFFLINE”->“本地视频”，“DRAMA”->“互动剧”）
    private List<PathInfo> path;
    private String movieQuality; //节目清晰度（0,1,2 对应url次序）
    private String videoTag;
    private String videoType;
    private long duration;
    private DramaInfo dramaInfo;
    private int playCount;
    private PayModel payModel;
    private int isChargeable;
    private String videoFormat;
    private String contentType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getMovieMode() {
        return movieMode;
    }

    public void setMovieMode(String movieMode) {
        this.movieMode = movieMode;
    }

    public String getMovieFormat() {
        return movieFormat;
    }

    public void setMovieFormat(String movieFormat) {
        this.movieFormat = movieFormat;
    }

    public String getMovieSource() {
        return movieSource;
    }

    public void setMovieSource(String movieSource) {
        this.movieSource = movieSource;
    }

    public List<PathInfo> getPath() {
        return path;
    }

    public void setPath(List<PathInfo> path) {
        this.path = path;
    }

    public String getMovieQuality() {
        return movieQuality;
    }

    public void setMovieQuality(String movieQuality) {
        this.movieQuality = movieQuality;
    }

    public String getVideoTag() {
        return videoTag;
    }

    public void setVideoTag(String videoTag) {
        this.videoTag = videoTag;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public static void convertPayModel(MediaInfo mediaInfo, PlayData playData, boolean isLive){
        if(mediaInfo!=null){
            PayModel payModel = new PayModel();
            if(isLive){
                LiveDetailsModel liveDetailsModel = playData.getCustomData(PlayerDataConstants.LIVE_INFO);
                payModel.setFreeTime(liveDetailsModel.getFreeTime());
                payModel.setCouponDto(liveDetailsModel.getCouponDto());
                payModel.setContentPackageQueryDtos(liveDetailsModel.getContentPackageQueryDtos());
                if(playData.getCustomData(PlayerDataConstants.PLAY_FREE_TIME)!=null){
                    int playTime = playData.getCustomData(PlayerDataConstants.PLAY_FREE_TIME);
                    payModel.setPlayTime(playTime);
                }
            }else{
                ProgramDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.PROGRAM_INFO);
                payModel.setFreeTime(programDetailModel.getFreeTime());
                payModel.setCouponDto(programDetailModel.getCouponDto());
                payModel.setContentPackageQueryDtos(programDetailModel.getContentPackageQueryDtos());
            }
            mediaInfo.setPayModel(payModel);
        }
    }

    public static Observable<MediaInfo> convertByPlayData(final PlayData playData, final boolean isLive, final boolean isFree){
        return Observable.create(new ObservableOnSubscribe<MediaInfo>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<MediaInfo> e) throws Exception {
                MediaInfo mediaInfo = new MediaInfo();
                mediaInfo.setSid(playData.getId());
                mediaInfo.setTitle(playData.getTitle());
                boolean isPay = false;
                int isChargeable = 0;
                if(playData.getCustomData(PlayerDataConstants.IS_CHARGEABLE) != null){
                    isChargeable = playData.getCustomData(PlayerDataConstants.IS_CHARGEABLE);
                }
                mediaInfo.setIsChargeable(isChargeable);
                String contentType = "";
                if(playData.getCustomData(PlayerDataConstants.CONTENT_TYPE) != null){
                    contentType = playData.getCustomData(PlayerDataConstants.CONTENT_TYPE);
                }
                mediaInfo.setContentType(contentType);
                String videoFormat;
                if(isLive){
                    videoFormat = "vr";
                }else{
                    videoFormat = getBIVideoFormat(playData.getType());
                }
                mediaInfo.setVideoFormat(videoFormat);
                if(isFree || isChargeable == 0){
                    isPay = true;
                }else if(playData.getCustomData(PlayerDataConstants.IS_PAY)!=null){
                    isPay =  playData.getCustomData(PlayerDataConstants.IS_PAY);
                }
                if(!isPay){
                    convertPayModel(mediaInfo, playData, isLive);
                }
                String serverRenderType;
                if (ProgramConstants.TYPE_PLAY_LOCALVIDEO == playData.getType()) {
                    serverRenderType = playData.getCustomData(PlayerDataConstants.CURRENT_RENDER_TYPE);
                    if (StrUtil.isEmpty(serverRenderType)) {
                        serverRenderType = RenderTypeUtil.getRenderTypeStrByRenderType(playData.getRenderType());
                    }
                }else{
                    serverRenderType = RenderTypeUtil.getRenderTypeStrByRenderType(playData.getRenderType());
                }
                String videoTag = playData.getCustomData(PlayerDataConstants.TAG);
                mediaInfo.setVideoTag(videoTag);
                mediaInfo.setVideoType(getVideoType(playData.getType()));
                switch (playData.getType()){
                    case PlayerType.TYPE_LIVE:
                        mediaInfo.setMovieMode("SPHERE");
                        mediaInfo.setMovieSource("LIVE");
                        mediaInfo.setMovieFormat("2D");
                        break;
                    case PlayerType.TYPE_PANO:
                        mediaInfo.setMovieMode("SPHERE");
                        mediaInfo.setMovieSource("ONLINE");
                        mediaInfo.setMovieFormat("2D");
                        break;
                    case PlayerType.TYPE_3D:
                        mediaInfo.setMovieMode("NORMAL");
                        mediaInfo.setMovieSource("ONLINE");
                        mediaInfo.setMovieFormat("3DLR");
                        break;
                    case PlayerType.TYPE_LOCALVIDEO:
                        mediaInfo.setMovieMode("SPHERE");
                        mediaInfo.setMovieSource("OFFLINE");
                        mediaInfo.setMovieFormat("2D");
                        break;
                    default:
                        mediaInfo.setMovieMode("SPHERE");
                        mediaInfo.setMovieSource("ONLINE");
                        mediaInfo.setMovieFormat("2D");
                        break;
                }
                initRenderType(serverRenderType, mediaInfo);
                if(playData.getType() == PlayerType.TYPE_LOCALVIDEO){
                    mediaInfo.setMovieQuality("");
                    List<PathInfo> localPath = new ArrayList<>();
                    PathInfo localPathInfo = new PathInfo(playData.getRealUrl(), "", serverRenderType);
                    localPath.add(localPathInfo);
                    mediaInfo.setPath(localPath);
                }else {
                    DefinitionModel currentDefinitionModel = playData.getCustomData(PlayerDataConstants.CURRENT_DEFINITION_MODEL);
                    mediaInfo.setMovieQuality(convertBitType(currentDefinitionModel.getDefinition()));
                    SimpleArrayMap<Integer, DefinitionModel> currentDefinitionMap = playData.getCustomData(PlayerDataConstants.RESOLVED_DEFINITION_MAP);
                    List<PathInfo> path = new ArrayList<>();
                    for (int i = 0, j = currentDefinitionMap.size() - 1; i <= j; i++) {
                        DefinitionModel definitionModel = currentDefinitionMap.valueAt(i);
                        PathInfo pathInfo = new PathInfo(definitionModel.getUrl(), convertBitType(definitionModel.getDefinition()),
                                RenderTypeUtil.getRenderTypeStrByRenderType(definitionModel.getRenderType()));
                        path.add(pathInfo);
                    }
                    mediaInfo.setPath(path);
                }
                e.onNext(mediaInfo);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }

    public static String getBIVideoFormat(int type) {
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

    public static String convertBitType(int bittype){
        switch (bittype){
            case VideoBitType.ST:
                return "ST";
            case VideoBitType.SD:
                return "SD";
            case VideoBitType.HD:
                return "HD";
            case VideoBitType.SDA:
                return "SDA";
            case VideoBitType.SDB:
                return "SDB";
            case VideoBitType.TDA:
                return "TDA";
            case VideoBitType.TDB:
                return "TDB";
            default:
                return "";
        }
    }

    public static String getVideoType(int type) {
        String videoType = "";
        switch (type) {
            case PlayerType.TYPE_LIVE:
                videoType = "live";
                break;
            case PlayerType.TYPE_PANO:
                videoType = "VR";
                break;
            case PlayerType.TYPE_3D:
                videoType = "3D";
                break;
            case PlayerType.TYPE_MORETV_2D:
                videoType = "moretv_movie";
                break;
            case PlayerType.TYPE_MORETV_TV:
                videoType = "moretv_tv";
                break;
            case PlayerType.TYPE_LOCALVIDEO:
                videoType = "local";
                break;
        }
        return videoType;
    }

    public static void initRenderType(String renderType, MediaInfo mediaInfo) {
        if (!TextUtils.isEmpty(renderType)) {
            switch (renderType) {
                case ServerRenderType.RENDER_TYPE_PLANE_2D:
                    mediaInfo.setMovieMode("NORMAL");
                    mediaInfo.setMovieFormat("2D");
                    break;
                case ServerRenderType.RENDER_TYPE_PLANE_3D_LR:
                    mediaInfo.setMovieMode("NORMAL");
                    mediaInfo.setMovieFormat("3DLR");
                    break;
                case ServerRenderType.RENDER_TYPE_PLANE_3D_UD:
                    mediaInfo.setMovieMode("NORMAL");
                    mediaInfo.setMovieFormat("3DUD");
                    break;
                case ServerRenderType.RENDER_TYPE_360_3D_LF:
                    mediaInfo.setMovieMode("SPHERE");
                    mediaInfo.setMovieFormat("3DLR");
                    break;
                case ServerRenderType.RENDER_TYPE_360_3D_UD:
                    mediaInfo.setMovieMode("SPHERE");
                    mediaInfo.setMovieFormat("3DUD");
                    break;
                case ServerRenderType.RENDER_TYPE_180_PLANE:
                    mediaInfo.setMovieMode("SEMIPHERE");
                    mediaInfo.setMovieFormat("2D");
                    break;
                case ServerRenderType.RENDER_TYPE_180_3D_UD:
                    mediaInfo.setMovieMode("SEMIPHERE");
                    mediaInfo.setMovieFormat("3DUD");
                    break;
                case ServerRenderType.RENDER_TYPE_180_3D_LF:
                    mediaInfo.setMovieMode("SEMIPHERE");
                    mediaInfo.setMovieFormat("3DLR");
                    break;
                case ServerRenderType.RENDER_TYPE_360_OCT_3D:
                    mediaInfo.setMovieMode("SPHERE");
                    mediaInfo.setMovieFormat("3DLR");
                    break;
                case ServerRenderType.RENDER_TYPE_180_OCT_3D:
                    mediaInfo.setMovieMode("SEMIPHERE");
                    mediaInfo.setMovieFormat("3DLR");
                    break;
            }
        }
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public DramaInfo getDramaInfo() {
        return dramaInfo;
    }

    public void setDramaInfo(DramaInfo dramaInfo) {
        this.dramaInfo = dramaInfo;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public PayModel getPayModel() {
        return payModel;
    }

    public void setPayModel(PayModel payModel) {
        this.payModel = payModel;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }
}
