package com.whaleyvr.biz.hybrid;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.sign.Sign;
import com.whaley.biz.sign.SignType;
import com.whaley.biz.sign.SignUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareListener;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.MD5Util;
import com.whaley.core.utils.NetworkUtils;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.biz.hybrid.fragments.WebViewFragment;
import com.whaleyvr.biz.hybrid.model.CopyToClipboardPayload;
import com.whaleyvr.biz.hybrid.model.DialogPayload;
import com.whaleyvr.biz.hybrid.model.MediaPayload;
import com.whaleyvr.biz.hybrid.model.RequestPayload;
import com.whaleyvr.biz.hybrid.model.SharePayload;
import com.whaleyvr.biz.hybrid.model.ShowImagesPayload;
import com.whaleyvr.biz.hybrid.model.WebPayload;
import com.whaleyvr.biz.hybrid.model.user.AccessTokenBean;
import com.whaleyvr.biz.hybrid.model.user.UserModel;
import com.whaleyvr.biz.hybrid.router.FormatPageModel;
import com.whaleyvr.biz.hybrid.router.GoPageUtil;
import com.whaleyvr.biz.hybrid.router.PageModel;
import com.whaleyvr.biz.hybrid.router.ProgramConstants;
import com.whaleyvr.biz.hybrid.router.QRcodeUtil;
import com.whaleyvr.biz.hybrid.router.RouterPath;
import com.whaleyvr.biz.hybrid.widget.VRWebView;
import com.whaleyvr.core.network.http.HttpManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 网页交互基础事件类
 * Created by YangZhi on 2016/10/27 15:21.
 */
public class BasicJSEvents {


    static final int REQUEST_CODE_LOGIN = 1000;
    static final int REQUEST_CODE_SCAN = REQUEST_CODE_LOGIN + 1;

    private boolean isLoaded = false;

    private H5Controller h5Controller;

    String ROOT_H5_DETAIL = "webDetail";//內部h5页


    private ConcurrentHashMap<String, JSComplete> jsCompleteMap = new ConcurrentHashMap<>();

    final NetworkRegister networkRegister = new NetworkRegister();


    private BasicJSEvents(H5Controller h5Controller) {
        this.h5Controller = h5Controller;
    }


    public static BasicJSEvents getInstance(H5Controller h5Controller) {
        return new BasicJSEvents(h5Controller);
    }


    public boolean isLoaded() {
        return isLoaded;
    }

    public boolean registEvents() {

        //获取APP版本信息
        h5Controller.onEvent("getAppClientInfo", new H5Callback() {
            @Override
            public Map<Object, Object> doPerform(Object payload) {
                Map<Object, Object> callbackPayload = new HashMap<Object, Object>();
                callbackPayload.put("platform", "android");
                callbackPayload.put("versionCode", CommonConstants.VALUE_APP_VERSION_CODE);
                callbackPayload.put("deviceId", AppUtil.getDeviceId());
                return callbackPayload;
            }
        });

        //退出监听
        h5Controller.onEvent("exit", new H5Callback() {
            @Override
            public Map<Object, Object> doPerform(Object payload) {
                h5Controller.getStarter().finish();
                return null;
            }
        });

        //调用登录界面监听
        h5Controller.onEvent("login", new H5AsyncCallback<Object>() {
            @Override
            public void doPerform(Object payload, JSComplete jsComplete) {
                jsCompleteMap.put(jsComplete.getCallbackId(), jsComplete);
                PageModel pageModel = PageModel.obtain(RouterPath.LOGIN);
                Bundle bundle = new Bundle();
                bundle.putString("str_callbackId", jsComplete.getCallbackId());
                pageModel.setBundle(bundle);
                pageModel.setRequsetCode(REQUEST_CODE_LOGIN);
                GoPageUtil.goPage(h5Controller.getStarter(), pageModel);
            }
        });

        //获取登录用户监听
        h5Controller.onEvent("getLoginUserInfo", new H5Callback() {
            UserModel retUserModel = null;

            @Override
            public Map<Object, Object> doPerform(Object payload) {

                Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
                    @Override
                    public void onCall(UserModel userModel) {
                        retUserModel = userModel;
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        retUserModel = null;
                    }
                }).excute();
                if (retUserModel == null) {
                    return null;
                }
                HashMap callbackPayload = new HashMap<Object, Object>();
                retUserModel.setLoginUser(true);
                callbackPayload.put("user", retUserModel);
                return callbackPayload;
            }
        });

        //当web数据渲染完成监听
        h5Controller.onEvent("completeLoadingPage", new H5Callback<Object>() {
            @Override
            public Map<Object, Object> doPerform(Object payload) {
                isLoaded = true;
                h5Controller.showContent();
                return null;
            }
        });

        //打开二维码扫描
        h5Controller.onEvent("scanQRCode", new H5AsyncCallback<Object>() {
            @Override
            public void doPerform(Object payload, JSComplete jsComplete) {
                jsCompleteMap.put(jsComplete.getCallbackId(), jsComplete);
                if (!QRcodeUtil.onClickQrcode(getActivity())) {
                    sendScanCallback(jsComplete, 0, "");
                    jsCompleteMap.remove(jsComplete.getCallbackId());
                } else {
                    PageModel pageModel = PageModel.obtain(RouterPath.QRCODE);
                    Bundle bundle = new Bundle();
                    bundle.putString("str_callbackId", jsComplete.getCallbackId());
                    pageModel.setBundle(bundle);
                    pageModel.setRequsetCode(REQUEST_CODE_SCAN);
                    GoPageUtil.goPage(h5Controller.getStarter(), pageModel);
                }
            }
        });

        //显示图片监听(查看大图)
        h5Controller.onEvent("showImages", new H5Callback<ShowImagesPayload>() {
            @Override
            public Map<Object, Object> doPerform(ShowImagesPayload payload) {
//                PicBrowserFragment.goPage(h5Controller.getStarter(), payload.getImgs(), payload.getIndex());
                return null;
            }
        });

        //打开播放器监听
        h5Controller.onEvent("openVideo", new H5Callback<MediaPayload>() {
            @Override
            public Map<Object, Object> doPerform(MediaPayload payload) {
                MediaPayload.MediaModel mediaModel = payload.getMediaModel();
                boolean isLive = ProgramConstants.TYPE_LIVE.equals(mediaModel.getType());
                GoPageUtil.goPage(h5Controller.getStarter(), FormatPageModel.getPlayerPageModel(mediaModel.getPlayData(), isLive));
                return null;
            }
        });

        //分享监听
        h5Controller.onEvent("share", new H5AsyncCallback<SharePayload.ShareModel>() {
            @Override
            public void doPerform(final SharePayload.ShareModel shareModel, final JSComplete complete) {
                if (getActivity() != null) {
                    ShareModel share = ShareModel.createBuilder().setUrl(shareModel.getUrl())
                            .setTitle(shareModel.getTitle())
                            .setDes(shareModel.getDesc())
                            .setType(shareModel.getPlatform())
                            .setImgUrl(shareModel.getImgUrl())
                            .setShareType(ShareTypeConstants.TYPE_SHARE_WEB)
                            .build();
                    Router.getInstance().buildExecutor("/share/service/sharemodel").putObjParam(share).notTransParam()
                            .callback(new Executor.Callback<ShareParam.Builder>() {
                                @Override
                                public void onCall(ShareParam.Builder builder) {
                                    builder.setContext(getActivity())
                                            .setShareListener(new ShareListener() {
                                                @Override
                                                public void onResult(int type) {
                                                    sendShareCallback(complete, type, 1);
                                                }

                                                @Override
                                                public void onError(int type, Throwable throwable) {
                                                    sendShareCallback(complete, type, 0);
                                                }

                                                @Override
                                                public void onCancel(int type) {
                                                    sendShareCallback(complete, type, 2);
                                                }
                                            });
                                    builder.setContext(getActivity());
                                    ShareUtil.share(builder.build());
                                }

                                @Override
                                public void onFail(Executor.ExecutionError executionError) {

                                }
                            }).notTransCallbackData().excute();
//                    ShareParam shareParam = ShareParam.createBuilder()
//                            .setTitle(shareModel.getTitle())
//                            .setUrl(shareModel.getUrl())
//                            .setContext(getActivity())
//                            .setDes(shareModel.getDesc())
//                            .setType(shareModel.getPlatform())
//                            .setFrom(ROOT_H5_DETAIL)
//                            .setShareType(ShareTypeConstants.TYPE_SHARE_WEB)
//                            .setShareListener(new ShareListener() {
//                                @Override
//                                public void onResult(int type) {
//                                    sendShareCallback(complete, type, 1);
//                                }
//
//                                @Override
//                                public void onError(int type, Throwable throwable) {
//                                    sendShareCallback(complete, type, 0);
//                                }
//
//                                @Override
//                                public void onCancel(int type) {
//                                    sendShareCallback(complete, type, 2);
//                                }
//                            }).build();

                }
            }


            private void sendShareCallback(JSComplete complete, int platForm, int status) {
                Map<Object, Object> callbackPayload = new HashMap<Object, Object>();
                callbackPayload.put("status", status);
                callbackPayload.put("platform", platForm);
                sendCalback(complete, callbackPayload);
            }
        });


        //复制文本监听
        h5Controller.onEvent("copyToClipboard", new H5Callback<CopyToClipboardPayload>() {
            @Override
            public Map<Object, Object> doPerform(CopyToClipboardPayload payload) {
                Map<Object, Object> callbackPayload = new HashMap<Object, Object>();
                try {
                    ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData myClip = ClipData.newPlainText("text", payload.getText());
                    myClipboard.setPrimaryClip(myClip);
                    callbackPayload.put("status", 1);
                } catch (Exception e) {
                    Log.e(e, "BasicJSEvents copyToClipboard");
                    callbackPayload.put("status", 0);

                }
                return callbackPayload;
            }
        });


        //显示Dialog监听
        h5Controller.onEvent("showDialog", new H5Callback<DialogPayload>() {
            @Override
            public Map<Object, Object> doPerform(DialogPayload payload) {
                return null;
            }
        });

        //跳转内部浏览器监听
        h5Controller.onEvent("goInsideBrowser", new H5Callback<WebPayload>() {
            @Override
            public Map<Object, Object> doPerform(WebPayload payload) {
                WebPayload.WebModel webModel = payload.getWebModel();
                WebPayload.TitleBarModel titleBarModel = webModel.getTitleBarModel();
                WebViewFragment.goPage(h5Controller.getStarter(), webModel.getUrl(), titleBarModel);
                return null;
            }
        });

        //跳转外部浏览器监听
        h5Controller.onEvent("goOutsideBrowser", new H5Callback<WebPayload>() {
            @Override
            public Map<Object, Object> doPerform(WebPayload payload) {
                WebPayload.WebModel webModel = payload.getWebModel();
                Intent i = new Intent();
                i.setAction("android.intent.action.VIEW");
                Uri u = Uri.parse(webModel.getUrl());
                i.setData(u);
                h5Controller.getStarter().startActivity(i);

                return null;
            }
        });

        //跨域请求
        h5Controller.onEventAsync("doHttpRequest", new H5AsyncCallback<RequestPayload>() {
            @Override
            public void doPerform(RequestPayload payload, final JSComplete jsComplete) {

                final Map<Object, Object> callbackPayload = new HashMap<Object, Object>();

                try {
                    if (!NetworkUtils.isNetworkAvailable()) {
                        sendHttpResponse(jsComplete, callbackPayload, 5, "网络不可用,请检查网络配置...", null);
                        return;
                    }
                    RequestPayload.RequestModel requestModel = payload.getRequestModel();
                    if (requestModel != null) {

                        H5Api h5Api = HttpManager.getInstance().getRetrofit(H5Api.class).create(H5Api.class);


                        Map<String, String> headers = new HashMap<String, String>();
                        Map<String, String> params = new HashMap<String, String>();

                        if (StrUtil.isEmpty(requestModel.getUrl())) {
                            sendHttpResponse(jsComplete, callbackPayload, 2, "参数异常,url为空", null);
                        }
                        StringBuilder cacheUrlSB = new StringBuilder(requestModel.getUrl());

                        if (requestModel.getMethod() != null) {
                            boolean isPost = requestModel.getMethod().toLowerCase().equals("post");
                            boolean useJson = requestModel.isUseJson();
                            if (requestModel.getHeaders() != null && requestModel.getHeaders().size() > 0) {
                                for (String keyValue : requestModel.getHeaders()) {
                                    String[] keyValueArr = keyValue.split("=");
                                    headers.put(keyValueArr[0], keyValueArr[1]);
                                }
                            }

                            if (requestModel.getParams() != null && requestModel.getParams().size() > 0) {
                                if (!isPost) {
                                    cacheUrlSB.append("?");
                                }
                                for (String keyValue : requestModel.getParams()) {
                                    String[] keyValueArr = keyValue.split("=");
                                    params.put(keyValueArr[0], keyValueArr[1]);
                                    if (!isPost) {
                                        cacheUrlSB.append(keyValue);
                                        cacheUrlSB.append("&");
                                    }
                                }
                                if (!isPost)
                                    cacheUrlSB.delete(cacheUrlSB.length() - 1, cacheUrlSB.length());
                            }

                            if (payload.isSignature() && !params.isEmpty()) {
                                String origin_signature = SignUtil.generateSign(params, false, false) + Sign.getSign(SignType.TYPE_SHOW);
                                String signature = MD5Util.getMD5String(origin_signature);
                                if (!TextUtils.isEmpty(signature)) {
                                    params.put("sign", signature);
                                    if (!isPost) {
                                        cacheUrlSB.append("&");
                                        cacheUrlSB.append("sign");
                                        cacheUrlSB.append("=");
                                        cacheUrlSB.append(signature);
                                    }
                                }
                            }

                            String cacheUrl = cacheUrlSB.toString();

                            if (payload.isShouldCache()) {
                                CacheManager.getInstance().get(cacheUrl, new CacheManager.CacheGetter<String>() {
                                    @Override
                                    public void onGetCache(String cacheData) {
                                        callbackPayload.put("fromCache", true);
                                        sendHttpResponse(jsComplete, callbackPayload, 0, "获取缓存成功", cacheData);
                                    }

                                    @Override
                                    public void onFailue(Throwable throwable) {
                                    }
                                });
                            }

                            Call<ResponseBody> call = null;

                            if (isPost) {
                                if (useJson) {
                                    String postJson = GsonUtil.getGson().toJson(params);
                                    RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postJson);
                                    call = h5Api.requestPost(requestModel.getUrl(), headers, requestBody);
                                } else {
                                    call = h5Api.requestPost(requestModel.getUrl(), headers, params);
                                }
                            } else {
                                call = h5Api.requestGet(requestModel.getUrl(), headers, params);
                            }
                            if (call != null) {
                                Response<ResponseBody> response = call.execute();
                                if (response.body() != null) {
                                    String json = new String(response.body().bytes());
                                    if (payload.isShouldCache() && !isPost) {
                                        CacheManager.getInstance().save(cacheUrlSB.toString(), json);
                                    }
//                                String json = response.body();
                                    callbackPayload.put("fromCache", false);
                                    sendHttpResponse(jsComplete, callbackPayload, 0, "请求成功", json);
                                } else if (response.errorBody() != null) {
                                    String text = new String(response.errorBody().bytes());
                                    sendHttpResponse(jsComplete, callbackPayload, 3, "数据返回异常", text);
                                } else {
                                    sendHttpResponse(jsComplete, callbackPayload, 3, "数据返回异常", null);
                                }

                            } else {
                                sendHttpResponse(jsComplete, callbackPayload, 2, "参数异常,method 必须为get或者post", null);
                            }
                        } else {
                            sendHttpResponse(jsComplete, callbackPayload, 2, "参数异常,没有method", null);
                        }
                    } else {
                        sendHttpResponse(jsComplete, callbackPayload, 2, "参数异常,没有找到request", null);
                    }
                } catch (Exception e) {
                    Log.e(e, "BasicJSEvents doHttpRequest");
                    sendHttpResponse(jsComplete, callbackPayload, 1, "程序错误," + e.getMessage(), null);
                }
            }

            private void sendHttpResponse(JSComplete jsComplete, Map<Object, Object> callbackPayload, int code, String msg, String json) {
                callbackPayload.put("code", code);
                callbackPayload.put("msg", msg);
                if (!StrUtil.isEmpty(json))
                    callbackPayload.put("data", json);
                sendCalback(jsComplete, callbackPayload);
            }

        });

        //H5让native自己处理触摸事件
        h5Controller.onEvent("setEventPropagation", new H5Callback<Map<Object, Object>>() {
            @Override
            public Map<Object, Object> doPerform(Map<Object, Object> payload) {
                Log.e("TouchPropagation 收到setEventPropagation" + payload.toString());
                boolean propagation = (boolean) payload.get("propagation");
                VRWebView webView = h5Controller.getWebView();
                if (webView != null) {
                    webView.setTouchAble(propagation);
                }
                return null;
            }
        });


        //Toast事件
        h5Controller.onEvent("toast", new H5Callback<Map<Object, Object>>() {
            @Override
            public Map<Object, Object> doPerform(Map<Object, Object> payload) {
                Toast.makeText(AppContextProvider.getInstance().getContext(), (String) payload.get("message"), Toast.LENGTH_SHORT).show();
                return null;
            }
        });

        //跳转到奖品页面
        h5Controller.onEvent("goGiftPage", new H5Callback() {
            @Override
            public Map<Object, Object> doPerform(Object payload) {
//                GiftFragment.goPage(h5Controller.getStarter());
                return null;
            }
        });

        //注册网络状态监听事件
        h5Controller.onEvent("registerNetworkStatusChange", new H5Callback() {
            @Override
            public Map<Object, Object> doPerform(Object payload) {
                networkRegister.regist(h5Controller);
                return null;
            }
        });

        //刷新用户Token
        h5Controller.onEvent("refreshToken", new H5AsyncCallback<Object>() {
            @Override
            public void doPerform(Object payload, JSComplete jsComplete) {
                sendTokenCallback(jsComplete);
            }
        });
        return true;
    }

    /**
     * 刷新token
     *
     * @param jsComplete
     */
    public void sendTokenCallback(final JSComplete jsComplete) {
        final Map<Object, Object> payload = new HashMap<Object, Object>();
        Router.getInstance().buildExecutor("/user/service/refreshtokenservice").callback(new Executor.Callback<AccessTokenBean>() {

            @Override
            public void onCall(AccessTokenBean accessTokenBean) {
                payload.put("status", 1);
                payload.put("accessToken", accessTokenBean);
                sendCalback(jsComplete, payload);
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                payload.put("status", 0);
                sendCalback(jsComplete, payload);
            }
        }).excute();

    }

    /**
     * 统一发送callback
     *
     * @param jsComplete
     * @param payload
     */
    private void sendCalback(final JSComplete jsComplete, final Map<Object, Object> payload) {
        h5Controller.sendCalback(jsComplete, payload);
    }


    /**
     * 发送登录事件回调
     */
    public void sendLoginCallback(JSComplete jsComplete) {
        final Map<Object, Object> payload = new HashMap<Object, Object>();
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                payload.put("status", 1);
                payload.put("user", userModel);
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                payload.put("status", 0);
            }
        }).excute();
        sendCalback(jsComplete, payload);
    }

    /**
     * 发送扫描二维码事件回调
     */
    public void sendScanCallback(JSComplete jsComplete, int status, String code) {
        Map<Object, Object> payload = new HashMap<Object, Object>();
        if (status == 1) {
            payload.put("code", code);
        }
        payload.put("status", status);
        sendCalback(jsComplete, payload);
    }

    public void sendIsLoginCallback(JSComplete jsComplete, UserModel userModel) {
        Map<Object, Object> payload = new HashMap<Object, Object>();
        if (userModel != null) {
            payload.put("status", 1);
            payload.put("user", userModel);
        } else {
            payload.put("status", 0);
        }
        sendCalback(jsComplete, payload);
    }


    /**
     * 发送页面触发返回按钮事件
     */
    public void sendOnBackpress() {
        h5Controller.sendEvent("captureExit", new OnCompletedListener() {
            @Override
            public void onCompleted(Map<Object, Object> payload) {

            }
        });
    }


    /**
     * 发送当键盘收起的事件
     */
    public void sendKeyBoardHide() {
        h5Controller.sendEvent("keyboardHide", new OnCompletedListener() {
            @Override
            public void onCompleted(Map<Object, Object> payload) {

            }
        });
    }

    public void sendKeyBoardShow(int height) {
        Map<Object, Object> payload = new HashMap<>();
        payload.put("height", height);
        h5Controller.sendEvent("keyboardShow", payload);
    }

    /**
     * 发送用户信息
     */
    public void sendUserInfo() {
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                Map<Object, Object> payload = new HashMap<Object, Object>();
                payload.put("user", userModel);
                h5Controller.sendEvent("userInfo", payload, new OnCompletedListener() {
                    @Override
                    public void onCompleted(Map<Object, Object> payload) {
                        Toast.makeText(AppContextProvider.getInstance().getContext(), "收到send Callback", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                //
            }
        }).excute();
    }


    public void sendResume() {
        h5Controller.sendEvent("resume");
    }

    public void sendPause() {
        h5Controller.sendEvent("pause");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareUtil.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (data != null) {
                    String callbackId = data.getStringExtra("str_callbackId");
                    JSComplete jsComplete = jsCompleteMap.get(callbackId);
                    sendLoginCallback(jsComplete);
                    jsCompleteMap.remove(callbackId);
                }
                break;
            case REQUEST_CODE_SCAN:
                if (data != null) {
                    String callbackId = data.getStringExtra("str_callbackId");
                    int status = data.getIntExtra("result_type", 0);
                    String code = data.getStringExtra("result_string");
                    JSComplete jsComplete = jsCompleteMap.get(callbackId);
                    sendScanCallback(jsComplete, status, code);
                    jsCompleteMap.remove(callbackId);
                }
        }
    }

    public Activity getActivity() {
        Activity activity = null;

        if (h5Controller.getStarter() != null && h5Controller.getStarter().getAttatchContext() != null) {
            Context context = h5Controller.getStarter().getAttatchContext();
            if (context instanceof Activity) {
                activity = ((Activity) context);
            } else if (context instanceof ContextWrapper) {
                Context context2 = ((ContextWrapper) context).getBaseContext();
                if (context2 instanceof Activity) {
                    activity = ((Activity) context2);
                }
            }
        }
        return activity;
    }


    public void destroy() {
        networkRegister.unRegist();
        jsCompleteMap.clear();
    }


    public static class NetworkRegister {
        int lastNetworkState = NetworkUtils.getNetworkState();
        private H5Controller h5Controller;


        public void regist(H5Controller h5Controller) {
            this.h5Controller = h5Controller;
            sendNetWorkStatusChanged(NetworkUtils.getNetworkState());
            EventController.regist(this);
            NetworkUtils.registNetWorkChanged();
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onEvent(NetworkUtils.NetworkChangedEvent event) {
            int state = event.getCurrntNetWorkState();
            if (lastNetworkState != state) {
                sendNetWorkStatusChanged(state);
                lastNetworkState = state;
            }
        }

        public void unRegist() {
            NetworkUtils.unRegistNetWorkChanged();
            EventController.unRegist(this);
            h5Controller = null;
        }

        /**
         * 发送网络状态改变
         *
         * @param networkState
         */
        public void sendNetWorkStatusChanged(int networkState) {
            Map<Object, Object> payload = new HashMap<>();
            payload.put("networkState", networkState);
            if (h5Controller != null)
                h5Controller.sendEvent("networkStatusChange", payload);
        }
    }
}
