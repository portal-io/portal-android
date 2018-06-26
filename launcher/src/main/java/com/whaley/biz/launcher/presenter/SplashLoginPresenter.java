package com.whaley.biz.launcher.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.snailvr.manager.R;
import com.umeng.socialize.UMShareAPI;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.launcher.App;
import com.whaley.biz.launcher.AppLike;
import com.whaley.biz.launcher.constants.UserConstants;
import com.whaley.biz.launcher.model.UserModel;
import com.whaley.biz.launcher.util.SharedPreferencesUtil;
import com.whaley.biz.launcher.util.UserUtil;
import com.whaley.biz.launcher.view.BaseSplashView;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by dell on 2017/9/5.
 */

public class SplashLoginPresenter extends BaseSplashPresenter<BaseSplashView> implements UserConstants {

    static final String EVENT_LOGIN_SUCCESS = "login_success";
    Disposable disposable;

    public SplashLoginPresenter(BaseSplashView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        regist();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegist();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case EVENT_LOGIN_SUCCESS:
                UserUtil.loginUser();
                startMainActivity();
                break;
        }
    }

    public void onLogin() {
        TitleBarActivity.goPage(getStater(), 0, "/user/ui/login");
    }

    public void onRegister() {
        TitleBarActivity.goPage(getStater(), 0, "/user/ui/register");
    }

    public void loginQQ() {
        setAuthLogin(UserConstants.TYPE_AUTH_QQ);
    }

    public void loginWeixin() {
        setAuthLogin(UserConstants.TYPE_AUTH_WX);
    }

    public void loginWeibo() {
        setAuthLogin(UserConstants.TYPE_AUTH_WB);
    }


    private void setAuthLogin(String type) {
        dispose();
        disposable = new UpdateUIObserver<String>(getUIView(), true) {
            @Override
            public void onNext(@NonNull String userJson) {
                super.onNext(userJson);
                UserModel userModel = GsonUtil.getGson().fromJson(userJson, UserModel.class);
                if (!userModel.isAddInformation()) {
                    getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.user_login_success));
                    EventController.postEvent(new BaseEvent("login_success"));
                } else {
                    Router.getInstance().buildNavigation("/user/ui/completeuserinfo")
                            //設置 starter
                            .setStarter(getStater())
                            //設置 requestCode
                            .withString("login_userbean_json", userJson)
                            .withInt(RouterConstants.KEY_ACTIVITY_TYPE, RouterConstants.TITLE_BAR_ACTIVITY)
                            .navigation();
                }
            }

            @Override
            public void onStatusError(int status, String message, String userJson) {
                super.onStatusError(status, message, userJson);
                if (status == 144) {
                    Router.getInstance().buildNavigation("/user/ui/userbind")
                            //設置 starter
                            .setStarter(getStater())
                            //設置 requestCode
                            .withString("login_userbean_json", userJson)
                            .withBoolean("third_bind", true)
                            .withInt(RouterConstants.KEY_ACTIVITY_TYPE, RouterConstants.TITLE_BAR_ACTIVITY)
                            .navigation();
                }
            }
        };
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("activity", getUIView().getAttachActivity());
        map.put("disposableObserver", disposable);
        Router.getInstance().buildExecutor("/user/service/thirdlogin").putParams(map).notTransParam().excute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(AppContextProvider.getInstance().getContext()).onActivityResult(requestCode, resultCode, data);
        if (getUIView() != null && requestCode != Activity.RESULT_OK && data == null) {
            getUIView().removeLoading();
        }
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        dispose();
    }

    @Override
    public void onDetached() {
        super.onDetached();
        UMShareAPI.get(AppContextProvider.getInstance().getContext()).release();
    }

    public void onLeapfrog() {
        startActivity();
    }


}
