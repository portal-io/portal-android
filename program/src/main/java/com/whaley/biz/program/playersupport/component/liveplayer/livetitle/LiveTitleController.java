package com.whaley.biz.program.playersupport.component.liveplayer.livetitle;

import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.HideEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.playersupport.event.PlayCountGetEvent;
import com.whaley.biz.program.playersupport.event.ShowLiveInfoEvent;

import org.greenrobot.eventbus.Subscribe;

import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.StrUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YangZhi on 2017/8/8 20:43.
 */

public class LiveTitleController extends ControlController<LiveTitleUIAdapter> {

    static final String SHOW_DANMU_EDIT_EVENT = "/program/event/showdanmuedit";

    static final String HIDE_DANMU_EDIT_EVENT = "/program/event/hidedanmuedit";

    static final String SHOW_GIFT_EDIT_EVENT = "/program/event/showlivegift";

    static final String HIDE_GIFT_EVENT = "/livegift/event/gifthide";


    static final String LIVE_GIFT_EVENT = "/program/event/livegift";

    ShareParam.Builder shareParamBuilder;

    String formatBeginTimeStr;

    boolean isDanmuEditOnShow;
    boolean isisContributionRankShow;

    boolean isContributionRank;
    boolean isLandScape;

    @Subscribe
    public void onDanmuEditVisibleChangeEvent(ModuleEvent event) {
        String eventName = event.getEventName();
        switch (eventName) {
            case SHOW_DANMU_EDIT_EVENT:
                isDanmuEditOnShow = true;
                getUIAdapter().hide(true);
                break;
            case HIDE_DANMU_EDIT_EVENT:
                isDanmuEditOnShow = false;
                if (!isHide()) {
                    getUIAdapter().show(true);
                }
                break;
            case SHOW_GIFT_EDIT_EVENT:
                isisContributionRankShow = true;
                if (isContributionRank && !isLandScape) {
                    getUIAdapter().hideContribution();
                }
                break;
            case HIDE_GIFT_EVENT:
                isisContributionRankShow = false;
                if (isContributionRank && !isLandScape) {
                    if (!isHide()) {
                        getUIAdapter().showContribution();
                    }
                }
                break;
            case LIVE_GIFT_EVENT:
                isContributionRank = true;
                PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                isLandScape = playData.getBooleanCustomData(PlayerDataConstants.ORIENTATION_IS_LANDSCAPE);
                if (isLandScape) {
                    getUIAdapter().showOnLandScape();
                } else {
                    getUIAdapter().showOnPortrait();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void showUI(boolean anim) {
        if (isDanmuEditOnShow) {
            return;
        }
        super.showUI(anim);
    }

    @Override
    public void onHideEvent(HideEvent hideEvent) {
        super.onHideEvent(hideEvent);
    }

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        getUIAdapter().updateTitle(preparingEvent.getPlayData().getTitle());
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        getUIAdapter().updateTitle(videoPreparedEvent.getPlayData().getTitle());
        PlayData playData = videoPreparedEvent.getPlayData();
        LiveDetailsModel liveDetail = playData.getCustomData(PlayerDataConstants.LIVE_INFO);
        ShareModel shareModel = ShareModel.createBuilder()
                .setTitle(liveDetail.getDisplayName())
                .setDes(liveDetail.getDescription())
                .setExtra(getFormatBeginTime(liveDetail))
                .setCode(liveDetail.getCode())
                .setShareType(ShareTypeConstants.TYPE_SHARE_LIVE_PLAYER)
                .setType(ShareConstants.TYPE_ALL)
                .setHorizontal(liveDetail.getDisplayMode() == 1)
                .setImgUrl(liveDetail.getPoster()).build();
        Router.getInstance().buildExecutor("/share/service/sharemodel").putObjParam(shareModel).notTransParam()
                .callback(new Executor.Callback<ShareParam.Builder>() {
                    @Override
                    public void onCall(ShareParam.Builder builder) {
                        shareParamBuilder = builder;
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                }).notTransCallbackData().excute();
    }

    @Subscribe
    public void onPlayCountGetEvent(PlayCountGetEvent event) {
        getUIAdapter().updatePlayCount(getCuttingCount(event.getPlayCount()));
    }

    private String getCuttingCount(int playCount) {
        if (playCount >= 10000) {
            int million = playCount / 10000;
            int thousand = (playCount % 10000) / 1000;
            if (thousand > 0) {
                return million + "." + thousand + "万人";
            } else {
                return million + "万人";
            }
        } else {
            return playCount + "";
        }
    }

    public void onTitleClick() {
        emitEvent(new ShowLiveInfoEvent());
    }


    public String getFormatBeginTime(LiveDetailsModel liveDetail) {
        if (StrUtil.isEmpty(formatBeginTimeStr)) {
            long time = 0;
            if (liveDetail != null)
                time = liveDetail.getBeginTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
            formatBeginTimeStr = simpleDateFormat.format(new Date(time));
        }
        return formatBeginTimeStr;
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        shareParamBuilder = null;
    }

    public void onShareClick() {
        if(shareParamBuilder!=null) {
            shareParamBuilder.setContext(getActivity());
            ShareUtil.share(shareParamBuilder.build());
        }
    }

    public void onContributionRankClick() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        LiveDetailsModel liveDetail = playData.getCustomData(PlayerDataConstants.LIVE_INFO);
        Router.getInstance()
                .buildNavigation(ProgramRouterPath.CONTRIBUTION_TAB)
                .withString(ProgramConstants.KEY_PARAM_LIVE_CODE, liveDetail.getCode())
                .withBoolean(ProgramConstants.KEY_PARAM_HAS_MEMBER_RANK, liveDetail.isTip())
                .setStarter((Starter) getActivity())
                .navigation();
    }
}
