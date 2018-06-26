package com.whaleyvr.biz.hybrid.jsbridge;

import android.util.Log;

import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebView;

public class DefaultJsBridgeImpl extends JsBridgeImpl {
	
	private int messageCount = 0;
	private Gson gson = new Gson();

	@Override
	public void send(String type, WebView toWebView, Object withPayload,
			JsCallback complete) {
		int messageId = messageCount;

		if (complete != null) {
			add(""+messageId, complete);
		}

		if (withPayload != null) {
			withPayload = gson.toJson(withPayload);
		}

		String url = String.format("javascript:jsBridge.trigger(\"%s\", %d, %s)",
				type, messageId, withPayload);

		Log.d("JsBridge","send url="+url);
		toWebView.loadUrl(url);

		++messageCount;
	}

	@Override
	public void triggerCallbackOnWebView(WebView webView, String messageId,Object withPayload) {
		if (withPayload != null) {
			withPayload = gson.toJson(withPayload);
		}

		String url = String.format("javascript:jsBridge.triggerCallback(\"%s\",%s)",
				messageId,withPayload);
		Log.d("JsBridge","callbackToJs url="+url);
		webView.loadUrl(url);
	}

}
