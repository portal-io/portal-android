package com.whaley.biz.common.model.share;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/8/9.
 */

public class ShareModel implements Parcelable {

    private String from;
    private int shareType;
    private int type;
    private String title;
    private String des;
    private String extra;
    private String url;
    private String code;
    private String imgUrl;
    private int resId;
    private boolean isHorizontal;
    private boolean isFullscreen;
    private boolean isVideo = false;

    public ShareModel(String from, int shareType, int type, String title, String des, String extra, String url, String code, boolean isVideo, boolean isHorizontal, boolean isFullscreen, String imgUrl, int resId) {
        this.from = from;
        this.shareType = shareType;
        this.type = type;
        this.title = title;
        this.des = des;
        this.extra = extra;
        this.url = url;
        this.code = code;
        this.isVideo = isVideo;
        this.isHorizontal = isHorizontal;
        this.isFullscreen = isFullscreen;
        this.imgUrl = imgUrl;
        this.resId = resId;
    }

    public String getExtra() {
        return extra;
    }

    public String getCode() {
        return code;
    }

    public static ShareModel.Builder createBuilder() {
        return new ShareModel.Builder();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        isFullscreen = fullscreen;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public static class Builder {
        private String from;
        private int shareType;
        private int type;
        private String title;
        private String des;
        private String extra;
        private String url;
        private String code;
        private boolean isHorizontal;
        private boolean isFullscreen;
        private boolean isVideo = false;
        private String imgUrl;
        private int resId;

        public Builder() {
        }

        public ShareModel.Builder setFullscreen(boolean isFullscreen) {
            this.isFullscreen = isFullscreen;
            return this;
        }

        public ShareModel.Builder setHorizontal(boolean isHorizontal) {
            this.isHorizontal = isHorizontal;
            return this;
        }

        public ShareModel.Builder setFrom(String from) {
            this.from = from;
            return this;
        }

        public ShareModel.Builder setVideo(boolean isVideo) {
            this.isVideo = isVideo;
            return this;
        }

        public ShareModel.Builder setShareType(int shareType) {
            this.shareType = shareType;
            return this;
        }

        public ShareModel.Builder setType(int type) {
            this.type = type;
            return this;
        }

        public ShareModel.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ShareModel.Builder setDes(String des) {
            this.des = des;
            return this;
        }

        public ShareModel.Builder setExtra(String extra) {
            this.extra = extra;
            return this;
        }

        public ShareModel.Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public ShareModel.Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public ShareModel.Builder setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public ShareModel.Builder setResId(int resId) {
            this.resId = resId;
            return this;
        }

        public ShareModel build() {
            return new ShareModel(this.from, this.shareType, this.type, this.title, this.des, this.extra, this.url, this.code, this.isVideo, this.isHorizontal, this.isFullscreen, this.imgUrl, this.resId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.from);
        dest.writeInt(this.shareType);
        dest.writeInt(this.type);
        dest.writeString(this.title);
        dest.writeString(this.des);
        dest.writeString(this.url);
        dest.writeString(this.imgUrl);
        dest.writeInt(this.resId);
        dest.writeByte(this.isHorizontal ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFullscreen ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isVideo ? (byte) 1 : (byte) 0);
    }

    protected ShareModel(Parcel in) {
        this.from = in.readString();
        this.shareType = in.readInt();
        this.type = in.readInt();
        this.title = in.readString();
        this.des = in.readString();
        this.url = in.readString();
        this.imgUrl = in.readString();
        this.resId = in.readInt();
        this.isHorizontal = in.readByte() != 0;
        this.isFullscreen = in.readByte() != 0;
        this.isVideo = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ShareModel> CREATOR = new Parcelable.Creator<ShareModel>() {
        @Override
        public ShareModel createFromParcel(Parcel source) {
            return new ShareModel(source);
        }

        @Override
        public ShareModel[] newArray(int size) {
            return new ShareModel[size];
        }
    };
}
