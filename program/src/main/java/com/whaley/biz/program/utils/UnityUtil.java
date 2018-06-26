package com.whaley.biz.program.utils;

import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.playercontroller.IPlayerController;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.NodeModel;
import com.whaley.biz.program.model.response.ProgramDramaDetailResponse;
import com.whaley.biz.program.playersupport.model.DramaInfo;
import com.whaley.biz.program.playersupport.model.MatchInfo;
import com.whaley.biz.program.playersupport.model.MediaInfo;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by dell on 2017/12/12.
 */

public class UnityUtil {

    public static Disposable goPageDrama(IPlayerController playerController){
        return goPage(playerController, false , true, false);
    }

    public static Disposable goPageProgram(IPlayerController playerController, boolean isLive){
        return goPage(playerController, isLive , false, false);
    }

    public static Disposable goPageLocal(IPlayerController playerController){
        return goPage(playerController, false , false, true);
    }

    public static Disposable goPage(IPlayerController playerController, final boolean isLive, final boolean isDrama, final boolean isLocal){
        final PlayData playData = playerController.getRepository().getCurrentPlayData();
        final long currentProgress = playerController.getCurrentProgress();
        final long duration = playerController.getRepository().getDuration();
        boolean isFootBall = false;
        if(playData.getCustomData(PlayerDataConstants.FOOTBALL_TYPE) != null){
            isFootBall = playData.getCustomData(PlayerDataConstants.FOOTBALL_TYPE);
        }
        if (isFootBall) {
            String behavior = playData.getCustomData(PlayerDataConstants.BEHAVIOR);
            String defaultSlot = playData.getCustomData(PlayerDataConstants.CURRENT_CAMERA);
            MatchInfo matchInfo = new MatchInfo(0, behavior, defaultSlot, currentProgress);
            Router.getInstance().buildExecutor(ProgramRouterPath.UNITY_SOCCER).putObjParam(matchInfo).excute();
            return null;
        } else {
            boolean isFree = isDrama || isLocal;
            return MediaInfo.convertByPlayData(playData, isLive, isFree)
                    .subscribeWith(new DisposableObserver<MediaInfo>() {
                        @Override
                        public void onNext(@NonNull MediaInfo mediaInfo) {
                            if(isLive){
                                mediaInfo.setDuration(0);
                                mediaInfo.setProgress(0f);
                                int playCount = 0;
                                if(playData.getCustomData(PlayerDataConstants.LIVE_PLAY_COUNT)!=null){
                                    playCount = playData.getCustomData(PlayerDataConstants.LIVE_PLAY_COUNT);
                                }
                                mediaInfo.setPlayCount(playCount);
                            }else {
                                mediaInfo.setDuration(duration);
                                if (duration > 0) {
                                    mediaInfo.setProgress(1f * currentProgress / duration);
                                } else {
                                    mediaInfo.setProgress(0f);
                                }
                            }
                            if(isDrama){
                                mediaInfo.setMovieSource("DRAMA");
                                ProgramDramaDetailResponse programDramaDetailResponse = playData.getCustomData(PlayerDataConstants.DRAMA_RESPONSE);
                                DramaInfo dramaInfo = new DramaInfo();
                                dramaInfo.setInfo(GsonUtil.getGson().toJson(programDramaDetailResponse));
                                NodeModel nodeModel = playData.getCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE);
                                dramaInfo.setCurrentNode(nodeModel.getCode());
                                List<String> trackList = playData.getCustomData(PlayerDataConstants.DRAMA_TRACK_LIST);
                                dramaInfo.setNodeTrack(trackList);
                                mediaInfo.setDramaInfo(dramaInfo);
                            }
                            Router.getInstance().buildExecutor(ProgramRouterPath.UNITY_PLAYER).putObjParam(mediaInfo).excute();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

}
