package com.whaley.biz.program.ui.detail.component.drama;

import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.model.ProgramDramaDetailModel;
import com.whaley.biz.program.ui.detail.ProgramDetailView;
import com.whaley.biz.program.ui.detail.component.ProgramFollowController;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaFollowController extends ProgramFollowController{

    public DramaFollowController(ProgramDetailView programDetailView) {
        super(programDetailView);
    }

    protected void initFollow( PlayData playData){
        ProgramDramaDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.DRAMA_INFO);
        code = programDetailModel.getContentPrivider().getCpCode();
        isFollow = (programDetailModel.getContentPrivider().getIsFollow() == 1);
    }

}
