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


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class JsBridgeService extends Service implements JsBridge {

	private final IBinder _binder = new JockeyBinder();
	
	private JsBridge _jsBridgeImpl = JsBridgeImpl.getDefault();
	
	/**
	 * Convenience method for binding to the JsBridgeService
	 * 
	 * @param context
	 * @param connection
	 */
	public static boolean bind(Context context, ServiceConnection connection) {
		return context.bindService(new Intent(context, JsBridgeService.class),
				connection, Context.BIND_AUTO_CREATE);
	}

	public static void unbind(Context context, ServiceConnection connection) {
		context.unbindService(connection);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return _binder;
	}

	public class JockeyBinder extends Binder {
	
		public JsBridge getService() {
			return JsBridgeService.this;
		}
	}

	@Override
	public void setOnValidateListener(OnValidateListener listener) {
		_jsBridgeImpl.setOnValidateListener(listener);
	}

	public void on(String type, JsBridgeHandler... handler) {
		_jsBridgeImpl.on(type, handler);
	}

	@Override
	public void off(String type) {
		_jsBridgeImpl.off(type);
	}

	public void send(String type, WebView toWebView) {
		send(type, toWebView, null);
	}

	public void send(String type, WebView toWebView, Object withPayload) {
		send(type, toWebView, withPayload, null);
	}

	public void send(String type, WebView toWebView, JsCallback complete) {
		send(type, toWebView, null, complete);
	}

	public void send(String type, WebView toWebView, Object withPayload,
			JsCallback complete) {
		_jsBridgeImpl.send(type, toWebView, withPayload, complete);
	}

	public void triggerCallbackOnWebView(WebView webView, String messageId,Object withPayload) {
		_jsBridgeImpl.triggerCallbackOnWebView(webView, messageId,withPayload);
	}

	public void configure(WebView webView) {
		_jsBridgeImpl.configure(webView);
	}

	@Override
	public boolean handles(String eventName) {
		return _jsBridgeImpl.handles(eventName);
	}

	public void setWebViewClient(WebViewClient client) {
		_jsBridgeImpl.setWebViewClient(client);
	}


	@Override
	public void destory() {
		_jsBridgeImpl.destory();
	}
}
