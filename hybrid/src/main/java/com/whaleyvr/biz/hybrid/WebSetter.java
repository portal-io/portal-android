package com.whaleyvr.biz.hybrid;

import android.content.Intent;

import com.whaleyvr.biz.hybrid.widget.VRWebView;

/**
 * WebView的设置器
 * Created by YangZhi on 2016/10/11 18:07.
 */
public interface WebSetter {

    /**
     * 设置WebView
     * @param webView
     */
    void onSetWebView(VRWebView webView);

    /**
     * 销毁WebView
     */
    void destroy();


    /**
     * 获取H5控制器
     * @return
     */
    H5Controller getH5Controller();

    /**
     * 获取WebView实例
     * @return
     */
    VRWebView getWebView();


    /**
     * onActivityResult
     * @return
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
