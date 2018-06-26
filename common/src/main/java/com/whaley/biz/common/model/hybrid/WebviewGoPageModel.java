package com.whaley.biz.common.model.hybrid;


import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.share.ShareModel;

/**
 * Created by dell on 2017/8/9.
 */

public class WebviewGoPageModel implements Parcelable {

    public static WebviewGoPageModel createWebviewGoPageModel(String url) {
        WebviewGoPageModel webviewGoPageModel = new WebviewGoPageModel();
        webviewGoPageModel.setUrl(url);
        return webviewGoPageModel;
    }

    public static WebviewGoPageModel createWebviewGoPageModel(String url, TitleBarModel titleBarModel) {
        WebviewGoPageModel webviewGoPageModel = new WebviewGoPageModel();
        webviewGoPageModel.setUrl(url);
        webviewGoPageModel.setTitleBarModel(titleBarModel);
        return webviewGoPageModel;
    }

    public static WebviewGoPageModel createWebviewGoPageModel(String url, TitleBarModel titleBarModel, ShareModel shareModel) {
        WebviewGoPageModel webviewGoPageModel = new WebviewGoPageModel();
        webviewGoPageModel.setUrl(url);
        webviewGoPageModel.setTitleBarModel(titleBarModel);
        webviewGoPageModel.setShareModel(shareModel);
        return webviewGoPageModel;
    }

    private String url;
    private TitleBarModel titleBarModel;
    private ShareModel shareModel;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TitleBarModel getTitleBarModel() {
        return titleBarModel;
    }

    public void setTitleBarModel(TitleBarModel titleBarModel) {
        this.titleBarModel = titleBarModel;
    }

    public ShareModel getShareModel() {
        return shareModel;
    }

    public void setShareModel(ShareModel shareModel) {
        this.shareModel = shareModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeParcelable(this.titleBarModel, flags);
        dest.writeParcelable(this.shareModel, flags);
    }

    public WebviewGoPageModel() {
    }

    protected WebviewGoPageModel(Parcel in) {
        this.url = in.readString();
        this.titleBarModel = in.readParcelable(TitleBarModel.class.getClassLoader());
        this.shareModel = in.readParcelable(ShareModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<WebviewGoPageModel> CREATOR = new Parcelable.Creator<WebviewGoPageModel>() {
        @Override
        public WebviewGoPageModel createFromParcel(Parcel source) {
            return new WebviewGoPageModel(source);
        }

        @Override
        public WebviewGoPageModel[] newArray(int size) {
            return new WebviewGoPageModel[size];
        }
    };
}
