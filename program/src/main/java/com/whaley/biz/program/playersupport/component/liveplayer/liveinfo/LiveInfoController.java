package com.whaley.biz.program.playersupport.component.liveplayer.liveinfo;

import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.LiveConstants;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.interactor.GetLiveDetail;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.model.LiveMediaModel;
import com.whaley.biz.program.playersupport.component.liveplayer.liveresolvedefinition.ResolutionUtil;
import com.whaley.biz.program.playersupport.component.normalplayer.resolvedefinition.RenderTypeUtil;
import com.whaley.biz.program.playersupport.event.LiveStateCompletedEvent;
import com.whaley.biz.program.playersupport.exception.ProgramErrorConstants;
import com.whaley.biz.program.playersupport.exception.LiveDataError;
import com.whaley.biz.program.playersupport.model.ADModel;
import com.whaley.biz.program.playersupport.model.CameraModel;
import com.whaley.biz.program.model.response.LiveDetailsResponse;
import com.whaley.biz.program.playersupport.model.DefinitionModel;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.utils.StrUtil;
import com.whaley.wvrplayer.vrplayer.external.event.VideoConstantValue;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/8/22 12:28.
 */

public class LiveInfoController extends BaseController implements BIConstants {

//    private Map<String, LiveDetailsModel> responseCache = new HashMap<>();

    private GetLiveDetail getLiveDetail;

    private Disposable disposable;


    public LiveInfoController() {
        getLiveDetail = new GetLiveDetail();
        getLiveDetail.setRepositoryManager(RepositoryManager.create(null));
    }

    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_GET_INFO)
    public void onPrepareStartPlayEvent(PrepareStartPlayEvent prepareStartPlayEvent) {
        if (prepareStartPlayEvent.getMaxPriority() < PlayerConstants.PREPARE_START_PLAY_PRIORITY_GET_INFO) {
            return;
        }
        if (!checkLiveDetail(prepareStartPlayEvent.getPlayData())) {
            getEventBus().cancelEventDelivery(prepareStartPlayEvent);
        }
    }

    private boolean checkLiveDetail(PlayData playData) {
//        LiveDetailsModel liveDetailsModel = playData.getCustomData(PlayerDataConstants.LIVE_INFO);
//        if (liveDetailsModel != null) {
//            return true;
//        }
//        liveDetailsModel = responseCache.get(playData.getId());
//        if (liveDetailsModel == null) {
        getLiveDetailInfo(playData);
        return false;
//        }
//        setupPlayDataByLiveDetail(playData, liveDetailsModel);
//        return true;
    }


    private void getLiveDetailInfo(final PlayData playData) {
        dispose();
        GetLiveDetail.Param param = new GetLiveDetail.Param();
        param.setCode(playData.getId());
        disposable = getLiveDetail.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<LiveDetailsResponse>() {
                    @Override
                    public void onFinalError(Throwable e) {
                        getPlayerController().setError(new LiveDataError("获取直播数据出错了"));
                    }

                    @Override
                    public void onStatusError(int status, String message) {
                        getPlayerController().setError(new LiveDataError("获取直播数据出错了"));
                    }

                    @Override
                    public void onNoDataError() {
                        getPlayerController().setError(new LiveDataError("获取直播数据出错了"));
                    }

                    @Override
                    public void onNext(@NonNull LiveDetailsResponse liveDetailsResponse) {
                        if (liveDetailsResponse.getData() == null) {
                            getPlayerController().setError(new LiveDataError("获取直播数据出错了"));
                            return;
                        }
                        if (!setupPlayDataByLiveDetail(playData, liveDetailsResponse.getData())) {
                            return;
                        }
                        PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
                        prepareStartPlayEvent.setPlayData(playData);
                        prepareStartPlayEvent.setMaxPriority(PlayerConstants.PREPARE_START_PLAY_PRIORITY_GET_INFO - 1);
                        emitStickyEvent(prepareStartPlayEvent);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private boolean setupPlayDataByLiveDetail(PlayData playData, LiveDetailsModel liveDetailsModel) {
        int status = liveDetailsModel.getLiveStatus();
        switch (status) {
            case LiveConstants.LIVE_STATE_BEFORE:
                getPlayerController().setError(new PlayerException(ProgramErrorConstants.ERROR_LIVE_ON_BEFORE, "当前直播未开始\n请直播开始后重试"));
                return false;
            case LiveConstants.LIVE_STATE_AFTER:
                LiveStateCompletedEvent completedEvent = new LiveStateCompletedEvent();
                emitStickyEvent(completedEvent);
                return false;
        }
        playData.setTitle(liveDetailsModel.getDisplayName());
        playData.putCustomData(PlayerDataConstants.LIVE_INFO, liveDetailsModel);
        playData.putCustomData(PlayerDataConstants.PAY_DETAIL_INFO, liveDetailsModel);
        String type = liveDetailsModel.getType();
        boolean isMultiPlaySource = false;
        boolean isFootbAll = false;
        switch (type) {
            case PlayerConstants.TYPE_LIVE_FOOTBALL:
                isFootbAll = true;
                isMultiPlaySource=true;
                playData.putCustomData(PlayerDataConstants.FOOTBALL_TYPE, true);
                playData.putCustomData(PlayerDataConstants.MULTI_POSITION_TYPE, true);
                break;
            case PlayerConstants.TYPE_LIVE_CAR:
                isMultiPlaySource = true;
                playData.putCustomData(PlayerDataConstants.MULTI_POSITION_TYPE, true);
                List<ADModel> adModels = new ArrayList<>();
                if (!StrUtil.isEmpty(liveDetailsModel.getFloatPic1()) && !StrUtil.isEmpty(liveDetailsModel.getFloatUrl1())) {
                    ADModel adModel = new ADModel();
                    adModel.setName(liveDetailsModel.getFloatName1());
                    adModel.setUrl(liveDetailsModel.getFloatUrl1());
                    adModel.setPic(liveDetailsModel.getFloatPic1());
                    adModels.add(adModel);
                }
                if (!StrUtil.isEmpty(liveDetailsModel.getFloatPic2()) && !StrUtil.isEmpty(liveDetailsModel.getFloatUrl2())) {
                    ADModel adModel = new ADModel();
                    adModel.setName(liveDetailsModel.getFloatName2());
                    adModel.setUrl(liveDetailsModel.getFloatUrl2());
                    adModel.setPic(liveDetailsModel.getFloatPic2());
                    adModels.add(adModel);
                }
                if (!StrUtil.isEmpty(liveDetailsModel.getFloatPic3()) && !StrUtil.isEmpty(liveDetailsModel.getFloatUrl3())) {
                    ADModel adModel = new ADModel();
                    adModel.setName(liveDetailsModel.getFloatName3());
                    adModel.setUrl(liveDetailsModel.getFloatUrl3());
                    adModel.setPic(liveDetailsModel.getFloatPic3());
                    adModels.add(adModel);
                }
                if (adModels.size() > 0) {
                    playData.putCustomData(PlayerDataConstants.LIVE_AD_TYPE, adModels);
                }
                break;
            default:
        }
//        boolean isGift = liveDetailsModel.getIsGift() == 1;
//        playData.putCustomData(PlayerDataConstants.GIFT_TYPE, isGift);
////        if (isGift) {
////            ModuleEvent giftEvent = new ModuleEvent("/program/event/livegift", liveDetailsModel.getGiftTemplate());
////            emitStickyEvent(giftEvent);
////        }
//        boolean isTip = liveDetailsModel.getIsTip() == 1;
//        playData.putCustomData(PlayerDataConstants.MEMBER_TIP_TYPE, isTip);
//        if (isTip) {
//            ModuleEvent memberEvent = new ModuleEvent("/program/event/memberlivegift", liveDetailsModel.getCode());
//            emitStickyEvent(memberEvent);
//        }
        playData.putCustomData(PlayerDataConstants.VIP_IMAGE, liveDetailsModel.getVipPic());
        playData.putCustomData(PlayerDataConstants.LIVE_INIT_BACKGROUND_IMAGE, liveDetailsModel.getPoster());
        playData.putCustomData(PlayerDataConstants.BACKGROUND_IMAGE, liveDetailsModel.getBgPic());
        playData.putCustomData(PlayerDataConstants.BACKGROUND_FINAL_IMAGE, liveDetailsModel.getBgPic());
        playData.putCustomData(PlayerDataConstants.ORIENTATION_IS_LANDSCAPE, liveDetailsModel.getDisplayMode() == 1);
        playData.putCustomData(PlayerDataConstants.IS_CHARGEABLE, liveDetailsModel.getIsChargeable());
        playData.putCustomData(PlayerDataConstants.CONTENT_TYPE, liveDetailsModel.getType());
        if (isMultiPlaySource) {
            List<LiveMediaModel> mediaDtos = liveDetailsModel.getLiveMediaDtos();
            if (mediaDtos == null || mediaDtos.size() == 0) {
                getPlayerController().setError(new LiveDataError("获取直播数据出错了"));
                return false;
            }
            Map<String, CameraModel> map = new HashMap<String, CameraModel>();
            boolean hasCurrentData = false;
            for (LiveMediaModel mediaModel : mediaDtos) {
                String currentCamera = (isFootbAll || StrUtil.isEmpty(mediaModel.getSrcName())) ? mediaModel.getSource() : mediaModel.getSrcName();
                CameraModel cameraModel = map.get(currentCamera);
                if (cameraModel == null) {
                    cameraModel = new CameraModel();
                    cameraModel.setUrl(mediaModel.getPlayUrl());
                    cameraModel.setCamera(currentCamera);
                    cameraModel.setServerRenderType(mediaModel.getRenderType());
                    if (isFootbAll && mediaModel.getSource().equals(PlayerConstants.CAMERA_PUBLIC)) {
                        cameraModel.setPublic(true);
                        playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA, PlayerConstants.CAMERA_PUBLIC);
                        playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA_IS_PUBLIC, true);
                        playData.putCustomData(PlayerDataConstants.SERVER_RENDER_TYPE, mediaModel.getRenderType());
                        playData.setOrginUrl(cameraModel.getUrl());
                        hasCurrentData = true;
                    } else {
                        cameraModel.setPublic(false);
                    }
                    cameraModel.getDefinitionModelList().add(createDefinitionModel(mediaModel.getPlayUrl(), mediaModel.getRenderType(), mediaModel.getResolution(), isFootbAll, cameraModel.isPublic()));
                    map.put(currentCamera, cameraModel);
                } else {
                    cameraModel.getDefinitionModelList().add(createDefinitionModel(mediaModel.getPlayUrl(), mediaModel.getRenderType(), mediaModel.getResolution(), isFootbAll, cameraModel.isPublic()));
                }
            }
            if (!hasCurrentData) {

                if (isFootbAll) {
                    String currentCamera = mediaDtos.get(0).getSource();
                    CameraModel cameraModel = map.get(currentCamera);
                    playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA, currentCamera);
                    playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA_IS_PUBLIC, false);
                    playData.setOrginUrl(cameraModel.getUrl());
                } else {
                    String currentCamera = mediaDtos.get(mediaDtos.size() - 1).getSrcName();
                    CameraModel cameraModel = map.get(currentCamera);
                    playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA, currentCamera);
                    playData.putCustomData(PlayerDataConstants.CURRENT_CAMERA_IS_PUBLIC, false);
                    if (cameraModel.getDefinitionModelList().size() > 1) {
                        for (DefinitionModel definitionModel : cameraModel.getDefinitionModelList()) {
                            if (definitionModel.getDefinition() == VideoBitType.ST) {
                                playData.setOrginUrl(cameraModel.getUrl());
                                break;
                            }
                        }
                    }
                    if (!StrUtil.isEmpty(playData.getOrginUrl())) {
                        playData.setOrginUrl(cameraModel.getUrl());
                    }
                }
            }
            playData.putCustomData(PlayerDataConstants.KEY_MULTI_POSITION_CAMERA, map);
            playData.putCustomData(PlayerDataConstants.BEHAVIOR, liveDetailsModel.getBehavior());
        }
        return true;
    }

    private DefinitionModel createDefinitionModel(String url, String serverRenderType, String resolution, boolean isFootBall, boolean isPublic) {
        DefinitionModel definitionModel = new DefinitionModel();
        definitionModel.setUrl(url);
        definitionModel.setDefinition(isFootBall ? VideoBitType.HD : ResolutionUtil.getVideoBitTypeFromResolution(resolution));
        if (isPublic || !isFootBall) {
            definitionModel.setRenderType(RenderTypeUtil.getRenderTypeByRenderTypeStr(serverRenderType));
        } else {
            definitionModel.setRenderType(VideoConstantValue.MODE_HALF_SPHERE_VIP);
        }
        return definitionModel;
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        dispose();
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        onDispose();
    }

    //==================bi埋点=====================//

//    /**
//     * 直播 浏览事件
//     */
//    private void browseView(LiveDetailsModel liveDetailsModel) {
//        if(liveDetailsModel==null)
//            return;
//        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
//                .setEventId(BROWSE_VIEW)
//                .setCurrentPageId(ROOT_LIVE_DETAILS)
//                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, liveDetailsModel.getCode())
//                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, liveDetailsModel.getDisplayName())
//                .setNextPageId(ROOT_LIVE_DETAILS);
//        if (builder != null) {
//            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
//        }
//    }

}
