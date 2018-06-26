package com.whaleyvr.biz.hybrid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.whaley.biz.common.CommonConstants;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.biz.hybrid.jsbridge.JsBridge;
import com.whaleyvr.biz.hybrid.jsbridge.JsBridgeAsyncHandler;
import com.whaleyvr.biz.hybrid.jsbridge.JsBridgeHandler;
import com.whaleyvr.biz.hybrid.jsbridge.JsBridgeImpl;
import com.whaleyvr.biz.hybrid.jsbridge.JsCallback;
import com.whaleyvr.biz.hybrid.model.CallbackPayload;
import com.whaleyvr.biz.hybrid.widget.VRWebView;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Map;

/**
 *
 * JsBridge的相关设置器
 * Created by YangZhi on 2016/10/11 18:14.
 */
public class JsBridgeWebSetter implements WebSetter,H5Controller{

    public static String[] HOSTS=new String[]{
//            "http://minisite.test.snailvr.com/",
//            "http://172.29.3.33:8888/"
    };



    private JsBridge jsBridge;

    private VRWebView webView;

    private Starter starter;


    private WebViewClient webViewClient;


    BasicJSEvents basicJSEvents;


    private CachePolicy cachePolicy=CachePolicy.create(CachePolicy.CACHE_POLICY_NONE);


    private HybridCacheManager cacheManager;


    public JsBridgeWebSetter(){
    }


    public void destroy(){
        if(cacheManager!=null)
            cacheManager.destroy();
        jsBridge.setWebViewClient(null);
        jsBridge.destory();
        basicJSEvents.destroy();
        dettachStater();
        webView=null;

    }

    public void attachStater(Starter starter) {
        this.starter = starter;
    }

    public void dettachStater(){
        this.starter=null;
    }


    @Override
    public Starter getStarter() {
        return starter;
    }

    @Override
    public void onSetWebView(VRWebView webView) {
        this.webView=webView;
        String deviceId = AppUtil.getDeviceId();
        WebSettings webSetting=webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setUserAgentString(webView.getSettings().getUserAgentString()
                + " APP_PLATFORM/Android APP_VERSION_CODE/"+ CommonConstants.VALUE_APP_VERSION_NAME
                +" APP_DEVICE_ID/"+deviceId
                +" APP_NAME/WhaleyVR APP_VERSION/"+ CommonConstants.VALUE_APP_VERSION_NAME
                +" SYSTEM_VERSION/"+ android.os.Build.VERSION.RELEASE
                +" whaleyvrapp");
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setUseWideViewPort(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setSupportMultipleWindows(true);
        cacheManager=HybridCacheManager.create(webView,cachePolicy);
        jsBridge = JsBridgeImpl.getDefault();

        jsBridge.configure(webView);

        jsBridge.setWebViewClient(new DefaultWebViewClient(webView.getEmptyLayout()){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(URLDecoder.decode(url));
                try {
                    URI uri = new URI(url);
                    if(!StrUtil.isEmpty(uri.getScheme())&&uri.getScheme().equals("whaleyvr")){
                        Intent intent=new Intent();
                        intent.setData(Uri.parse(url));
                        getStarter().startActivity(intent);
                        return true;
                    }
                } catch (URISyntaxException e) {
                }

                if(webViewClient!=null&&webViewClient.shouldOverrideUrlLoading(view,url)){
                    return true;
                }
                return false;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if(hasDelegate()){
                    delegate().onLoadResource(view,url);
                }else {
                    super.onLoadResource(view, url);
                }
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view,request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest, Bundle bundle) {
                return shouldInterceptRequest(webView,webResourceRequest.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if(hasDelegate()){
                    WebResourceResponse resourceResponse=delegate().shouldInterceptRequest(view,url);
                    if(resourceResponse!=null)
                        return resourceResponse;
                }
                if (cacheManager!=null)
                    return cacheManager.onInterceptRequest(url);
                return null;
            }

            @Override
            protected WebViewClient delegate() {
                return webViewClient;
            }
        });


        basicJSEvents=BasicJSEvents.getInstance(this);
        basicJSEvents.registEvents();
    }


    @Override
    public void loadUrl(String url) {
        if(cacheManager!=null){
            cacheManager.loadUrl(url);
        }else if(webView!=null){
            webView.loadUrl(url);
        }
    }

    @Override
    public void setCachePolicy(CachePolicy cachePolicy) {
        this.cachePolicy = cachePolicy;

        cacheManager.setCachePolicy(cachePolicy);
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        this.webViewClient=client;
    }

    public JsBridge getJsBridge() {
        return jsBridge;
    }

    @Override
    public VRWebView getWebView() {
        return webView;
    }

    @Override
    public H5Controller getH5Controller() {
        return this;
    }

    @Override
    public void sendEvent(String type) {
        if(getWebView()!=null)
            getJsBridge().send(type,getWebView());
    }

    @Override
    public void sendEvent(String type, Object data) {
        if(getWebView()!=null)
            getJsBridge().send(type,getWebView(),data);
    }

    @Override
    public void sendEvent(String type, final OnCompletedListener completedListener) {
        if(getWebView()!=null)
        getJsBridge().send(type, getWebView(), new JsCallback() {
            @Override
            public void call(Map<Object,Object> payload) {
                completedListener.onCompleted(payload);
            }
        });
    }

    @Override
    public void sendEvent(String type, Object data, final OnCompletedListener completedListener) {
        if(getWebView()!=null)
        getJsBridge().send(type, getWebView(), data, new JsCallback() {
            @Override
            public void call(Map<Object,Object> payload) {
                completedListener.onCompleted(payload);
            }
        });
    }

    @Override
    public void onEvent(String type, H5Callback... callbacks) {
        JsBridgeHandler[] jsBridgeHandlers =new JsBridgeHandler[callbacks.length];
        int i=0;
        for (final H5Callback callback:callbacks){
            jsBridgeHandlers[i]=new JsBridgeHandler() {
                @Override
                protected Map<Object,Object> doPerform(Map<Object, Object> payload) {
                    return JsBridgeWebSetter.this.doPerform(callback,payload);
                }
            };
            i++;
        }
        getJsBridge().on(type, jsBridgeHandlers);
    }

    @Override
    public void onEventAsync(String type, H5Callback... callbacks) {
        JsBridgeHandler[] jsBridgeHandlers =new JsBridgeHandler[callbacks.length];
        int i=0;
        for (final H5Callback callback:callbacks){
            jsBridgeHandlers[i]=new JsBridgeAsyncHandler() {
                @Override
                protected Map<Object,Object> doPerform(Map<Object, Object> payload) {
                    return JsBridgeWebSetter.this.doPerform(callback,payload);
                }
            };
            i++;
        }
        getJsBridge().on(type, jsBridgeHandlers);
    }

    private Map<Object,Object> doPerform(H5Callback callback,Map<Object, Object> payload){
        try {
            Object data=getPayloadFromMap(callback,payload);
            if(data!=null&&data instanceof CallbackPayload){
                CallbackPayload callbackPayload=(CallbackPayload) data;
                if(callback instanceof H5AsyncCallback){
                    H5AsyncCallback asyncCallback=(H5AsyncCallback) callback;
                    asyncCallback.setJsComplete(new JSComplete(getH5Controller(),callbackPayload));
                    return asyncCallback.doPerform(callbackPayload);
                }
            }
            return callback.doPerform(data);
        }catch (Exception e){
            Log.e(e,"JsBridgeWebSetter doPerform");
        }
        return null;
    }





    /**
     * 将Map 对象化  获取对象模型
     * @param callback
     * @param payload
     * @return
     */
    private Object getPayloadFromMap(H5Callback callback,Map<Object, Object> payload){
        Object object=null;
        try {
            String json= GsonUtil.getGson().toJson(payload);

            Type type;
            if(callback instanceof H5AsyncCallback) {
                type=getAsyncH5CallbackType(callback.getClass());
            }else {
                type=getType(callback.getClass());
            }
            if(type!=null)
                object=GsonUtil.getGson().fromJson(json,type);
        }catch (Exception e){
            Log.e(e,"JsBridgeWebSetter getPayloadFromMap");
        }
        return object;
    }





    private Type getAsyncH5CallbackType(Class clazz) {
        Type[] types = clazz.getSuperclass().getGenericInterfaces();
        if (types[0] instanceof ParameterizedType) {
            Type actualType1 = ((ParameterizedType) types[0]).getActualTypeArguments()[0];
            return actualType1;
        }
        return null;
    }

    private Type getType(Class clazz) {
        Type[] types = clazz.getGenericInterfaces();
        if (types[0] instanceof ParameterizedType) {
            Type actualType1 = ((ParameterizedType) types[0]).getActualTypeArguments()[0];
            return actualType1;
        }
        return null;
    }

    @Override
    public void offEvent(String type) {
        getJsBridge().off(type);
    }


    @Override
    public boolean isLoaded() {
        if(basicJSEvents!=null)
            return basicJSEvents.isLoaded();
        return false;
    }







    /**
     * 统一发送callback
     * @param jsComplete
     * @param payload
     */
    @Override
    public void sendCalback(final JSComplete jsComplete,final Map<Object,Object> payload){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if(jsComplete != null) {
                    jsComplete.sendCallback(payload);
                }
            }
        };
        if(webView!=null){
            webView.post(runnable);
        }

    }


    @Override
    public void showEmpty() {
        if(webView!=null&&webView.getEmptyLayout()!=null){
            webView.getEmptyLayout().showEmpty();
        }
    }

    @Override
    public void showLoading(String loadtext) {
        if(webView!=null&&webView.getEmptyLayout()!=null){
            webView.getEmptyLayout().showLoading(loadtext);
        }
    }

    @Override
    public void showError(Throwable error) {
        if(webView!=null&&webView.getEmptyLayout()!=null){
            webView.getEmptyLayout().showError(error);
        }
    }


    @Override
    public void setOnRetryListener(EmptyDisplayView.OnRetryListener listener) {
        if(webView!=null&&webView.getEmptyLayout()!=null){
            webView.getEmptyLayout().setOnRetryListener(listener);
        }
    }

    @Override
    public void showContent() {
        if(webView!=null&&webView.getEmptyLayout()!=null){
            webView.getEmptyLayout().showContent();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(basicJSEvents!=null)
            basicJSEvents.onActivityResult(requestCode,resultCode,data);
    }
}
