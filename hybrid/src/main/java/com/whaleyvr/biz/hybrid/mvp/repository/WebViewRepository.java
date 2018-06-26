package com.whaleyvr.biz.hybrid.mvp.repository;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.core.share.model.ShareParam;
import com.whaleyvr.biz.hybrid.model.WebPayload;

import java.io.Serializable;

/**
 * Created by YangZhi on 2016/10/31 18:53.
 */
public class WebViewRepository extends MemoryRepository implements Serializable, Parcelable {

    String url;

    WebPayload.TitleBarModel titleBarModel;

    ShareParam shareParam;

    int cachePolice;
    ShareParam.Builder builder;

    public ShareParam.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(ShareParam.Builder builder) {
        this.builder = builder;
    }

    //    @Override
//    public boolean isShowEmptyView() {
//        return false;
//    }
//
//    @Override
//    public boolean isNoData() {
//        return false;
//    }
//
//    @Override
//    public boolean isRefreshDataOnShow() {
//        return false;
//    }


    public WebPayload.TitleBarModel getTitleBarModel() {
        return titleBarModel;
    }

    public void setTitleBarModel(WebPayload.TitleBarModel titleBarModel) {
        this.titleBarModel = titleBarModel;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

//    public ShareParam getShareParam() {
//        return shareParam;
//    }
//
//    public void setShareParam(ShareParam shareParam) {
//        this.shareParam = shareParam;
//    }

//    public void setShareParam(ShareModel shareParam) {
//
//    }

    public void setCachePolice(int cachePolice) {
        this.cachePolice = cachePolice;
    }

    public int getCachePolice() {
        return cachePolice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeSerializable(this.titleBarModel);
        dest.writeSerializable(this.shareParam);
        dest.writeInt(this.cachePolice);
    }

    public WebViewRepository() {
    }

    protected WebViewRepository(Parcel in) {
        this.url = in.readString();
        this.titleBarModel = (WebPayload.TitleBarModel) in.readSerializable();
        this.shareParam = (ShareParam) in.readSerializable();
        this.cachePolice = in.readInt();
    }

    public static final Creator<WebViewRepository> CREATOR = new Creator<WebViewRepository>() {
        @Override
        public WebViewRepository createFromParcel(Parcel source) {
            return new WebViewRepository(source);
        }

        @Override
        public WebViewRepository[] newArray(int size) {
            return new WebViewRepository[size];
        }
    };

    public void setShareParam(ShareParam shareParam) {
        this.shareParam = shareParam;
    }

    public ShareParam getShareParam() {
        return shareParam;
    }
}
