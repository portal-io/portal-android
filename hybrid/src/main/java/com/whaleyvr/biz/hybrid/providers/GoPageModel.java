package com.whaleyvr.biz.hybrid.providers;

import com.whaley.biz.common.model.share.ShareModel;
import com.whaleyvr.biz.hybrid.model.WebPayload;

/**
 * Created by dell on 2017/8/9.
 */

public class GoPageModel {

    private String url;
    private WebPayload.TitleBarModel titleBarModel;
    private ShareModel shareModel;

    public WebPayload.TitleBarModel getTitleBarModel() {
        return titleBarModel;
    }

    public void setTitleBarModel(WebPayload.TitleBarModel titleBarModel) {
        this.titleBarModel = titleBarModel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ShareModel getShareModel() {
        return shareModel;
    }

    public void setShareModel(ShareModel shareModel) {
        this.shareModel = shareModel;
    }
}
