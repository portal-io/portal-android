package com.whaley.biz.program.playersupport.component.dramaplayer.info;

import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.interactor.GetDetailInfo;
import com.whaley.biz.program.interactor.GetDramaDetailInfo;
import com.whaley.biz.program.model.MediaModel;
import com.whaley.biz.program.model.NodeModel;
import com.whaley.biz.program.model.ProgramDramaDetailModel;
import com.whaley.biz.program.model.response.ProgramDramaDetailResponse;
import com.whaley.biz.program.playersupport.exception.ProgramDataError;
import com.whaley.core.repository.RepositoryManager;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/11/13.
 */

public class DramaInfoController extends BaseController {

    private Map<String, ProgramDramaDetailResponse> responseCache = new HashMap<>();

    GetDramaDetailInfo getDramaDetailInfo;

    Disposable disposable;

    public DramaInfoController() {
        getDramaDetailInfo = new GetDramaDetailInfo();
        getDramaDetailInfo.setRepositoryManager(RepositoryManager.create(null));
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
        ProgramDramaDetailModel programDramaDetailModel = playData.getCustomData(PlayerDataConstants.DRAMA_INFO);
        if (programDramaDetailModel != null) {
            return true;
        }
        ProgramDramaDetailResponse programDramaDetailResponse = responseCache.get(playData.getId());
        if (programDramaDetailResponse == null) {
            getProgramDetailInfo(playData);
            return false;
        }
        setupPlayDataByProgramDetail(playData, programDramaDetailResponse);
        return true;
    }


    private void getProgramDetailInfo(final PlayData playData) {
        GetDetailInfo.Param param = new GetDetailInfo.Param();
        param.setCode(playData.getId());
        dispose();
        disposable = getDramaDetailInfo.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<ProgramDramaDetailResponse>() {
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
                    public void onNext(@NonNull ProgramDramaDetailResponse programDramaDetailResponse) {
                        if (programDramaDetailResponse.getData() == null) {
                            getPlayerController().setError(new ProgramDataError("获取节目详情失败"));
                            return;
                        }
                        if (setupPlayDataByProgramDetail(playData, programDramaDetailResponse)) {
                            responseCache.put(playData.getId(), programDramaDetailResponse);
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


    private boolean setupPlayDataByProgramDetail(PlayData playData, ProgramDramaDetailResponse programDramaDetailResponse) {
        ProgramDramaDetailModel programDramaDetailModel = programDramaDetailResponse.getData();
        playData.setTitle(programDramaDetailModel.getDisplayName());
        playData.setType(PlayerType.TYPE_PANO);
        playData.putCustomData(PlayerDataConstants.IS_CHARGEABLE, programDramaDetailModel.getIsChargeable());
        playData.putCustomData(PlayerDataConstants.TAG, programDramaDetailModel.getTags());
        if (programDramaDetailModel.getStartNode() == null) {
            getPlayerController().setError(new ProgramDataError("获取节目详情失败"));
            return false;
        }
        List<MediaModel> mediaDtos = programDramaDetailModel.getStartNode().getMediaDtos();
        if (mediaDtos == null || mediaDtos.size() == 0) {
            getPlayerController().setError(new ProgramDataError("获取节目详情失败"));
            return false;
        }
        Iterator<MediaModel> iterator = programDramaDetailModel.getStartNode().getMediaDtos().iterator();
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
        playData.putCustomData(PlayerDataConstants.MULTI_POSITION_TYPE, false);
        playData.putCustomData(PlayerDataConstants.SERVER_RENDER_TYPE, mediaModel.getRenderType());
        playData.setOrginUrl(mediaModel.getPlayUrl());
        playData.putCustomData(PlayerDataConstants.DRAMA_INFO, programDramaDetailModel);
        playData.putCustomData(PlayerDataConstants.DRAMA_RESPONSE, programDramaDetailResponse);
        playData.putCustomData(PlayerDataConstants.IS_CAN_DOWNLOAD, false);
        playData.putCustomData(PlayerDataConstants.CONTENT_TYPE, programDramaDetailModel.getType());
        playData.putCustomData(PlayerDataConstants.CENTER_IMAGE, programDramaDetailModel.getCenterPic());
        playData.putCustomData(PlayerDataConstants.CENTER_IMAGE_RADIUS, programDramaDetailModel.getRadius());
        playData.putCustomData(programDramaDetailModel.getStartNode().getCode(), programDramaDetailModel.getStartNode());
        List<NodeModel> nodeList = programDramaDetailModel.getNodes();
        if (nodeList != null) {
            for (NodeModel nodeModel : nodeList) {
                playData.putCustomData(nodeModel.getCode(), nodeModel);
            }
        }
        List<String> trackList = new ArrayList<>();
        trackList.add(programDramaDetailModel.getStartNode().getCode());
        playData.putCustomData(PlayerDataConstants.DRAMA_TRACK_LIST, trackList);
        playData.putCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE, programDramaDetailModel.getStartNode());
        playData.putCustomData(PlayerDataConstants.START_DRAMA_NODE, programDramaDetailModel.getStartNode());
        return true;
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
