package com.whaleyvr.biz.unity.router;

import android.view.View;
import android.widget.Toast;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.router.Router;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.biz.unity.util.LoginUtil;
import com.whaleyvr.biz.unity.model.user.UserModel;

/**
 * Author: qxw
 * Date:2017/8/17
 * Introduction:
 */

public class GoPageUtil {

    public static void goPageForceLogin(final Starter starter, final PageModel pageModel){
        if (pageModel == null) {
            return;
        }
        LoginUtil.checkLogin(new LoginUtil.LoginCallback() {
            @Override
            public void onLogin(UserModel userModel) {
                goPage(starter, pageModel);
            }
            @Override
            public void onNotLogin() {
                //
            }
        });
    }

    public static void goPageForceLogin(final Starter starter, final PageModel pageModel, final int loginRequestCode, final String loginMsg){
        if (pageModel == null) {
            return;
        }
        LoginUtil.checkLogin(new LoginUtil.LoginCallback() {
            @Override
            public void onLogin(UserModel userModel) {
                goPage(starter, pageModel);
            }
            @Override
            public void onNotLogin() {
                DialogUtil.showDialog(starter.getAttatchContext(), loginMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startRouter(starter, PageModel.obtain(RouterPath.LOGIN), loginRequestCode);
                    }
                });
            }
        });
    }

    public static void goPage(Starter starter, PageModel pageModel) {
        if (pageModel == null) {
            return;
        }
        if (pageModel.isCanJump()) {
            if (pageModel.isStartUnity()) {
                startUnity(pageModel);
                return;
            }
            startRouter(starter, pageModel, 0);
            return;
        }
        if (StrUtil.isEmpty(pageModel.getMsg()) || starter == null) {
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
