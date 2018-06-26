package com.whaleyvr.biz.hybrid;

import com.tencent.smtt.sdk.WebViewClient;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaleyvr.biz.hybrid.widget.VRWebView;

import java.util.Map;

/**
 * H5交互控制器
 * Created by YangZhi on 2016/10/8 17:35.
 */
public interface H5Controller  extends EmptyDisplayView {

    /**
     * 发出一个事件
     * @param type
     */
    void sendEvent(String type);

    /**
     * 发出一个事件 并传送一个数据
     * @param type
     * @param data
     */
     void sendEvent(String type, Object data);

    /**
     * 发出一个事件 并监听JS回调
     * @param type
     * @param completedListener
     */
     void sendEvent(String type, OnCompletedListener completedListener);

    /**
     * 发出一个事件 传送一个数据 并监听JS回调
     * @param type
     * @param data
     * @param completedListener
     */
     void sendEvent(String type, Object data, OnCompletedListener completedListener);

    /**
     * 监听一个js事件,回调在UI线程
     * @param type
     * @param callback 当监听到该事件的回调
     */
     void onEvent(String type, H5Callback... callback);

     /**
      * 监听一个js事件,回调在异步线程
      * @param type
      * @param callback 当监听到该事件的回调
      */
     void onEventAsync(String type, H5Callback... callback);

     /**
      * 取消监听某个已监听的js事件
      * @param type
      */
     void offEvent(String type);


    /**
     * 发送一个回调
     * @param jsComplete
     * @param payload
     */
     void sendCalback(final JSComplete jsComplete, final Map<Object, Object> payload);

    /**
     * 设置自定义WebViewClient
     * @param client
     */
     void setWebViewClient(WebViewClient client);


    /**
     * 获得WebView实例
     * @return
     */
     VRWebView getWebView();

    /**
     * 设置缓存策略
     * @param cachePolicy
     */
    void setCachePolicy(CachePolicy cachePolicy);


    /**
     * 加载url
     * @param url
     */
    void loadUrl(String url);

    Starter getStarter();

    /**
     * 获取页面时候加载完成
     * @return
     */
    boolean isLoaded();
}
