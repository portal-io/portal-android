package com.whaleyvr.biz.hybrid;

import android.graphics.Bitmap;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaleyvr.biz.hybrid.jsbridge.util.ForwardingWebViewClient;

/**
 * Created by YangZhi on 2016/10/8 16:47.
 */
public class DefaultWebViewClient extends ForwardingWebViewClient {


    static final String URL_H5_HOST_1="http://store-image.snailvr.com";

    static final String URL_H5_HOST_2="http://minisite.snailvr.com";

    private EmptyDisplayView emptyLayout;

    @Override
    protected WebViewClient delegate() {
        return null;
    }

    public DefaultWebViewClient(EmptyDisplayView emptyLayout){
        this.emptyLayout=emptyLayout;
    }


    public DefaultWebViewClient(){
        this(null);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i(url);
        view.loadUrl(url);
        return false;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Log.i(url);
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }


    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (emptyLayout != null)
            emptyLayout.showError(new Throwable(description));
//                if (webview != null && webview.isAttachedToWindow()) {
//                    webview.setVisibility(View.GONE);
//                }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }


    public void setEmptyLayout(EmptyDisplayView emptyLayout) {
        this.emptyLayout = emptyLayout;
    }
}
