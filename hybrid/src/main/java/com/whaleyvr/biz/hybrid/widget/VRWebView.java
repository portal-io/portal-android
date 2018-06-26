package com.whaleyvr.biz.hybrid.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.whaley.biz.common.CommonConstants;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.PermissionUtil;
import com.whaley.core.utils.StrUtil;
import com.whaley.core.widget.refresh.RefreshLayout;
import com.whaleyvr.biz.hybrid.BuildConfig;
import com.whaleyvr.biz.hybrid.DefaultWebViewClient;
import com.whaleyvr.biz.hybrid.H5Controller;
import com.whaleyvr.biz.hybrid.JsBridgeWebSetter;
import com.whaleyvr.biz.hybrid.WebSetter;

/**
 * 通用WebView
 * Created by YangZhi on 2016/10/11 17:34.
 */
public class VRWebView extends WebView implements WebSetter {


    public static final String WEBVIEW_URL_HTTP = "http://";
    public static final String WEBVIEW_URL_HTTPS = "https://";
    public static final String WEBVIEW_URL_FILE = "file://";
    public static final String WEBVIEW_URL_ASSETS = "assets://";


    EmptyDisplayView emptyLayout;

    private WebViewClient client;

    private String initUrl;


    private WebSetter customWebSetter;

    private OnScrollChangedCallback mOnScrollChangedCallback;


    private Starter starter= AppContextProvider.getInstance().getStarter();


    boolean isTouchAble;

    View.OnTouchListener onTouchListener;


    private boolean isClampedY = false;

    public VRWebView(Context context) {
        super(context);
        initWebView();
    }

    public VRWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    public VRWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView();
    }



    private void initWebView(){
        this.setWebViewClientExtension(new X5WebViewEventHandler(this));
        JsBridgeWebSetter jsBridgeWebSetter=new JsBridgeWebSetter();


        jsBridgeWebSetter.attachStater(new Starter() {
            @Override
            public void startActivityForResult(Intent intent, int requestCode) {
                if(starter!=null){
                    starter.startActivityForResult(intent,requestCode);
                }
            }

            @Override
            public void startActivity(Intent intent) {
                if(starter!=null){
                    starter.startActivity(intent);
                }
            }

            @Override
            public Context getAttatchContext() {
                if(starter!=null){
                   return starter.getAttatchContext();
                }
                return null;
            }

            @Override
            public void transitionAnim(int enterAnim, int outAnim) {
                if(starter!=null){
                    starter.transitionAnim(enterAnim,outAnim);
                }
            }

            @Override
            public void finish() {
                if(starter!=null)
                    starter.finish();
            }
        });
        setCustomWebSetter(jsBridgeWebSetter);
        getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret=false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(onTouchListener!=null)
                            onTouchListener.onTouch(v,event);
                        break;
                }
                if(isTouchAble){
                    if(onTouchListener!=null)
                        onTouchListener.onTouch(v,event);
                }else {
                }
                return ret;
            }
        });
    }


    @Override
    public void onSetWebView(VRWebView webView) {

        this.setWebViewClient(new DefaultWebViewClient(emptyLayout));
        setEmptyLayout(emptyLayout);
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                super.onReceivedTouchIconUrl(view, url, precomposed);
                Log.i("onReceivedTouchIconUrl url=" + url + ",precomposed=" + precomposed);
            }


            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i("onConsoleMessage message=" + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.i("onProgressChanged progress=" + newProgress);
            }
        });
    }


    @Override
    public H5Controller getH5Controller() {
        if(customWebSetter!=null)
            return customWebSetter.getH5Controller();
        return null;
    }

    @Override
    public VRWebView getWebView() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(customWebSetter!=null){
            customWebSetter.onActivityResult(requestCode,resultCode,data);
        }
    }

    public Activity getActivity(){
        Activity activity=null;
        if(getContext()!=null){
            Context context=getContext();
            if (context instanceof Activity) {
                activity=((Activity) context);
            } else if (context instanceof ContextWrapper) {
                Context context2 = ((ContextWrapper) context).getBaseContext();
                if (context2 instanceof Activity) {
                    activity=((Activity) context2);
                }
            }
        }
        return activity;
    }

    protected void setWebViewSetting(){
        this.getSettings().setJavaScriptEnabled(true);
//        webSetting.setGeolocationEnabled(true);
//        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
//        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webSetting.setBuiltInZoomControls(true);
//        webSetting.setSupportMultipleWindows(false);
//        webSetting.setSupportZoom(true);
        if(customWebSetter ==null) {
            onSetWebView(this);
        }else {
            customWebSetter.onSetWebView(this);
        }
    }

    public void loadInitUrl(String url){
        if(initUrl==null) {
            if (StrUtil.isEmpty(url)) {
                if (emptyLayout != null) {
                    emptyLayout.showError(new Throwable("地址为空..."));
                }
                return;
            }
            if (url.startsWith(WEBVIEW_URL_HTTP) || url.startsWith(WEBVIEW_URL_HTTPS) || url.startsWith(WEBVIEW_URL_FILE)||url.startsWith(WEBVIEW_URL_ASSETS)) {
                initUrl = url;

            } else {
                initUrl = WEBVIEW_URL_HTTP + url;
            }
            if (emptyLayout != null) {
                emptyLayout.showLoading("正在载入中...");
            }
        }
        if(getH5Controller()!=null)
            getH5Controller().loadUrl(initUrl);
    }



    public void setStarter(Starter starter) {
        this.starter = starter;
    }


    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(client);
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(client);
        this.client=client;
    }

    public void setEmptyLayout(EmptyDisplayView emptyLayout) {
        this.emptyLayout = emptyLayout;
        if(client!=null&&client instanceof DefaultWebViewClient){
            DefaultWebViewClient webViewClient=(DefaultWebViewClient) client;
            webViewClient.setEmptyLayout(emptyLayout);
        }
    }

    public EmptyDisplayView getEmptyLayout() {
        return emptyLayout;
    }


    /**
     * 设置自定义的WebSetter
     * @param customWebSetter
     */
    public void setCustomWebSetter(WebSetter customWebSetter) {
        if(this.customWebSetter!=null){
            this.customWebSetter.destroy();
        }
        this.customWebSetter = customWebSetter;
        if(this.customWebSetter!=null)
            setWebViewSetting();
    }


    public WebSetter getCustomWebSetter() {
        return customWebSetter;
    }


    public RefreshLayout getRefreshLayout(){
        ViewGroup viewGroup=(ViewGroup) getParent();
        if(viewGroup!=null&&viewGroup instanceof RefreshLayout){
            return (RefreshLayout) viewGroup;
        }
        return null;
    }

    protected void tbs_onScrollChanged(int l, int t, int oldl, int oldt, View view) {
        super_onScrollChanged(l, t, oldl, oldt);
    }

    protected void tbs_onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY, View view) {
        super_onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    protected void tbs_computeScroll(View view) {
        super_computeScroll();
    }

    protected boolean tbs_overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent, View view) {
        return super_overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX,
                maxOverScrollY, isTouchEvent);
    }


    // TBS: Do not use @Override to avoid false calls
    public boolean tbs_dispatchTouchEvent(MotionEvent ev, View view) {
        boolean r = super.super_dispatchTouchEvent(ev);
        android.util.Log.d("Bran", "dispatchTouchEvent " + ev.getAction() + " " + r);
        return r;
    }

    // TBS: Do not use @Override to avoid false calls
    public boolean tbs_onInterceptTouchEvent(MotionEvent ev, View view) {
        boolean r = super.super_onInterceptTouchEvent(ev);
        return r;
    }

    protected boolean tbs_onTouchEvent(MotionEvent event, View view) {
        return super_onTouchEvent(event);
    }



    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret = super.drawChild(canvas, child, drawingTime);
        if(Debug.isDebug()) {
            canvas.save();
            Paint paint = new Paint();
            paint.setColor(0x7fff0000);
            paint.setTextSize(24.f);
            paint.setAntiAlias(true);
            if (getX5WebViewExtension() != null) {
                canvas.drawText(this.getContext().getPackageName() + "-pid:" + android.os.Process.myPid(), 10, 50, paint);
                canvas.drawText("X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10, 100, paint);
            } else {
                canvas.drawText(this.getContext().getPackageName() + "-pid:" + android.os.Process.myPid(), 10, 50, paint);
                canvas.drawText("Sys Core", 10, 100, paint);
            }
            canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
            canvas.drawText(Build.MODEL, 10, 200, paint);
            canvas.restore();
        }
        return ret;
    }


    public void setTouchAble(boolean isTouchAble){
        this.isTouchAble=isTouchAble;
    }



    public void setOntouchListener(View.OnTouchListener ontouchListener){
        this.onTouchListener=ontouchListener;
    }

    /**
     * 页面销毁时 必须调用
     */
    @Override
    public void destroy(){
        dettatch();
        super.destroy();
    }


    public void dettatch(){
        Log.e("WebView dettatch begin");
        setOnScrollChangedCallback(null);
        setCustomWebSetter(null);
        if (getParent() != null)
            ((ViewGroup) getParent()).removeView(this);
        clearHistory();
        removeAllViews();
        try {
            stopLoading();
        }catch (Exception e) {
            Log.e(e,"VRWebView dettatch");
        }
        if(getView()!=null) {
            getView().setOnClickListener(null);
            getView().setOnTouchListener(null);
            getView().setOnLongClickListener(null);
        }
        setWebViewClient(null);
        setWebChromeClient(null);
        if(getX5WebViewExtension()!=null) {
            getX5WebViewExtension().setWebViewClientExtension(null);
            getX5WebViewExtension().setWebChromeClientExtension(null);
        }
        getSettings().setJavaScriptEnabled(false);
        Log.e("WebView Destoryed");
    }




    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }


    public static interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
    }
}
