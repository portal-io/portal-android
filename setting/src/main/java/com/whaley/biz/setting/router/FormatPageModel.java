package com.whaley.biz.setting.router;

import android.os.Bundle;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.setting.constant.ProgramConstants;
import com.whaley.biz.setting.model.player.PlayData;
import com.whaley.core.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/25.
 */

public class FormatPageModel {


    public static PageModel getPlayerPageModel(PlayData playData, boolean isLive) {
        return getPlayerPageModel(playData, isLive, false);
    }

    public static PageModel getPlayerPageModel(PlayData playData, boolean isLive, boolean isDrama) {
        PageModel pageModel = new PageModel();
        if (playData != null) {
            Bundle bundle = new Bundle();
            List<PlayData> list = new ArrayList<>();
            list.add(playData);
            bundle.putString("key_param_datas", GsonUtil.getGson().toJson(list));
            pageModel.setBundle(bundle);
            if (isLive) {
                pageModel.setRouterPath(ProgramRouterPath.PLAYER_LIVE);
            } else if (isDrama) {
                pageModel.setRouterPath(ProgramRouterPath.PROGRAM_DRAMA);
            } else {
                pageModel.setRouterPath(ProgramRouterPath.PROGRAM);
            }
            pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        } else {
            pageModel.setCanJump(false);
            pageModel.setMsg("播放地址出错");
        }
        return pageModel;
    }

    public static PageModel getLocalPlayerPageModel(PlayData playData) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        List<PlayData> list = new ArrayList<>();
        list.add(playData);
        bundle.putString("key_param_datas", GsonUtil.getGson().toJson(list));
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        pageModel.setBundle(bundle);
        pageModel.setRouterPath(ProgramRouterPath.PLAYER_LOCAL);
        return pageModel;
    }

    public static PageModel getPackagePageModel(String code) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        String routerPath = ProgramRouterPath.PACKAGE;
        pageModel.setRouterPath(routerPath);
        bundle.putString(ProgramConstants.KEY_PARAM_ID, code);
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        pageModel.setBundle(bundle);
        return pageModel;
    }

    public static PageModel getLiveBeforePageModel(String code) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        pageModel.setRouterPath(ProgramRouterPath.LIVE_STATE_BEFORE);
        bundle.putString(ProgramConstants.KEY_PARAM_ID, code);
        pageModel.setBundle(bundle);
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        return pageModel;
    }

    public static PageModel getLiveAfterPageModel(String code) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        pageModel.setRouterPath(ProgramRouterPath.LIVE_COMPLETED);
        bundle.putString(ProgramConstants.KEY_PARAM_LIVE_CODE, code);
        pageModel.setBundle(bundle);
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        return pageModel;
    }

}
