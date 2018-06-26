package com.whaley.biz.program.utils;

import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.core.appcontext.Starter;

/**
 * Created by dell on 2017/10/27.
 */

public class BIUtil implements BIConstants {

    public static String getNextPageId(ClickableUIViewModel clickableUIViewModel) {
        String nextPageId = null;
        if (clickableUIViewModel != null) {
            PageModel pageModel = clickableUIViewModel.getPageModel();
            if (pageModel != null) {
                switch (pageModel.getRouterPath()) {
                    case ProgramRouterPath.LIVE_COMPLETED:
                        nextPageId = ROOT_LIVE_END;
                        break;
                    case ProgramRouterPath.LIVE_STATE_BEFORE:
                        nextPageId = ROOT_LIVE_PREVUE;
                        break;
                    case ProgramRouterPath.PLAYER_LIVE:
                        nextPageId = ROOT_LIVE_DETAILS;
                        break;
                    case ProgramRouterPath.PACKAGE:
                        nextPageId = ROOT_CONTENT_PACKAGE;
                        break;
                    case ProgramRouterPath.TOPIC:
                        nextPageId = ROOT_TOPIC;
                        break;
                    case ProgramRouterPath.PROGRAM:
                        nextPageId = ROOT_VIDEO_DETAILS;
                        break;
                    case ProgramRouterPath.PROGRAM_DRAMA:
                        nextPageId = ROOT_DRAMA;
                        break;
                }
            }
        }
        return nextPageId;
    }

    public static boolean isHomePage(Starter starter) {
        if (starter != null) {
            String className = starter.getClass().getName();
            if ("com.whaley.biz.launcher.activitys.MainActivity".equals(className)) {
                return true;
            }
        }
        return false;
    }

}
