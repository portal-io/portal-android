package com.whaleyvr.biz.hybrid.mvp.view;

import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.core.widget.titlebar.ITitleBar;
import com.whaleyvr.biz.hybrid.widget.VRWebView;

/**
 * Created by YangZhi on 2016/10/31 18:53.
 */
public interface WebView extends BasePageView {

    ITitleBar getTitleBar();

    void loadWebUrl(String url);

    void loadInitUrl(String url);

    VRWebView getWebView();

    void  destoryWebView();

    void skipLongClick();

    void setTransparentFullStatusBar();
}
