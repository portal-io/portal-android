package com.whaleyvr.biz.hybrid.router;

import android.os.Bundle;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.core.utils.GsonUtil;
import com.whaleyvr.biz.hybrid.model.player.PlayData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/25.
 */

public class FormatPageModel {

    public static PageModel getPlayerPageModel(PlayData playData, boolean isLive) {
        PageModel pageModel = new PageModel();
        if (playData != null) {
            Bundle bundle = new Bundle();
            List<PlayData> list = new ArrayList<>();
            list.add(playData);
            bundle.putString("key_param_datas", GsonUtil.getGson().toJson(list));
            pageModel.setBundle(bundle);
            if (isLive) {
                pageModel.setRouterPath(RouterPath.PLAYER_LIVE);
            } else {
                pageModel.setRouterPath(RouterPath.PROGRAM);
            }
            pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        } else {
            pageModel.setCanJump(false);
            pageModel.setMsg("播放地址出错");
        }
        return pageModel;
    }

}
