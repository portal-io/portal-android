package com.whaley.biz.program.playersupport.component.normalplayer.info;

import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.constants.ServerRenderType;
import com.whaley.biz.program.constants.VideoType;
import com.whaley.biz.program.interactor.GetDetailInfo;
import com.whaley.biz.program.model.MediaModel;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.playersupport.exception.ProgramDataError;
import com.whaley.biz.program.playersupport.model.CameraModel;
import com.whaley.biz.program.model.response.ProgramDetailResponse;
import com.whaley.core.repository.RepositoryManager;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yangzhi on 2017/8/15.
 */

public class InfoController extends BaseController {

    private Map<String, ProgramDetailModel> responseCache = new HashMap<>();

    GetDetailInfo getDetailInfo;

    Disposable disposable;

    public InfoController() {
        getDetailInfo = new GetDetailInfo();
        getDetailInfo.setRepositoryManager(RepositoryManager.create(null));
    }


    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_GET_INFO)
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent) {
        if (prepareStartPlayEvent.getMaxPriority() < PlayerConstants.PREPARE_START_PLAY_PRIORITY_GET_INFO) {
            return;
        }
        if (!checkProgramDetail(prepareStartPlayEvent.getPlayData())) {
            getEventBus().cancelEventDelivery(prepareStartPlayEvent);
        }
    }

    private boolean checkProgramDetail(PlayData playData) {
        ProgramDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.PROGRAM_INFO);
        if (programDetailModel != null) {
            return true;
        }
        programDetailModel = responseCache.get(playData.getId());
        if (programDetailModel == null) {
            getProgramDetailInfo(playData);
            return false;
        }
        setupPlayDataByProgramDetail(playData, programDetailModel);
        return true;
    }


    private void getProgramDetailInfo(final PlayData playData) {
        GetDetailInfo.Param param = new GetDetailInfo.Param();
        param.setCode(playData.getId());
        dispose();
        disposable = getDetailInfo.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<ProgramDetailResponse>() {
                    @Override
                    public void onFinalError(Throwable e) {
                        getPlayerController().setError(new ProgramDataError("获取节目详情失败"));
                    }

                    @Override
                    public void onStatusError(int status, String message) {
                        getPlayerController().setError(new ProgramDataError("获取节目详情失败"));
                    }

                    @Override
                    public void onNoDataError() {
                        getPlayerController().setError(new ProgramDataError("获取节目详情失败"));
                    }

                    @Override
                    public void onNext(@NonNull ProgramDetailResponse programDetailResponse) {
                        if (programDetailResponse.getData() == null) {
                            getPlayerController().setError(new ProgramDataError("获取节目详情失败"));
                            return;
                        }
                        if (setupPlayDataByProgramDetail(playData, programDetailResponse.getData())) {
                            responseCache.put(playData.getId(), programDetailResponse.getData());
                            PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
                            prepareStartPlayEvent.setPlayData(playData);
                            prepareStartPlayEvent.setMaxPriority(PlayerConstants.PREPARE_START_PLAY_PRIORITY_GET_INFO - 1);
                            getEventBus().postSticky(prepareStartPlayEvent);
                        } else {
                            getPlayerController().setError(new ProgramDataError("获取节目详情失败"));
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private boolean setupPlayDataByProgramDetail(PlayData playData, ProgramDetailModel programDetailModel) {
        playData.setTitle(programDetailModel.getDisplayName());
        String type = programDetailModel.getType();
        String videoType = programDetailModel.getVideoType();
        switch (videoType) {
            case VideoType.VIDEO_TYPE_VR:
                playData.setType(PlayerType.TYPE_PANO);
                break;
            case VideoType.VIDEO_TYPE_MORETV_2D:
                playData.setType(PlayerType.TYPE_MORETV_2D);
                break;
            case VideoType.VIDEO_TYPE_MORETV_TV:
                playData.setType(PlayerType.TYPE_MORETV_TV);
                break;
            case VideoType.VIDEO_TYPE_3D:
                playData.setType(PlayerType.TYPE_3D);
                break;
        }
        playData.putCustomData(PlayerDataConstants.IS_CHARGEABLE, programDetailModel.getIsChargeable());
        playData.putCustomData(PlayerDataConstants.CONTENT_TYPE, programDetailModel.getType());
        playData.putCustomData(PlayerDataConstants.TAG, programDetailModel.getTags());
        boolean isFootBall = PlayerConstants.TYPE_FOOTBALL.equals(type);
        playData.putCustomData(PlayerDataConstants.MULTI_POSITION_TYPE, isFootBall);
        playData.putCustomData(PlayerDataConstants.FOOTBALL_TYPE, isFootBall);
        playData.putCustomData(PlayerDataConstants.VIP_IMAGE, programDetailModel.getVipPic());
        playData.putCustomData(PlayerDataConstants.BACKGROUND_IMAGE, programDetailModel.getBgPic());
        playData.putCustomData(PlayerDataConstants.BACKGROUND_FINAL_IMAGE, programDetailModel.getBgPic());
        if (!isFootBall) {
            if (programDetailModel.getMediaDtos() != null && programDetailModel.getMediaDtos().size() > 0) {
                Iterator<MediaModel> iterator = programDetailModel.getMediaDtos().iterator();
                MediaModel mediaModel = null;
                while (iterator.hasNext()) {
                    MediaModel nextMediaModel = iterator.next();
                    if (nextMediaModel.getSource() != null && (nextMediaModel.getSource().equals("vr_share") || nextMediaModel.getSource().equals("vr_share_4k"))) {
                        continue;
                    }
                    mediaModel = nextMediaModel;
                    break;
                }
                if (mediaModel == null)
                    return false;
                playData.putCustomData(PlayerDataConstants.SERVER_RENDER_TYPE, mediaModel.getRenderType());
                playData.setOrginUrl(mediaModel.getPlayUrl());
            }
        } else {
            List<MediaModel> mediaDtos = programDetailModel.getMediaDtos();
            if (mediaDtos == null || mediaDtos.size() == 0) {
                getPlayerController().setError(new ProgramDataError("获取节目详情失败"));
                return false;
            }
            playData.putCustomData(PlayerDataConstants.BEHAVIOR, programDetailModel.getBehavior());
            Map<String, CameraModel> map = new HashMap<String, CameraModel>();
            boolean hasCurrentData = false;
            for (MediaModel mediaModel : mediaDtos) {
                String source = mediaModel.getSource();
                if (source != null && (source.equals("vr_share") || source.equals("vr_share_4k"))) {
                    continue;
                }
                CameraModel cameraModel = new CameraModel();
                cameraModel.setCamera(mediaModel.getSource());
                cameraModel.setServerRenderType(mediaModel.getRenderType());
                cameraModel.setUrl(mediaModel.getPlayUrl());
                if (mediaModel.getSource().equals(PlayerConstants.CAMERA_PUBLIC)) {
                    cameraModel.setPublic(true);
                    playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA, PlayerConstants.CAMERA_PUBLIC);
                    playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA_IS_PUBLIC, true);
                    playData.putCustomData(PlayerDataConstants.SERVER_RENDER_TYPE, mediaModel.getRenderType());
                    playData.setOrginUrl(cameraModel.getUrl());
                    hasCurrentData = true;
                } else {
                    cameraModel.setPublic(false);
                }
                map.put(mediaModel.getSource(), cameraModel);
            }
            if (!hasCurrentData) {
                CameraModel cameraModel = map.get(mediaDtos.get(0).getSource());
                playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA, mediaDtos.get(0).getSource());
                playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA_IS_PUBLIC, false);
                playData.setOrginUrl(cameraModel.getUrl());
            }
            playData.putCustomData(PlayerDataConstants.KEY_MULTI_POSITION_CAMERA, map);
        }
        playData.putCustomData(PlayerDataConstants.PROGRAM_INFO, programDetailModel);
        playData.putCustomData(PlayerDataConstants.PAY_DETAIL_INFO, programDetailModel);
        boolean isCanDownload = playData.getType() == PlayerType.TYPE_PANO
                && programDetailModel.getIsChargeable() == 0
                && programDetailModel.getDownloadDtos() != null
                && programDetailModel.getDownloadDtos().size() > 0
                && programDetailModel.getDownloadDtos().get(0) != null
                && !filterRenderType((String) playData.getCustomData(PlayerDataConstants.SERVER_RENDER_TYPE));
        playData.putCustomData(PlayerDataConstants.IS_CAN_DOWNLOAD, isCanDownload);
        return true;
    }


    /**
     * 过滤渲染类型
     *
     * @param renderType
     * @return
     */
    public boolean filterRenderType(String renderType) {
        boolean isFilter = false;
        if (renderType != null)
            switch (renderType) {
                case ServerRenderType.RENDER_TYPE_360_3D_LF:
                case ServerRenderType.RENDER_TYPE_360_3D_UD:
                case ServerRenderType.RENDER_TYPE_180_PLANE:
                case ServerRenderType.RENDER_TYPE_180_3D_UD:
                case ServerRenderType.RENDER_TYPE_180_3D_LF:
                    isFilter = true;
                    break;
            }
        return isFilter;
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        dispose();
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        onDispose();
    }
}
