package com.whaleyvr.biz.hybrid.mvp.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.tencent.smtt.sdk.WebViewClient;
import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.widget.titlebar.ITitleBar;
import com.whaleyvr.biz.hybrid.BasicJSEvents;
import com.whaleyvr.biz.hybrid.CachePolicy;
import com.whaleyvr.biz.hybrid.H5Callback;
import com.whaleyvr.biz.hybrid.H5Controller;
import com.whaleyvr.biz.hybrid.R;
import com.whaleyvr.biz.hybrid.mvp.view.WebView;
import com.whaleyvr.core.network.http.HttpManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Js Hybrid所用Presenter
 * Created by YangZhi on 2016/10/31 19:44.
 */
public abstract class JSBridgePresenter<MVPVIEW extends WebView> extends WebViewPresenter<MVPVIEW> {


    /**
     * H5 Hybrid消息控制器
     */
    private H5Controller h5Controller;

    /**
     * 基础JS事件
     */
    private BasicJSEvents basicJSEvents;

    /**
     * 是否为内部网址
     */
    protected boolean isForeignUrl = true;

    private boolean isLoadingPage;

    public JSBridgePresenter(MVPVIEW view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        h5Controller = null;
    }

    public H5Controller getH5Controller() {
        if (h5Controller == null)
            h5Controller = findH5Controller();
        return h5Controller;
    }

    public BasicJSEvents getBasicJSEvents() {
        return basicJSEvents;
    }

    private H5Controller findH5Controller() {
        if (getUIView() != null && getUIView().getWebView() != null)
            return getUIView().getWebView().getH5Controller();
        return null;
    }

    public void onWebViewCreated() {
        if (getH5Controller() != null) {
            h5Controller.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String s) {
                    super.onPageFinished(webView, s);
                    JSBridgePresenter.this.onPageFinished(s);
                }

                @Override
                public void onPageStarted(com.tencent.smtt.sdk.WebView webView, String s, Bitmap bitmap) {
                    super.onPageStarted(webView, s, bitmap);
                    JSBridgePresenter.this.onPageStarted(s);
                }

            });
            h5Controller.setCachePolicy(CachePolicy.create(getWebViewRepository().getCachePolice()));
            basicJSEvents = BasicJSEvents.getInstance(h5Controller);
        }
        registEvents();
        super.onWebViewCreated();
    }

    public void registEvents() {
        if (h5Controller != null) {
            h5Controller.onEvent("loadingPage", new H5Callback() {
                @Override
                public Map<Object, Object> doPerform(Object payload) {
                    isLoadingPage = true;
                    onLoadingPage();
                    return null;
                }
            });

            h5Controller.onEvent("completeLoadingPage", new H5Callback<Object>() {
                @Override
                public Map<Object, Object> doPerform(Object payload) {
                    onCompleteLoadingPage();
                    return null;
                }
            });

            h5Controller.onEvent("showTitleShareButton", new H5Callback<ShareParam>() {
                @Override
                public Map<Object, Object> doPerform(ShareParam payload) {
                    ShareModel shareModel = ShareModel.createBuilder().setUrl(payload.getUrl())
                            .setTitle(payload.getTitle())
                            .setDes(payload.getDes())
                            .setType(ShareConstants.TYPE_ALL)
                            .setShareType(payload.getShareType() > 0 ? payload.getShareType() : ShareTypeConstants.TYPE_SHARE_NEWS)
                            .build();
                    if (payload.getImageModel() != null) {
                        shareModel.setImgUrl(payload.getImageModel().getImgUrl());
                    }
                    Router.getInstance().buildExecutor("/share/service/sharemodel").putObjParam(shareModel).notTransParam()
                            .callback(new Executor.Callback<ShareParam.Builder>() {
                                @Override
                                public void onCall(ShareParam.Builder builder) {
                                    getWebViewRepository().setBuilder(builder);
                                }

                                @Override
                                public void onFail(Executor.ExecutionError executionError) {

                                }
                            }).notTransCallbackData().excute();
                    updateTitleShare();
                    ITitleBar titleBar = getUIView().getTitleBar();
                    if (titleBar == null) {
                        return null;
                    }
                    titleBar.setRightIcon(R.drawable.bg_btn_share_ttle_selector);
                    return null;
                }

            });
        }
    }

    @Override
    public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int newProgress) {
//        super.onProgressChanged(view, newProgress);
    }

    protected void onPageFinished(String url) {
        onProgressIsCompleted();
    }

    protected void onPageStarted(String url) {

    }


    /**
     * 当监听到有loadingPage,则代表该网址为内部网址
     */
    protected void onLoadingPage() {
        Log.d(TAG, "onLoadingPage");
        isForeignUrl = false;
        if (getUIView() != null)
            getUIView().skipLongClick();
    }


    public boolean isLoadingPage() {
        return isLoadingPage;
    }

    /**
     * 是外部网址的时候才根据progress进度监听网页加载进度
     */
    @Override
    protected void onProgressIsCompleted() {
        Log.d(TAG, "onProgressIsCompleted");
        if (isForeignUrl) {
            super.onProgressIsCompleted();
        }
    }

    @Override
    protected void onCompleteLoadingPage() {
        Log.d(TAG, "onCompleteLoadingPage");
        super.onCompleteLoadingPage();
    }

    @Override
    public void onHideKeyboard() {
        super.onHideKeyboard();
        if (basicJSEvents != null)
            basicJSEvents.sendKeyBoardHide();
    }

    @Override
    public void onShowKeyboard(int keyboardHeight) {
        super.onShowKeyboard(keyboardHeight);
        if (basicJSEvents != null)
            basicJSEvents.sendKeyBoardShow(keyboardHeight);
    }

    @Override
    public boolean onBackPressed() {
        //只有当JS加载完成才用JS中的返回控制
        if (h5Controller != null && h5Controller.isLoaded()) {
            if (basicJSEvents != null) {
                basicJSEvents.sendOnBackpress();
                return true;
            }
        }
        return super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (basicJSEvents != null) {
            basicJSEvents.sendResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (basicJSEvents != null) {
            basicJSEvents.sendPause();
        }
    }

    @Override
    protected String getAppendUrl() {
        Map<String, String> apiUrlMap = new HashMap<>();
        apiUrlMap.put("STORE", formatHttpStr(HttpManager.getInstance().checkTest(BaseUrls.STORE_API)));
        apiUrlMap.put("WHALEY_ACCOUNT", formatHttpStr(HttpManager.getInstance().checkTest(BaseUrls.WHALEY_ACCOUNT)));
        apiUrlMap.put("VR_API_AGINOMOTO", formatHttpStr(HttpManager.getInstance().checkTest(BaseUrls.VR_API_AGINOMOTO)));
        apiUrlMap.put("BUS_AGINOMOTO", formatHttpStr(HttpManager.getInstance().checkTest(BaseUrls.BUS_AGINOMOTO_API)));
        apiUrlMap.put("BI", formatHttpStr(HttpManager.getInstance().checkTest(BaseUrls.BI_API)));
        String apiUrlsJson = GsonUtil.getGson().toJson(apiUrlMap, new TypeToken<Map<String, String>>() {
        }.getType());
        String encodeStr = "";
        try {
            encodeStr = URLEncoder.encode(apiUrlsJson, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(e, TAG + "getAppendUrl");
        }
        String appendUrl = "statusBarHeight=" + DisplayUtil.getStatusHeight(AppContextProvider.getInstance().getContext()) + "&api=" + encodeStr;
        return appendUrl;
    }


    private String formatHttpStr(String url) {
        return url;
    }
}
