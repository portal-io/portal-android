package com.whaley.biz.program.model;

import com.whaley.biz.playerui.model.DataBuilder;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.ProgramConstants;

/**
 * Author: qxw
 * Date: 2017/3/23
 */

public class CpFollowInfoModel extends CpProgramModel {
    private String name;
    private String headPic;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }


    @Override
    public PlayData getPlayData() {
        int type = ProgramConstants.TYPE_PLAY_PANO;
        if (VIDEO_TYPE_3D.equals(getVideoType())) {
            type = TYPE_PLAY_3D;
        }
        return DataBuilder.createBuilder("", type)
                .setId(getCode())
                .setTitle(getDisplayName())
                .setMonocular(true)
                .build();
    }
}
