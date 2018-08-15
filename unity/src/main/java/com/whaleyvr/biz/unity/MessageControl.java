package com.whaleyvr.biz.unity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whaley.biz.common.utils.StringUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.VersionUtil;
import com.whaleyvr.biz.unity.model.DeviceResultInfo;
import com.whaleyvr.biz.unity.model.LiveVRInfo;
import com.whaleyvr.biz.unity.model.LocalVideoInfo;
import com.whaleyvr.biz.unity.model.MatchInfo;
import com.whaleyvr.biz.unity.model.MediaInfo;
import com.whaleyvr.biz.unity.model.PayResultInfo;
import com.whaleyvr.biz.unity.model.TestInfo;
import com.whaleyvr.biz.unity.model.UserInfo;
import com.whaleyvr.biz.unity.model.user.UserModel;
import com.whaleyvr.biz.unity.util.HermesConstants;
import com.whaleyvr.biz.unity.util.LoginUtil;
import com.whaleyvr.biz.unity.vrone.SplitPlayerActivity;
import com.whaleyvr.core.network.http.HttpManager;

import java.util.concurrent.LinkedBlockingQueue;

import xiaofei.library.hermes.Hermes;
import xiaofei.library.hermes.HermesListener;
import xiaofei.library.hermes.HermesService;

/**
 * Created by dell on 2016/11/23.
 */

public class MessageControl implements HermesConstants {

    private static MessageControl sInstance = null;
    private LinkedBlockingQueue<Event> taskQueue = new LinkedBlockingQueue<>();
    private Gson gson;

    private MessageControl() {
        Hermes.setHermesListener(new HermesListener() {
            @Override
            public void onHermesConnected(Class<? extends HermesService> service) {
                executeNextTask();
            }
        });
    }

    private void executeNextTask() {
        Event event = taskQueue.poll();
        if (event != null) {
            handleEvent(event);
            executeNextTask();
        }
    }

    public static synchronized MessageControl getInstance() {
        if (sInstance == null) {
            sInstance = new MessageControl();
        }
        return sInstance;
    }

    public boolean isConnected() {
        return Hermes.isConnected(HermesService.HermesService1.class);
    }

    public void startUnityLauncher() {
        taskQueue.clear();
        Log.d("Unity", "startUnityLauncher");
        SplitPlayerActivity.startScene(AppContextProvider.getInstance().getContext(), EVENT_SCENE, "", "");
    }

    public void startUnityPlayer(MediaInfo mediaInfo) {
        taskQueue.clear();
        Log.d("Unity", "startUnityPlayer : " + getGson().toJson(mediaInfo));
        SplitPlayerActivity.startScene(AppContextProvider.getInstance().getContext(), EVENT_PLAY,
                "MediaInfo", getGson().toJson(mediaInfo));
    }

    public void startUnityShow(final String room_id) {
        taskQueue.clear();
        LoginUtil.checkLogin(new LoginUtil.LoginCallback() {
            @Override
            public void onLogin(UserModel userModel) {
                startUnityShow(room_id, userModel.getAccessTokenBean().getAccesstoken());
            }

            @Override
            public void onNotLogin() {
                startUnityShow(room_id, "");
            }
        });
    }

    private void startUnityShow(String room_id, String access_token) {
        LiveVRInfo liveVRInfo = new LiveVRInfo(access_token, AppUtil.getDeviceId(), room_id);
        SplitPlayerActivity.startScene(AppContextProvider.getInstance().getContext(), EVENT_SHOW, "LiveVRInfo",
                getGson().toJson(liveVRInfo));
    }

    public void startUnitySoccer(MatchInfo matchInfo) {
        taskQueue.clear();
        String json = "";
        if (matchInfo != null) {
            json = getGson().toJson(matchInfo);
        }
        Log.d("Unity", "startUnitySoccer : " + json);
        SplitPlayerActivity.startScene(AppContextProvider.getInstance().getContext(), EVENT_SOCCER, "MatchInfo", json);
    }

    public void sendEvent(final String eventName) {
        sendEvent(eventName, "");
    }

    public void sendEvent(final String eventName, String json) {
        Event event = new Event(eventName, json);
        if (isConnected()) {
            handleEvent(event);
        } else {
            taskQueue.add(event);
            Hermes.connect(AppContextProvider.getInstance().getContext(), HermesService.HermesService1.class);
        }
    }

    private void handleEvent(Event event) {
        switch (event.getEventName()) {
            case EVENT_RESUME:
                manager2UnityResume();
                break;
            case EVENT_SYN:
                sendIsTest();
                sendUserInfo();
                break;
            case EVENT_SEND_USERINFO:
                sendUserInfo();
                break;
            case EVENT_SEND_LOGIN:
                sendUserInfo();
                sendMetadata();
                manager2UnityResume();
                break;
            case EVENT_SEND_METADATA:
                sendMetadata();
                break;
            case EVENT_VIDEO_PAYMENT_CALLBAK:
                sendPayResult(event.getJson());
                break;
            case EVENT_REPORT_DEVICE_CALLBACK:
                sendReportDeviceResult(event.getJson());
                break;
            case EVENT_CHECK_DEVICE_CALLBACK:
                sendCheckDeviceResult(event.getJson());
                break;
            case EVENT_EXCHANGE_CODE_RESULT:
                managerUnityResume(EVENT_EXCHANGE_CODE_RESULT);
                break;
            case EVENT_OPERATION_LOCALVIDEO_CALLBACK:
                sendOperationLocalVideoCallback(event.getJson());
                break;
        }
    }

    private void sendReportDeviceResult(String json) {
        try {
            IMessage message = Hermes.getInstanceInService(HermesService.HermesService1.class, IMessage.class);
            message.sendEvent(EVENT_REPORT_DEVICE_CALLBACK, DeviceResultInfo.class.getSimpleName(), json);
        } catch (Exception e) {
            Log.e(e, "sendReportDeviceResult");
        }
    }

    private void sendCheckDeviceResult(String json) {
        try {
            IMessage message = Hermes.getInstanceInService(HermesService.HermesService1.class, IMessage.class);
            message.sendEvent(EVENT_CHECK_DEVICE_CALLBACK, DeviceResultInfo.class.getSimpleName(), json);
        } catch (Exception e) {
            Log.e(e, "sendCheckDeviceResult");
        }
    }

    private void sendOperationLocalVideoCallback(String json) {
        try {
            IMessage message = Hermes.getInstanceInService(HermesService.HermesService1.class, IMessage.class);
            message.sendEvent(EVENT_OPERATION_LOCALVIDEO_CALLBACK, LocalVideoInfo.class.getSimpleName(), json);
        } catch (Exception e) {
            Log.e(e, "sendOperationLocalVideoCallback");
        }
    }

    private void sendPayResult(String json) {
        try {
            IMessage message = Hermes.getInstanceInService(HermesService.HermesService1.class, IMessage.class);
            message.sendEvent(EVENT_VIDEO_PAYMENT_CALLBAK, PayResultInfo.class.getSimpleName(), json);
            Log.e("Payed", "EVENT_VIDEO_PAYMENT_CALLBAK" + "json=" + json);
        } catch (Exception e) {
            Log.e("Payed", "sendPayResult" + "error");
        }
    }

    private void sendIsTest() {
        TestInfo testInfo = new TestInfo(HttpManager.getInstance().isTest(), StringUtil.setResolveString(VersionUtil.getVersionName()),
                VersionUtil.getVersionCode(), AppUtil.getDeviceId());
        try {
            IMessage message = Hermes.getInstanceInService(HermesService.HermesService1.class, IMessage.class);
            message.sendEvent(EVENT_SEND_ISTEST, TestInfo.class.getSimpleName(), getGson().toJson(testInfo));
        } catch (Exception e) {
            Log.e(e, "sendIsTest");
        }
    }

    private void sendMetadata() {
        try {
            Router.getInstance().buildExecutor("/datastatistics/service/getmetadata").notTransCallbackData().callback(new Executor.Callback<String>() {
                @Override
                public void onCall(String json) {
                    if (json == null) {
                        json = "";
                    }
                    IMessage message = Hermes.getInstanceInService(HermesService.HermesService1.class, IMessage.class);
                    message.sendEvent(EVENT_SEND_METADATA, "ActivityMetadata", json);
                }

                @Override
                public void onFail(Executor.ExecutionError executionError) {
                    IMessage message = Hermes.getInstanceInService(HermesService.HermesService1.class, IMessage.class);
                    message.sendEvent(EVENT_SEND_METADATA, "ActivityMetadata", "");
                }
            }).excute();
        } catch (Exception e) {
            //
        }
    }

    private void manager2UnityResume() {
        try {
            IMessage message = Hermes.getInstanceInService(HermesService.HermesService1.class, IMessage.class);
            message.sendEvent(EVENT_RESUME, "", "");
        } catch (Exception e) {
            //
        }
    }

    /**
     * 发送空消息
     */
    private void managerUnityResume(String type) {
        try {
            IMessage message = Hermes.getInstanceInService(HermesService.HermesService1.class, IMessage.class);
            message.sendEvent(type, "", "");
        } catch (Exception e) {
            Log.e(e, "manager2UnityResume");
        }
    }

    private void sendUserInfo() {
        LoginUtil.checkLogin(new LoginUtil.LoginCallback() {
            @Override
            public void onLogin(UserModel userModel) {
                long mwid = Long.valueOf(userModel.getAccount_id());
                UserInfo userInfo = new UserInfo(mwid, userModel.getNickname(),
                        userModel.getAccessTokenBean().getAccesstoken(), AppUtil.getDeviceId(), userModel.getAvatar());
                sendUserInfo(getGson().toJson(userInfo));
            }

            @Override
            public void onNotLogin() {
                sendUserInfo("");
            }
        });
    }

    private void sendUserInfo(String json) {
        try {
            IMessage message = Hermes.getInstanceInService(HermesService.HermesService1.class, IMessage.class);
            message.sendEvent(EVENT_SYN, UserInfo.class.getSimpleName(), json);
        } catch (Exception e) {
            //
        }
    }

    private Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().disableHtmlEscaping().create();
        }
        return gson;
    }

}
