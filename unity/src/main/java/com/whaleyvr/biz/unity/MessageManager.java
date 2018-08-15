package com.whaleyvr.biz.unity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.event.EventController;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaleyvr.biz.unity.event.ChangeDramaProgressEvent;
import com.whaleyvr.biz.unity.event.ChangeVideoProgressEvent;
import com.whaleyvr.biz.unity.event.ExitPlayEvent;
import com.whaleyvr.biz.unity.event.ExitSplitEvent;
import com.whaleyvr.biz.unity.event.KickOutEvent;
import com.whaleyvr.biz.unity.model.DeviceResultInfo;
import com.whaleyvr.biz.unity.model.LocalVideoInfo;
import com.whaleyvr.biz.unity.model.MediaResultInfo;
import com.whaleyvr.biz.unity.model.user.UserModel;
import com.whaleyvr.biz.unity.router.GoPageUtil;
import com.whaleyvr.biz.unity.router.PageModel;
import com.whaleyvr.biz.unity.router.RouterPath;
import com.whaleyvr.biz.unity.util.HermesConstants;
import com.whaleyvr.biz.unity.util.LoginUtil;

import xiaofei.library.hermes.annotation.ClassId;
import xiaofei.library.hermes.annotation.MethodId;

/**
 * Created by dell on 2016/11/23.
 */

@ClassId("ServerMessage")
public class MessageManager implements IServerMessage, HermesConstants {

   private static MessageManager sInstance = null;

    private Handler handler;

    private MessageManager() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static synchronized MessageManager getInstance() {
        if (sInstance == null) {
            sInstance = new MessageManager();
        }
        return sInstance;
    }

    @MethodId("sendEvent")
    public void sendEvent(final String eventName, String dataType, final String json) {
        Log.d("Unity", "eventName : " + eventName + " / json : " + json);
        switch (eventName) {
            case EVENT_READY:
                MessageControl.getInstance().sendEvent(EVENT_SYN);
                break;
            case EVENT_LOGIN:
                LoginUtil.checkLogin(new LoginUtil.LoginCallback() {
                    @Override
                    public void onLogin(UserModel userModel) {
                        MessageControl.getInstance().sendEvent(EVENT_SEND_LOGIN);
                    }
                    @Override
                    public void onNotLogin() {
                        GoPageUtil.goPage(AppContextProvider.getInstance().getStarter(), PageModel.obtain(RouterPath.LOGIN));
                    }
                });
                break;
            case EVENT_REGISTER:
                LoginUtil.checkLogin(new LoginUtil.LoginCallback() {
                    @Override
                    public void onLogin(UserModel userModel) {
                        MessageControl.getInstance().sendEvent(EVENT_SEND_LOGIN);
                    }
                    @Override
                    public void onNotLogin() {
                        GoPageUtil.goPage(AppContextProvider.getInstance().getStarter(), PageModel.obtain(RouterPath.REGISTER));
                    }
                });
                break;
            case EVENT_GIFT:
                PageModel pageModel = PageModel.obtain(RouterPath.GIFT);
                Bundle bundle = new Bundle();
                bundle.putBoolean("str_fromUnity", true);
                pageModel.setBundle(bundle);
                GoPageUtil.goPageForceLogin(AppContextProvider.getInstance().getStarter(), pageModel);
                break;
            case EVENT_GET_METADATA:
                MessageControl.getInstance().sendEvent(EVENT_SEND_METADATA);
                break;
            case EVENT_VIDEO_PAYMENT:
                pageModel = PageModel.obtain(RouterPath.UNITY_PROGRAM);
                bundle = new Bundle();
                bundle.putString("str_unity_pay", json);
                pageModel.setBundle(bundle);
                pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
                GoPageUtil.goPage(AppContextProvider.getInstance().getStarter(), pageModel);
                break;
            case EVENT_PROGRAM_PAYMENT:
                pageModel = PageModel.obtain(RouterPath.UNITY_PACKAGE);
                bundle = new Bundle();
                bundle.putString("str_json", json);
                pageModel.setBundle(bundle);
                pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
                GoPageUtil.goPage(AppContextProvider.getInstance().getStarter(), pageModel);
                break;
            case EVENT_REPORT_DEVICE:
                reportDevice();
                break;
            case EVENT_CHECK_DEVICE:
                checkDevice();
                break;
            case EVENT_REDEEM_CODE:
                pageModel = PageModel.obtain(RouterPath.CONVERT);
                bundle = new Bundle();
                bundle.putBoolean("str_fromUnity", true);
                pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
                pageModel.setBundle(bundle);
                GoPageUtil.goPageForceLogin(AppContextProvider.getInstance().getStarter(), pageModel);
                break;
            case EVENT_OPERATION_LOCALVIDEO:
                LocalVideoInfo localVideoInfo = GsonUtil.getGson().fromJson(json, LocalVideoInfo.class);
                if(localVideoInfo==null)
                    return;
                Router.getInstance().buildExecutor("/setting/service/operationLocalVideo").putObjParam(localVideoInfo).callback(new Executor.Callback<LocalVideoInfo>() {
                    @Override
                    public void onCall(LocalVideoInfo o) {
                        if(o!=null) {
                            MessageControl.getInstance().sendEvent(EVENT_OPERATION_LOCALVIDEO_CALLBACK, GsonUtil.getGson().toJson(o));
                        }
                    }
                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        //
                    }
                }).excute();
                break;
            case EVENT_REPORT_VIDEO_PROGRESS:
                final MediaResultInfo mediaResultInfo  = GsonUtil.getGson().fromJson(json, MediaResultInfo.class);
                if(mediaResultInfo==null)
                    return;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mediaResultInfo.isKickOut()){
                            EventController.postEvent(new KickOutEvent());
                        } else if(mediaResultInfo.isDrama()){
                            EventController.postStickyEvent(new ChangeDramaProgressEvent(mediaResultInfo));
                        }else {
                            EventController.postStickyEvent(new ChangeVideoProgressEvent(mediaResultInfo));
                        }
                    }
                });
                break;
            case EVENT_SWITCH_FULLSCREEN:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ExitSplitEvent exitSplitEvent = new ExitSplitEvent();
                        exitSplitEvent.setObject(json);
                        EventController.postEvent(exitSplitEvent);
                    }
                });
                break;
            case EVENT_EXIT_PLAY:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        EventController.postStickyEvent(new ExitPlayEvent());
                    }
                });
                break;
        }
    }

    private void reportDevice() {
        Executor executor = Router.getInstance().buildObj("/pay/usecase/reportdevice").getObj();
        executor.excute(null, new Executor.Callback<Boolean>() {
            @Override
            public void onCall(Boolean aBoolean) {
                DeviceResultInfo deviceResultInfo = new DeviceResultInfo(aBoolean);
                MessageControl.getInstance().sendEvent(EVENT_REPORT_DEVICE_CALLBACK, GsonUtil.getGson().toJson(deviceResultInfo));
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                executionError.printStackTrace();
            }
        });
    }

    private void checkDevice() {
        Executor executor = Router.getInstance().buildObj("/pay/usecase/checkdevice").getObj();
        executor.excute(null, new Executor.Callback<Boolean>() {
            @Override
            public void onCall(Boolean aBoolean) {
                DeviceResultInfo deviceResultInfo = new DeviceResultInfo(aBoolean);
                MessageControl.getInstance().sendEvent(EVENT_CHECK_DEVICE_CALLBACK, GsonUtil.getGson().toJson(deviceResultInfo));
                if(!aBoolean){
                    Router.getInstance().buildExecutor("/user/service/signout").callback(new Executor.Callback<Boolean>() {
                        @Override
                        public void onCall(Boolean aBoolean) {

                        }

                        @Override
                        public void onFail(Executor.ExecutionError executionError) {
                            //
                        }
                    }).excute();
                }
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                //
            }
        });
    }

}
