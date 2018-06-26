package com.whaley.biz.program.ui.detail.component.drama;

import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerConstants;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.model.ProgramDramaDetailModel;
import com.whaley.biz.program.ui.detail.ProgramDetailView;
import com.whaley.biz.program.ui.detail.component.ProgramDetailController;
import com.whaley.biz.program.ui.detail.presenter.BaseProgramDetailPresenter;
import com.whaley.biz.program.ui.detail.viewholder.VRViewHolder;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaDetailController extends ProgramDetailController {

    public DramaDetailController(ProgramDetailView programDetailView) {
        super(programDetailView);
    }

    @Subscribe(priority = PlayerConstants.PREPARE_START_PLAY_PRIORITY_PROGRAM)
    public void onPrepareStartPlay(PrepareStartPlayEvent prepareStartPlayEvent) {
        if (prepareStartPlayEvent.getMaxPriority() < PlayerConstants.PREPARE_START_PLAY_PRIORITY_PROGRAM)
            return;
        PlayData playData = prepareStartPlayEvent.getPlayData();
        ProgramDramaDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.DRAMA_INFO);
        setCode(programDetailModel.getCode());
        VRViewHolder.VRProgramModel programModel = new VRViewHolder.VRProgramModel();
        programModel.setProgramName(programDetailModel.getDisplayName());
        programModel.setPlayCount(formatPlayCount(programDetailModel.getStat().getPlayCount()));
        programModel.setPosterName(programDetailModel.getContentPrivider().getName());
        programModel.setPosterFans(programDetailModel.getContentPrivider().getFansCount());
        programModel.setPosterImage(programDetailModel.getContentPrivider().getHeadPic());
        programModel.setPosterCode(programDetailModel.getContentPrivider().getCpCode());
        programModel.setPosterFollow(programDetailModel.getContentPrivider().getIsFollow() == 1);
        getUIView().updateInfo(programModel);
        ModuleEvent moduleEvent = new ModuleEvent(BaseProgramDetailPresenter.KEY_EVENT_GET_COLLECTION, programDetailModel);
        emitEvent(moduleEvent);
        getUIView().getEmptyDisplayView().showContent();
        isPrepareStartPlay = true;
        browse(true);
    }

    //==================bi埋点=====================//

    protected LogInfoParam.Builder getGeneralBuilder(String eventId) {
        if (getPlayerController() != null) {
            ProgramDramaDetailModel programDetailModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.DRAMA_INFO);
            if (programDetailModel != null) {
                LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                        .setEventId(eventId)
                        .setCurrentPageId(ROOT_DRAMA)
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
