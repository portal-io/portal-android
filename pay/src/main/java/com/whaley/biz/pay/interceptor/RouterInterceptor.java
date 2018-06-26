package com.whaley.biz.pay.interceptor;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.model.ThirdPayModel;
import com.whaley.biz.pay.model.user.UserModel;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.AbsRouterInterceptor;
import com.whaley.core.router.Executor;
import com.whaley.core.router.NavigatorRouterMeta;
import com.whaley.core.router.Router;
import com.whaley.core.router.RouterInterceptorCallback;
import com.whaley.core.router.RouterMeta;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.OnBackMainThread;

/**
 * Author: qxw
 * Date:2017/8/31
 * Introduction:
 */
@Interceptor(priority = 1)
public class RouterInterceptor extends AbsRouterInterceptor implements IInterceptor {
    private Context context;

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void process(RouterMeta routerMeta, RouterInterceptorCallback routerInterceptorCallback) {
        if (!(routerMeta instanceof NavigatorRouterMeta)) {
            routerInterceptorCallback.onContinue(routerMeta);
            return;
        }
        NavigatorRouterMeta navigatorRouterMeta = (NavigatorRouterMeta) routerMeta;
        if ("/pay/ui/paydialog".equals(navigatorRouterMeta.getPath())) {
            checklogin(navigatorRouterMeta);
            routerInterceptorCallback.onInterrupt(null);
        } else {
            routerInterceptorCallback.onContinue(routerMeta);
        }
    }

    private void checklogin(NavigatorRouterMeta navigatorRouterMeta) {
        final Starter starter = navigatorRouterMeta.getStarter();
        final Bundle bundle = navigatorRouterMeta.getExtras();
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                Log.d("PAY", "login");
                showDialog(starter, bundle, true);
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                Log.d("PAY", "not login");
                showDialog(starter, bundle, false);
            }
        }).excute();
    }


    private void showDialog(final Starter starter, final Bundle bundle, final boolean isLogin) {
        OnBackMainThread.onBackMainThread(new OnBackMainThread.OnBackMainThreadListener() {
            @Override
            public void onBackMainThread() {
                if (isLogin) {
                    showPayDialog(starter, bundle);
                } else {
                    showLoginDialog(starter, bundle);
                }
            }
        });
    }

    private void showLoginDialog(final Starter starter, final Bundle bundle) {
        DialogUtil.showDialog(starter.getAttatchContext(),
                bundle.getString("key_login_tips"), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TitleBarActivity.goPage(starter, 0, "/user/ui/login");
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventController.postEvent(new BaseEvent("not_login_pay"));
                    }
                });
    }


    private void showPayDialog(final Starter starter, final Bundle bundle) {
        Log.d("PAY", "payMethod START");
        final boolean[] isBack = {false, false};
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (starter instanceof CommonActivity) {
                    if (!isBack[0] && !isBack[1]) {
                        ((CommonActivity) starter).showLoading(null);
                    }
                }
            }
        }, 300);
        final ThirdPayModel thirdPayModel = GsonUtil.getGson().fromJson(bundle.getString("key_pay_data"), ThirdPayModel.class);
        Router.getInstance().buildExecutor("/pay/service/useramount").callback(new Executor.Callback<Integer>() {
            @Override
            public void onCall(Integer money) {
                thirdPayModel.setWhaleyMoney(money);
                isBack[1] = true;
                if (isBack[0]) {
                    showPayDialog(thirdPayModel, starter);
                }
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {

            }
        }).excute();
        PayUtil.payMethod(new PayUtil.ThirdPayMethodListener() {
            @Override
            public void onComeBack() {
                isBack[0] = true;
                if (isBack[1]) {
                    showPayDialog(thirdPayModel, starter);
                }
            }
        });
    }

    private void showPayDialog(ThirdPayModel thirdPayModel, Starter starter) {
        if (starter instanceof CommonActivity) {
            ((CommonActivity) starter).removeLoading();
        }
        FragmentManager fragmentManager = null;
        if (starter instanceof Fragment) {
            fragmentManager = ((Fragment) starter).getFragmentManager();
        } else if (starter instanceof FragmentActivity) {
            fragmentManager = ((FragmentActivity) starter).getSupportFragmentManager();
        } else if (starter instanceof ContextWrapper) {
            Context context2 = ((ContextWrapper) context).getBaseContext();
            if (context2 instanceof FragmentActivity) {
                fragmentManager = ((FragmentActivity) starter).getSupportFragmentManager();
            }
        }
        Log.d("PAY", "showPayDialog");
        PayUtil.showPayDialog(fragmentManager,thirdPayModel);
    }

}
