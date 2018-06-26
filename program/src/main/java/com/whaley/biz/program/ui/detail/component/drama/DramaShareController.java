package com.whaley.biz.program.ui.detail.component.drama;

import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.model.ProgramDramaDetailModel;
import com.whaley.biz.program.ui.detail.ProgramDetailView;
import com.whaley.biz.program.ui.detail.component.ProgramShareController;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.utils.StrUtil;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaShareController extends ProgramShareController {

    public DramaShareController(ProgramDetailView programDetailView) {
        super(programDetailView);
    }

    @Override
    protected ShareModel getShareModel(PlayData playData) {
        ProgramDramaDetailModel programDetailModel = playData.getCustomData(PlayerDataConstants.DRAMA_INFO);
        ShareModel shareModel = ShareModel.createBuilder()
                .setImgUrl(getImg(programDetailModel))
                .setTitle(programDetailModel.getDisplayName())
                .setType(ShareConstants.TYPE_ALL)
                .setCode(programDetailModel.getCode())
                .setDes(programDetailModel.getDescription())
                .setShareType(ShareTypeConstants.TYPE_SHARE_DRAMA)
                .build();
        return shareModel;
    }

    private String getImg(ProgramDramaDetailModel bean) {
        if (!StrUtil.isEmpty(bean.getSmallPic()))
            return bean.getSmallPic();
        return bean.getBigPic();
    }

}
