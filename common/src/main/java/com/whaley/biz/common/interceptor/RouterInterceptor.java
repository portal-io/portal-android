package com.whaley.biz.common.interceptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.router.AbsRouterInterceptor;
import com.whaley.core.router.NavigatorRouterMeta;
import com.whaley.core.router.RouterInterceptorCallback;
import com.whaley.core.router.RouterMeta;
import com.whaley.core.router.RouterTag;

import java.lang.reflect.Field;

/**
 * Created by yangzhi on 2017/8/15.
 */

@Interceptor(priority = 4)
public class RouterInterceptor extends AbsRouterInterceptor implements IInterceptor {
    private Context context;

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void process(RouterMeta routerMeta, final RouterInterceptorCallback routerInterceptorCallback) {
        if (!(routerMeta instanceof NavigatorRouterMeta)) {
            routerInterceptorCallback.onContinue(routerMeta);
            return;
        }
        final NavigatorRouterMeta navigatorRouterMeta = (NavigatorRouterMeta) routerMeta;
        switch (routerMeta.getType()) {
            case FRAGMENT:
                final Starter starter = navigatorRouterMeta.getStarter();
                Bundle bundle = navigatorRouterMeta.getExtras();
                int activityType = bundle.getInt(RouterConstants.KEY_ACTIVITY_TYPE);
//                Bundle childBundle = bundle.getBundle(RouterConstants.KEY_BUNDLE_TYPE);
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
//                if (childBundle != null) {
//                    intent.putExtras(childBundle);
//                } else {
                intent.putExtras(navigatorRouterMeta.getExtras());
//                }

                // Set flags.
                int flags = navigatorRouterMeta.getFlags();
                if (-1 != flags) {
                    intent.setFlags(flags);
                } else if (!(starter.getAttatchContext() instanceof Activity)) {    // Non activity, need less one flag.
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                // Navigation in main looper.
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        starter.startActivityForResult(intent, navigatorRouterMeta.getRequestCode());

                        if ((0 != navigatorRouterMeta.getEnterAnim() || 0 != navigatorRouterMeta.getExitAnim()) && starter.getAttatchContext() instanceof Activity) {    // Old version.
                            ((Activity) starter.getAttatchContext()).overridePendingTransition(navigatorRouterMeta.getEnterAnim(), navigatorRouterMeta.getExitAnim());
                        }
                        routerInterceptorCallback.onInterrupt(null);
                    }
                });
                break;
            default:

                routerInterceptorCallback.onContinue(routerMeta);
                break;
        }
    }

}
