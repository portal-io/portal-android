package com.whaley.biz.program.interceptor;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.google.gson.reflect.TypeToken;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.playercontroller.PlayerController;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.playersupport.event.NewPlayerPageEvent;
import com.whaley.biz.program.ui.activitys.LandScapeActivity;
import com.whaley.biz.program.ui.activitys.PortaitActivity;
import com.whaley.biz.program.ui.detail.BaseProgramDetailFragment;
import com.whaley.biz.program.ui.event.NotLoginPayEvent;
import com.whaley.biz.program.ui.player.PlayerFragment;
import com.whaley.biz.program.utils.PayUtil;
import com.whaley.core.appcontext.AppContextProvider;
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
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by yangzhi on 2017/8/15.
 */

@Interceptor(priority = 3)
public class RouterInterceptor extends AbsRouterInterceptor implements IInterceptor {
    private Context context;

    private NewPlayerPageEvent newPlayerPageEvent = new NewPlayerPageEvent();

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
//                if (navigatorRouterMeta.getPath().equals(ProgramRouterPath.PAY)) {
//                    Log.d("PAY", "checklogin START");
//                    checklogin(navigatorRouterMeta);
//                    routerInterceptorCallback.onInterrupt(null);
//                    return;
//                }
                if (navigatorRouterMeta.getPath().equals(ProgramRouterPath.WEB_NEWS) || navigatorRouterMeta.getPath().equals(ProgramRouterPath.WEB_INNER)) {
                    Bundle bundle = navigatorRouterMeta.getExtras();
                    Router.getInstance().buildExecutor("/hybrid/service/goPage").putObjParam(bundle.getParcelable(ProgramConstants.WEBVIEW_DATA)).excute();
                    routerInterceptorCallback.onInterrupt(null);
                    return;
                }
                Bundle bundle = navigatorRouterMeta.getExtras();
                Bundle copyBundle = new Bundle(bundle);
                navigatorRouterMeta.with(copyBundle);
                Class clazz = navigatorRouterMeta.getDestination();
                final boolean isPlayerFragmentChild = PlayerFragment.class.isAssignableFrom(clazz);
                final boolean isProgramDetailFragmentChild = BaseProgramDetailFragment.class.isAssignableFrom(clazz);
                if (!isPlayerFragmentChild && !isProgramDetailFragmentChild) {
                    routerInterceptorCallback.onContinue(routerMeta);
                    return;
                }
                String playDatasJson = copyBundle.getString(PlayerFragment.KEY_PARAM_DATAS, null);
                if (StrUtil.isEmpty(playDatasJson)) {
                    routerInterceptorCallback.onContinue(routerMeta);
                    return;
                }
                copyBundle.remove(PlayerFragment.KEY_PARAM_DATAS);
                final List<PlayData> playDatas = GsonUtil.getGson().fromJson(playDatasJson, new TypeToken<List<PlayData>>() {
                }.getType());

                if (playDatas != null && playDatas.size() > 0) {
                    final Starter starter = navigatorRouterMeta.getStarter();
                    PlayData playData = playDatas.get(0);
                    int type = playData.getType();
                    Intent realIntent;
                    switch (type) {
                        case PlayerType.TYPE_LIVE:
                            boolean isLandScape = playData.getBooleanCustomData(PlayerDataConstants.ORIENTATION_IS_LANDSCAPE);
                            realIntent = isLandScape ? LandScapeActivity.createIntent(starter, navigatorRouterMeta.getDestination().getName()) : PortaitActivity.createIntent(starter, navigatorRouterMeta.getDestination().getName());
                            break;
                        default:
                            realIntent = PortaitActivity.createIntent(starter, navigatorRouterMeta.getDestination().getName());
                            break;
                    }

                    final Intent intent = realIntent;

                    intent.putExtras(navigatorRouterMeta.getExtras());

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
                            if (EventBus.getDefault().hasSubscriberForEvent(NewPlayerPageEvent.class)) {
                                EventBus.getDefault().post(newPlayerPageEvent);
                            }
                            if (isPlayerFragmentChild) {
                                PlayerFragment.PLAYDATAS = playDatas;
                            } else {
                                BaseProgramDetailFragment.PLAYDATAS = playDatas;
                            }
                            starter.startActivityForResult(intent, navigatorRouterMeta.getRequestCode());
                            PlayerController.getInstance(AppContextProvider.getInstance().getContext()).release();
                            if ((0 != navigatorRouterMeta.getEnterAnim() || 0 != navigatorRouterMeta.getExitAnim()) && starter.getAttatchContext() instanceof Activity) {// Old version.
                                ((Activity) starter.getAttatchContext()).overridePendingTransition(navigatorRouterMeta.getEnterAnim(), navigatorRouterMeta.getExitAnim());
                            }
                        }
                    });
                    routerInterceptorCallback.onInterrupt(null);
                } else {
                    routerInterceptorCallback.onContinue(routerMeta);
                }
                break;
            default:
                routerInterceptorCallback.onContinue(routerMeta);
                break;
        }
    }


//    private void checklogin(NavigatorRouterMeta navigatorRouterMeta) {
//        final Starter starter = navigatorRouterMeta.getStarter();
//        final Bundle bundle = navigatorRouterMeta.getExtras();
//        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
//            @Override
//            public void onCall(UserModel userModel) {
//                Log.d("PAY", "login");
//                showDialog(starter, bundle, true);
//            }
//
//            @Override
//            public void onFail(Executor.ExecutionError executionError) {
//                Log.d("PAY", "not login");
//                showDialog(starter, bundle, false);
//            }
//        }).excute();
//    }

//    private void showDialog(final Starter starter, final Bundle bundle, final boolean isLogin) {
//        OnBackMainThread.onBackMainThread(new OnBackMainThread.OnBackMainThreadListener() {
//            @Override
//            public void onBackMainThread() {
//                if (isLogin) {
//                    showPayDialog(starter, bundle);
//                } else {
//                    showLoginDialog(starter, bundle);
//                }
//            }
//        });
//    }

//    private void showLoginDialog(final Starter starter, final Bundle bundle) {
//        DialogUtil.showDialog(starter.getAttatchContext(),
//                bundle.getString(ProgramConstants.KEY_LOGIN_TIPS), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        TitleBarActivity.goPage(starter, 0, "/user/ui/login");
//                    }
//                }, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        EventController.postEvent(new NotLoginPayEvent(ProgramConstants.EVENT_NOT_LOGIN_PAY));
//                    }
//                });
//    }
//
//
//    private void showPayDialog(final Starter starter, final Bundle bundle) {
//        Log.d("PAY", "payMethod START");
//        final boolean[] isBack = {false};
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (starter instanceof CommonActivity) {
//                    if (!isBack[0]) {
//                        ((CommonActivity) starter).showLoading(null);
//                    }
//                }
//            }
//        }, 200);
//        PayUtil.payMethod(new PayUtil.ThirdPayMethodListener() {
//            @Override
//            public void onComeBack() {
//                isBack[0] = true;
//                if (starter instanceof CommonActivity) {
//                    ((CommonActivity) starter).removeLoading();
//                }
//                FragmentManager fragmentManager = null;
//                if (starter instanceof Fragment) {
//                    fragmentManager = ((Fragment) starter).getFragmentManager();
//                } else if (starter instanceof FragmentActivity) {
//                    fragmentManager = ((FragmentActivity) starter).getSupportFragmentManager();
//                } else if (starter instanceof ContextWrapper) {
//                    Context context2 = ((ContextWrapper) context).getBaseContext();
//                    if (context2 instanceof FragmentActivity) {
//                        fragmentManager = ((FragmentActivity) starter).getSupportFragmentManager();
//                    }
//                }
//                Log.d("PAY", "showPayDialog");
//                PayUtil.showPayDialog(fragmentManager, bundle.getString(ProgramConstants.KEY_PAY_DATA));
//            }
//        });
//    }
}
