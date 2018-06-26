package com.whaley.biz.program.ui.detail.component;

import android.view.ViewGroup;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.ActivityPauseEvent;
import com.whaley.biz.program.playersupport.event.ActivityResultEvent;
import com.whaley.biz.playerui.event.ActivityResumeEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.model.CouponPackModel;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.biz.program.model.pay.PayModel;
import com.whaley.biz.program.ui.detail.ProgramDetailView;
import com.whaley.biz.program.ui.detail.presenter.BaseProgramDetailPresenter;
import com.whaley.biz.program.ui.detail.viewholder.MovieViewHolder;
import com.whaley.biz.program.ui.detail.viewholder.TVViewHolder;
import com.whaley.biz.program.ui.detail.viewholder.VRViewHolder;
import com.whaley.biz.program.utils.CouponPackUtil;
import com.whaley.biz.program.utils.StringUtil;
import com.whaley.core.router.Router;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ProgramDetailController extends BaseController implements BIConstants {

    ProgramDetailView programDetailView;

    boolean isChargeable;

    String price;

    boolean isSet;

    String code;

    protected boolean isPrepareStartPlay = false;

    boolean isBrowsed = false;

    long startDurationTime;

    boolean isUnity = false;

    public ProgramDetailController(ProgramDetailView programDetailView) {
        this.programDetailView = programDetailView;
        this.programDetailView.getEmptyDisplayView().setOnRetryListener(new EmptyDisplayView.OnRetryListener() {
            @Override
            public void onRetry() {
                getPlayerController().reStartPlay(true);
            }
        });
    }

    @Override
    protected void onDestory() {
        browseDuration();
        super.onDestory();
        this.programDetailView.getEmptyDisplayView().setOnRetryListener(null);
    }

    public ProgramDetailView getUIView() {
        return programDetailView;
    }

    @Override
    public void registEvents() {
        super.registEvents();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void UnityEvent(BaseEvent event) {
        if ("/unity/service/player".equals(event.getEventType())) {
            isUnity = true;
        }
    }

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        checkInitOrientation();
        updatePlayerHeight();

        PlayData playData = preparingEvent.getPlayData();
        if (getCode() == null || playData == null || !playData.getId().equals(getCode())) {
            getUIView().getEmptyDisplayView().showLoading(null);
        }
    }

    public boolean isChargeable() {
        return isChargeable;
    }

    public void setChargeable(boolean chargeable) {
        isChargeable = chargeable;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Subscribe
    public void moduleEvent(ModuleEvent event) {
        if (!isChargeable()) {
            return;
        }
        if ("key_pay".equals(event.getEventName())) {
            PayModel payModel = GsonUtil.getGson().fromJson((String) event.getParam(), PayModel.class);
            if (payModel.getCode().equals(getCode())) {
                if (!payModel.isPay()) {
                    getUIView().updatePayBtn(isChargeable, true, getPrice());
                } else {
                    getUIView().updatePayBtn(isChargeable, false, null);
                }
            }
        }

    }

    @Subscribe(sticky = true, priority = 20)
    public void onErrorEvent(ErrorEvent errorEvent) {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (getCode() == null || playData == null || !playData.getId().equals(getCode())) {
            getUIView().getEmptyDisplayView().showError(errorEvent.getPlayerException());
        }
    }

    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_PROGRAM)
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent) {
        if (prepareStartPlayEvent.getMaxPriority() < PlayerConstants.PREPARE_START_PLAY_PRIORITY_PROGRAM)
            return;
        PlayData playData = prepareStartPlayEvent.getPlayData();
        ProgramDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.PROGRAM_INFO);
        setCode(programDetailModel.getCode());
        switch (playData.getType()) {
            case PlayerType.TYPE_3D:
            case PlayerType.TYPE_MORETV_2D: {
                getUIView().changeToMoive();
                MovieViewHolder.MovieProgramModel programModel = new MovieViewHolder.MovieProgramModel();
                programModel.setProgramName(programDetailModel.getDisplayName());
                programModel.setDescription(programDetailModel.getDescription());
                programModel.setPlayCount(formatPlayCount(programDetailModel.getStat().getPlayCount()));
                programModel.setTags(getTags(programDetailModel));
                String score = programDetailModel.getScore();
                boolean isScoreNotEmpty = !StrUtil.isEmpty(score);
                float scoreValue = Float.parseFloat(score);
                programModel.setRateStr(isScoreNotEmpty ? scoreValue + "分" : "0分");
                programModel.setRate(isScoreNotEmpty ? scoreValue / 2 : 0f);
                programModel.setYear(programDetailModel.getAge());
                programModel.setDistrict(programDetailModel.getArea());
                programModel.setActor(programDetailModel.getActors());
                programModel.setDirector(programDetailModel.getDirector());
                getUIView().updateInfo(programModel);
                break;
            }
            case PlayerType.TYPE_PANO: {
                getUIView().changeToVR();
                VRViewHolder.VRProgramModel programModel = new VRViewHolder.VRProgramModel();
                programModel.setProgramName(programDetailModel.getDisplayName());
                programModel.setPlayCount(formatPlayCount(programDetailModel.getStat().getPlayCount()));
                boolean isCanDownload = playData.getBooleanCustomData(PlayerDataConstants.IS_CAN_DOWNLOAD);
                programModel.setDownloadEnable(isCanDownload);
                programModel.setPosterName(programDetailModel.getName());
                programModel.setPosterFans(programDetailModel.getFansCount());
                programModel.setPosterImage(programDetailModel.getHeadPic());
                programModel.setPosterCode(programDetailModel.getCpCode());
                programModel.setPosterFollow(programDetailModel.getIsFollow() == 1);
                getUIView().updateInfo(programModel);
                break;
            }
            case PlayerType.TYPE_MORETV_TV:
                getUIView().changeToTV();
                TVViewHolder.TVProgramModel programModel = new TVViewHolder.TVProgramModel();
                programModel.setProgramName(programDetailModel.getDisplayName());
                programModel.setDescription(programDetailModel.getDescription());
                programModel.setPlayCount(formatPlayCount(programDetailModel.getStat().getPlayCount()));
                programModel.setTags(getTags(programDetailModel));
                programModel.setSeriesModels(programDetailModel.getSeries());
                programModel.setSelectedPosition(programDetailModel.getCurEpisode() - 1);
                getUIView().updateInfo(programModel);
                break;
        }
        setChargeable(programDetailModel.getIsChargeable() == 1);
        if (isChargeable()) {
            CouponPackModel couponPackModel = CouponPackUtil.getCouponPackModel(programDetailModel.getCouponDto(), programDetailModel.getContentPackageQueryDtos(), false);
            setPrice(couponPackModel.price);
            setSet(couponPackModel.isSet);
        }
        ModuleEvent moduleEvent = new ModuleEvent(BaseProgramDetailPresenter.KEY_EVENT_GET_COLLECTION, programDetailModel);
        emitEvent(moduleEvent);
        getUIView().getEmptyDisplayView().showContent();
        isPrepareStartPlay = true;
        browse(true);
    }

    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    @Override
    protected void onSwitchToLandscape() {
        super.onSwitchToLandscape();
        updatePlayerHeight();
    }

    @Override
    protected void onSwitchToProtrait() {
        super.onSwitchToProtrait();
        updatePlayerHeight();
    }

    private void updatePlayerHeight() {
        int height;
        int width;
        if (isLandScape()) {
            height = ViewGroup.LayoutParams.MATCH_PARENT;
            width = DisplayUtil.screenH;
        } else {
            width = ViewGroup.LayoutParams.MATCH_PARENT;
            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
            switch (playData.getType()) {
                case PlayerType.TYPE_3D:
                case PlayerType.TYPE_MORETV_2D:
                case PlayerType.TYPE_MORETV_TV:
                    height = (int) (1f * DisplayUtil.screenW * 9 / 16);
                    break;
                default:
                    height = DisplayUtil.screenW;
                    break;
            }
        }
        getUIView().updateRealPlayerWidth(width);
        getUIView().updatePlayerHeight(height);
    }

    private List<String> getTags(ProgramDetailModel programDetailModel) {
        List<String> tagList = new ArrayList<>();
        if (programDetailModel.getTags() != null && !programDetailModel.getTags().isEmpty()) {
            String[] tags = programDetailModel.getTags().split("-");
            for (int i = 0; i < tags.length; i++) {
                tagList.add("#" + tags[i]);
                if (i >= 2) {
                    break;
                }
            }
        }
        return tagList;
    }


    protected String formatPlayCount(int playCount) {
        return StringUtil.getCuttingCount(playCount) + "次播放";
    }

    @Subscribe
    public void onActivityResumeEvent(ActivityResumeEvent activityResumeEvent) {
        if (isUnity) {
            browse(false);
        }
        isUnity = false;
        startDurationTime = System.currentTimeMillis();
    }

    @Subscribe
    public void onActivityPauseEvent(ActivityPauseEvent activityPauseEvent) {
        browseDuration();
    }

    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent activityResultEvent) {
        browse(false);
    }

    //==================bi埋点=====================//

    /**
     * 浏览
     */
    public void browse(boolean isFromFetch) {
        if (isFromFetch) {
            if (isBrowsed) {
                return;
            } else {
                isBrowsed = true;
            }
        }
        LogInfoParam.Builder builder = getGeneralBuilder(BROWSE_VIEW);
        if (builder != null) {
            builder.setNextPageId(builder.getCurrentPageId());
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    /**
     * 浏览时长
     */
    private void browseDuration() {
        LogInfoParam.Builder builder = getGeneralBuilder(BROWSE_DURATION);
        if (builder != null) {
            builder.putEventPropKeyValue(EVENT_PROP_KEY_ACTION_TYPE, ACTION_TYPE_END_BROWSE);
            builder.putEventPropKeyValue(EVENT_PROP_KEY_DURATION, String.valueOf(System.currentTimeMillis() - startDurationTime));
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    protected LogInfoParam.Builder getGeneralBuilder(String eventId) {
        if (getPlayerController() != null) {
            ProgramDetailModel programDetailModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.PROGRAM_INFO);
            if (programDetailModel != null) {
                LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                        .setEventId(eventId)
                        .setCurrentPageId(ROOT_VIDEO_DETAILS)
                        .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, programDetailModel.getCode())
                        .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, programDetailModel.getDisplayName())
                        .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_FORMAT, programDetailModel.getVideoFormat())
                        .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_CHARGEABLE, String.valueOf(programDetailModel.getIsChargeable()))
                        .putCurrentPagePropKeyValue(CURRENT_PROP_CONTENT_TYPE, programDetailModel.getType());
                return builder;
            }
        }
        return null;
    }

}