package com.whaleyvr.biz.hybrid.model;

/**
 * Created by YangZhi on 2016/10/13 1:34.
 */
public class SharePayload {

    private String callbackId;

    private ShareModel shareModel;

    public static class ShareModel {
        private String title;

        private String url;

        private String imgUrl;

        private String desc;

        private int platform;

        private String mediaType;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getPlatform() {
            return platform;
        }

        public void setPlatform(int platform) {
            this.platform = platform;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }
    }


    public ShareModel getShareModel() {
        return shareModel;
    }

    public void setShareModel(ShareModel shareModel) {
        this.shareModel = shareModel;
    }


    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }
}
