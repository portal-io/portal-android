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
import android.os.Handler;

import com.whaleyvr.biz.hybrid.jsbridge.JsBridgeHandler.OnCompletedListener;
import com.whaleyvr.biz.hybrid.jsbridge.util.ForwardingWebViewClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.HashMap;
import java.util.Map;

public abstract class JsBridgeImpl implements JsBridge {

	// A default Callback that does nothing.
	protected static final JsCallback _DEFAULT = new JsCallback() {
		@Override
		public void call(Map<Object,Object> payload) {
		}
	};

	private Map<String, CompositeJsBridgeHandler> _listeners = new HashMap<String, CompositeJsBridgeHandler>();
	private Map<String,JsCallback> _callbacks = new HashMap<String, JsCallback>();

	private OnValidateListener _onValidateListener;

	private Handler _handler = new Handler();

	private JsBridgeWebViewClient _client;

	public JsBridgeImpl() {
		_client = new JsBridgeWebViewClient(this);
	}

	@Override
	public void send(String type, WebView toWebView) {
		send(type, toWebView, null);
	}

	@Override
	public void send(String type, WebView toWebView, Object withPayload) {
		send(type, toWebView, withPayload, null);
	}

	@Override
	public void send(String type, WebView toWebView, JsCallback complete) {
		send(type, toWebView, null, complete);

	}

	@Override
	public void on(String type, JsBridgeHandler... handler) {

		if (!this.handles(type)) {
			_listeners.put(type, new CompositeJsBridgeHandler());
		}

		_listeners.get(type).add(handler);
	}

	@Override
	public void off(String type) {
		_listeners.remove(type);
	}

	@Override
	public boolean handles(String eventName) {
		return _listeners.containsKey(eventName);
	}

	protected void add(String messageId, JsCallback callback) {
		_callbacks.put(messageId, callback);
	}

	protected void triggerEventFromWebView(final WebView webView,
			JsBridgeWebViewPayload envelope) {
		final String messageId = envelope.id;
		String eventName = envelope.eventName;

		if (this.handles(eventName)) {
			JsBridgeHandler handler = _listeners.get(eventName);

			handler.perform(envelope.payload, new OnCompletedListener() {
				@Override
				public void onCompleted(final Map<Object,Object> callbackPayload) {
					// This has to be done with a handler because a webview load
					// must be triggered
					// in the UI thread
					_handler.post(new Runnable() {
						@Override
						public void run() {
							triggerCallbackOnWebView(webView, messageId,callbackPayload);
						}
					});
				}
			});
		}
	}

	protected void triggerCallbackForMessage(String messageId,JsBridgeWebViewPayload envelope) {
		try {
			String eventName = envelope.eventName;
			JsCallback complete = _callbacks.get(messageId);
			if(complete==null){
				complete=_DEFAULT;
			}
			complete.call(envelope.payload);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		_callbacks.remove(messageId);
	}

	public void validate(String host) throws HostValidationException {
		if (_onValidateListener != null && !_onValidateListener.validate(host)) {
			throw new HostValidationException();
		}
	}

	@Override
	public void setOnValidateListener(OnValidateListener listener) {
		_onValidateListener = listener;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void configure(WebView webView) {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(this.getWebViewClient());
	}

	protected ForwardingWebViewClient getWebViewClient() {
		return this._client;
	}

	public static JsBridge getDefault() {
		return new DefaultJsBridgeImpl();
	}
	
	@Override
	public void setWebViewClient(com.tencent.smtt.sdk.WebViewClient client) {
		if(this._client!=null)
		this._client.setDelegate(client);
	}

	@Override
	public void destory() {
		_listeners.clear();
		_callbacks.clear();
		_handler.removeCallbacksAndMessages(null);
		setWebViewClient(null);
		_client=null;
	}
}
