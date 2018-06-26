package com.whaley.biz.user.interceptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.event.LoginCancelEvent;
import com.whaley.biz.user.ui.view.LoginFragment;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.AbsRouterInterceptor;
import com.whaley.core.router.NavigatorRouterMeta;
import com.whaley.core.router.RouterInterceptorCallback;
import com.whaley.core.router.RouterMeta;
import com.whaley.core.utils.OnBackMainThread;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangzhi on 2017/8/15.
 */

@Interceptor(priority = 2)
public class RouterInterceptor extends AbsRouterInterceptor implements IInterceptor {

    public final static String KEY_LOGIN_TIPS = "key_login_tips";

    public static LinkedHashMap<String, RouterMeta> routerMap = new LinkedHashMap<String, RouterMeta>() {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, RouterMeta> eldest) {
            return size() > 1;
        }
    };

    @Override
    public void init(Context context) {
        //
    }

    @Override
    public void process(final RouterMeta routerMeta, final RouterInterceptorCallback routerInterceptorCallback) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        if (!(routerMeta instanceof NavigatorRouterMeta)) {
            routerInterceptorCallback.onContinue(routerMeta);
            return;
        }
        routerMap.clear();
        final NavigatorRouterMeta navigatorRouterMeta = (NavigatorRouterMeta) routerMeta;
        switch (routerMeta.getType()) {
            case FRAGMENT:
                if (navigatorRouterMeta.getExtra() == RouterConstants.EXTRA_LOGIN) {
                    if (UserManager.getInstance().isLogin()) {
                        routerInterceptorCallback.onContinue(routerMeta);
                    } else {
                        OnBackMainThread.onBackMainThread(new OnBackMainThread.OnBackMainThreadListener() {
                            @Override
                            public void onBackMainThread() {
                                showLoginDialog(navigatorRouterMeta.getStarter(), navigatorRouterMeta.getExtras(), routerMeta);
                            }
                        });
                        routerInterceptorCallback.onInterrupt(null);
                    }
                } else {
                    routerInterceptorCallback.onContinue(routerMeta);
                }
                break;
            default:
                routerInterceptorCallback.onContinue(routerMeta);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case UserConstants.EVENT_LOGIN_SUCCESS:
                String path = event.getObject();
                RouterMeta routerMeta = routerMap.get(path);
                if (routerMeta != null) {
                    Log.d("continueRouter");
                    continueRouter(routerMeta);
                }
                routerMap.clear();
                break;
        }
    }

    private void showLoginDialog(final Starter starter, final Bundle bundle, final RouterMeta routerMeta) {
        DialogUtil.showDialog(starter.getAttatchContext(),
                bundle.getString(KEY_LOGIN_TIPS), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        routerMap.put(routerMeta.getPath(), routerMeta);
                        LoginFragment.goPage(starter, routerMeta.getPath(), 0);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(EventController.hasSubscriberForEvent(LoginCancelEvent.class)) {
                            EventController.postEvent(new LoginCancelEvent());
                        }
                    }
                });
    }

    private void continueRouter(RouterMeta routerMeta) {
        if (!(routerMeta instanceof NavigatorRouterMeta)) {
            return;
        }
        final NavigatorRouterMeta navigatorRouterMeta = (NavigatorRouterMeta) routerMeta;
        final Starter starter = navigatorRouterMeta.getStarter();
        Bundle bundle = navigatorRouterMeta.getExtras();
        int activityType = bundle.getInt(RouterConstants.KEY_ACTIVITY_TYPE);
        Intent realIntent;
        switch (activityType) {
            case RouterConstants.TITLE_BAR_ACTIVITY:
                realIntent = TitleBarActivity.createIntent(starter, navigatorRouterMeta.getDestination());
                break;
            default:
                realIntent = CommonActivity.createIntent(starter, navigatorRouterMeta.getDestination().getName(), null);
                break;
        }
        final Intent intent = realIntent;
        intent.putExtras(navigatorRouterMeta.getExtras());
        int flags = navigatorRouterMeta.getFlags();
        if (-1 != flags) {
            intent.setFlags(flags);
        } else if (!(starter.getAttatchContext() instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                starter.startActivityForResult(intent, navigatorRouterMeta.getRequestCode());
                if ((0 != navigatorRouterMeta.getEnterAnim() || 0 != navigatorRouterMeta.getExitAnim()) && starter.getAttatchContext() instanceof Activity) {    // Old version.
                    ((Activity) starter.getAttatchContext()).overridePendingTransition(navigatorRouterMeta.getEnterAnim(), navigatorRouterMeta.getExitAnim());
                }
            }
        });
    }

}
