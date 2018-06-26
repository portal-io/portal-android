package com.whaleyvr.biz.hybrid.router;

import android.widget.Toast;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.core.appcontext.Starter;
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
            startRouter(starter, pageModel, pageModel.getRequsetCode());
            return;
        }
        if (StrUtil.isEmpty(pageModel.getMsg())) {
            return;
        }
        Toast.makeText(starter.getAttatchContext(), pageModel.getMsg(), Toast.LENGTH_SHORT).show();
    }

    private static void startUnity(PageModel pageModel) {
        Router.getInstance().buildExecutor(pageModel.getRouterPath()).putObjParam(pageModel.getUnityJson()).excute();
    }


    private static void startRouter(Starter starter, PageModel pageModel, int requsetCode) {
        Router.getInstance().buildNavigation(pageModel.getRouterPath())
                //設置 starter
                .setStarter(starter)
                //設置 requestCode
                .setRequestCode(requsetCode)
                .with(pageModel.getBundle())
                .withInt(RouterConstants.KEY_ACTIVITY_TYPE, pageModel.getActivityType())
                .navigation();
    }


}
