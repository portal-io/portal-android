package com.whaley.biz.program.utils;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Router;
import com.whaley.core.utils.StrUtil;

/**
 * Author: qxw
 * Date:2017/8/17
 * Introduction:
 */

public class GoPageUtil {

    public static void goPage(Starter starter, PageModel pageModel) {
        if (pageModel == null) {
            return;
        }
        if (pageModel.isCanJump()) {
            if (pageModel.isStartUnity()) {
                startUnity(pageModel);
                return;
            }
            if (pageModel.getRouterPath().equals(ProgramRouterPath.WEB_OUTSIDE)) {
                startOutWeb(starter, pageModel.getBundle());
                return;
            }
            startRouter(starter, pageModel);
            return;
        }
        if (StrUtil.isEmpty(pageModel.getMsg())) {
            return;
        }
        Toast.makeText(starter.getAttatchContext(), pageModel.getMsg(), Toast.LENGTH_SHORT).show();
    }

    private static void startOutWeb(Starter starter, Bundle bundle) {
        try {
            Intent i = new Intent();
            i.setAction("android.intent.action.VIEW");
            Uri u = Uri.parse(bundle.getString(ProgramConstants.WEBVIEW_URL));
            i.setData(u);
            starter.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Log.e(e, "StartController");
        }
    }

    private static void startUnity(PageModel pageModel) {
        Router.getInstance().buildExecutor(pageModel.getRouterPath()).putObjParam(pageModel.getUnityObject()).excute();
    }


    private static void startRouter(Starter starter, PageModel pageModel) {
        Router.getInstance().buildNavigation(pageModel.getRouterPath())
                //設置 starter
                .setStarter(starter)
                //設置 requestCode
                .setRequestCode(pageModel.getRequestCode())
                .with(pageModel.getBundle())
                .withInt(RouterConstants.KEY_ACTIVITY_TYPE, pageModel.getActivityType())
                .navigation();
    }


}
