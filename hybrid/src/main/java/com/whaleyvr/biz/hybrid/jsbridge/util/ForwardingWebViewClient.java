package com.whaleyvr.biz.hybrid.jsbridge.util;

import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.whaleyvr.biz.hybrid.BuildConfig;

public abstract class ForwardingWebViewClient extends com.tencent.smtt.sdk.WebViewClient {

	static final String TAG="ForwardingWebViewClient";

	protected abstract com.tencent.smtt.sdk.WebViewClient delegate();

	protected boolean hasDelegate() {
		return delegate() != null;
	}

	private static final boolean DEBUG= BuildConfig.DEBUG;

	private void logger(String msg){
		if(DEBUG)
			Log.e(TAG,msg);
	}


	@Override
	public void onTooManyRedirects(com.tencent.smtt.sdk.WebView view, Message cancelMsg, Message continueMsg) {
		super.onTooManyRedirects(view, cancelMsg, continueMsg);
		logger("onTooManyRedirects");
	}


	@Override
	public void doUpdateVisitedHistory(com.tencent.smtt.sdk.WebView view, String url,
			boolean isReload) {
		logger("doUpdateVisitedHistory");
		if (hasDelegate())
			delegate().doUpdateVisitedHistory(view, url, isReload);
		else
			super.doUpdateVisitedHistory(view, url, isReload);
	}

	@Override
	public void onFormResubmission(com.tencent.smtt.sdk.WebView view, Message dontResend,
			Message resend) {
		logger("onFormResubmission");
		if (hasDelegate())
			delegate().onFormResubmission(view, dontResend, resend);
		else
			super.onFormResubmission(view, dontResend, resend);
	}

	@Override
	public void onLoadResource(com.tencent.smtt.sdk.WebView view, String url) {
		logger("onLoadResource");
		if (hasDelegate())
			delegate().onLoadResource(view, url);
		else
			super.onLoadResource(view, url);


	}

	@Override
	public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
		logger("onPageFinished");
		if (hasDelegate())
			delegate().onPageFinished(view, url);
		else
			super.onPageFinished(view, url);
	}

	@Override
	public void onPageStarted(com.tencent.smtt.sdk.WebView view, String url, Bitmap favicon) {
		logger("onPageStarted");
		if (hasDelegate())
			delegate().onPageStarted(view, url, favicon);
		else
			super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onReceivedError(com.tencent.smtt.sdk.WebView view, int errorCode,
			String description, String failingUrl) {
		logger("onReceivedError");
		if (hasDelegate())
			delegate()
					.onReceivedError(view, errorCode, description, failingUrl);
		else
			super.onReceivedError(view, errorCode, description, failingUrl);
	}

	@Override
	public void onReceivedHttpAuthRequest(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.HttpAuthHandler httpAuthHandler, String host, String realm) {
		if (hasDelegate())
			delegate().onReceivedHttpAuthRequest(webView, httpAuthHandler, host, realm);
		else
			super.onReceivedHttpAuthRequest(webView, httpAuthHandler, host, realm);
	}


	@Override
	public void onReceivedLoginRequest(com.tencent.smtt.sdk.WebView  view, String realm,
			String account, String args) {
		logger("onReceivedLoginRequest");
		if (hasDelegate())
			delegate().onReceivedLoginRequest(view, realm, account, args);
		else
			super.onReceivedLoginRequest(view, realm, account, args);
	}

	@Override
	public void onReceivedSslError(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
		logger("onReceivedSslError");
		if (hasDelegate())
			delegate().onReceivedSslError(webView, sslErrorHandler, sslError);
		else
			super.onReceivedSslError(webView, sslErrorHandler, sslError);

	}


	@Override
	public void onUnhandledKeyEvent(com.tencent.smtt.sdk.WebView view, KeyEvent event) {
		logger("onUnhandledKeyEvent");
		if (hasDelegate())
			delegate().onUnhandledKeyEvent(view, event);
		else
			super.onUnhandledKeyEvent(view, event);
	}

	@Override
	public void onScaleChanged(com.tencent.smtt.sdk.WebView view, float oldScale, float newScale) {
		logger("onScaleChanged");
		if (hasDelegate())
			delegate().onScaleChanged(view, oldScale, newScale);
		else

			super.onScaleChanged(view, oldScale, newScale);
	}

	@Override
	public boolean shouldOverrideKeyEvent(com.tencent.smtt.sdk.WebView webView, KeyEvent keyEvent) {
		logger("shouldOverrideKeyEvent");
		if (hasDelegate())
			return delegate().shouldOverrideKeyEvent(webView, keyEvent);
		else
			return super.shouldOverrideKeyEvent(webView, keyEvent);
	}

	@Override
	public com.tencent.smtt.export.external.interfaces.WebResourceResponse shouldInterceptRequest(com.tencent.smtt.sdk.WebView webView, String s) {
		logger("shouldInterceptRequest");
		if (hasDelegate()){
			return delegate().shouldInterceptRequest(webView, s);
		}else {
			return super.shouldInterceptRequest(webView, s);
		}
	}


	@Override
	public void onReceivedError(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest, com.tencent.smtt.sdk.WebViewClient.a a) {
		super.onReceivedError(webView, webResourceRequest, a);
		logger("onReceivedError");

	}

	@Override
	public void onReceivedHttpError(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest, com.tencent.smtt.export.external.interfaces.WebResourceResponse webResourceResponse) {
		super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
		logger("onReceivedHttpError");
	}

	@Override
	public com.tencent.smtt.export.external.interfaces.WebResourceResponse shouldInterceptRequest(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
		logger("shouldInterceptRequest");
		if (hasDelegate()){
			return delegate().shouldInterceptRequest(webView, webResourceRequest);
		}else {
			return super.shouldInterceptRequest(webView, webResourceRequest);
		}
	}
}
