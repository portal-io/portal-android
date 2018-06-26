package com.whaley.biz.program.ui.detail.component.drama;

import android.text.TextUtils;

import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.SaveCollection;
import com.whaley.biz.program.model.ProgramDramaDetailModel;
import com.whaley.biz.program.ui.detail.ProgramDetailView;
import com.whaley.biz.program.ui.detail.component.ProgramCollectionController;
import com.whaley.core.router.Router;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaCollectionController extends ProgramCollectionController {

    public DramaCollectionController(ProgramDetailView programDetailView) {
        super(programDetailView);
    }

    @Override
    protected void getCollectionEvent(ModuleEvent event) {
        ProgramDramaDetailModel programDetailModel = (ProgramDramaDetailModel) event.getParam();
        param = new SaveCollection.Param(
                programDetailModel.getCode(),
                programDetailModel.getDisplayName(),
                programDetailModel.getVideoType(),
                programDetailModel.getDuration(),
                getPic(programDetailModel));
        param.setProgramType(ProgramConstants.TYPE_DYNAMIC);
        getCollection(param.getCode());
    }

    private String getPic(ProgramDramaDetailModel programDetailModel){
        if(!TextUtils.isEmpty(programDetailModel.getSmallPic())){
            return programDetailModel.getSmallPic();
        }
        return programDetailModel.getBigPic();
    }

    //==================bi埋点=====================//

    protected void prevueClickCollection() {
        ProgramDramaDetailModel programDetailModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.DRAMA_INFO);
        if (programDetailModel == null)
            return;
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(PREVUE_CLICK_COLLECTION)
                .setCurrentPageId(ROOT_DRAMA)
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, programDetailModel.getCode())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, programDetailModel.getDisplayName())
                .setNextPageId(ROOT_DRAMA);
        if (builder != null) {
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

}
