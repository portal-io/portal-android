/*******************************************************************************
 * Copyright (c) 2013,  Paul Daniels
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package com.whaleyvr.biz.hybrid.jsbridge;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.whaleyvr.biz.hybrid.jsbridge.util.ForwardingWebViewClient;

import java.net.URI;
import java.net.URISyntaxException;

@SuppressLint("SetJavaScriptEnabled")
class JsBridgeWebViewClient extends ForwardingWebViewClient {

	private JsBridgeImpl _jockeyImpl;
	private WebViewClient _delegate;
	private Gson _gson;

	public JsBridgeWebViewClient(JsBridgeImpl jockey) {
		_gson = new Gson();
		_jockeyImpl = jockey;
	}

	public JsBridgeImpl getImplementation() {
		return _jockeyImpl;
	}

	protected void setDelegate(WebViewClient client) {
		_delegate = client;
	}

	public WebViewClient delegate() {
		return _delegate;
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
	
		if (delegate() != null
				&& delegate().shouldOverrideUrlLoading(view, url))
			return true;
	
		try {
			URI uri = new URI(url);
	
			if (isJockeyScheme(uri)) {
				Log.d("JsBridge","load url ="+url);
				processUri(view, uri);
				return true;
			}
		} catch (URISyntaxException e) {
//			e.printStackTrace();
		} catch (HostValidationException e) {
//			e.printStackTrace();
			Log.e("JsBridge", "The source of the event could not be validated!");
		}
		return false;
	}

	public boolean isJockeyScheme(URI uri) {
		return uri.getScheme().equals("jsbridge") && !uri.getQuery().equals("");
	}

	public void processUri(WebView view, URI uri)
			throws HostValidationException {
		String[] parts = uri.getPath().replaceAll("^\\/", "").split("/");
		String host = uri.getHost();

		JsBridgeWebViewPayload payload = checkPayload(_gson.fromJson(
				uri.getQuery(), JsBridgeWebViewPayload.class));
		if(payload!=null){
			Log.d("JsBridge",payload.toString());
		}
		if (parts.length > 0) {
			if (host.equals("event")) {
				getImplementation().triggerEventFromWebView(view, payload);
			} else if (host.equals("callback")) {
				getImplementation().triggerCallbackForMessage(
						parts[0],payload);
			}
		}
	}

	public JsBridgeWebViewPayload checkPayload(JsBridgeWebViewPayload fromJson)
			throws HostValidationException {
		validateHost(fromJson.host);
		return fromJson;
	}

	private void validateHost(String host) throws HostValidationException {
		getImplementation().validate(host);
	}

}