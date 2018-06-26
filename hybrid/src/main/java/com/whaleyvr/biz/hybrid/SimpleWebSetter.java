package com.whaleyvr.biz.hybrid;

import android.content.Intent;

import com.whaleyvr.biz.hybrid.widget.VRWebView;

/**
 * Created by YangZhi on 2017/9/25 19:13.
 */

public class SimpleWebSetter implements WebSetter{

    @Override
    public void onSetWebView(VRWebView webView) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public H5Controller getH5Controller() {
        return null;
    }

    @Override
    public VRWebView getWebView() {
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
