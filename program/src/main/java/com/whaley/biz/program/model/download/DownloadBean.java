package com.whaley.biz.program.model.download;

import com.whaley.biz.program.constants.DownloadStatus;
import com.whaley.biz.program.constants.DownloadType;
import com.whaley.biz.program.model.ProgramDetailModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.AppFileStorage;

import java.io.File;

/**
 * Created by dell on 2017/8/23.
 */

public class DownloadBean {

    public String itemid;
    public int status;
    public float progress;
    public String downloadUrl;
    public String savePath;
    public String name;
    public String pic;
    public long totalSize;
    public long currentSize;
    public int type = DownloadType.DOWNLOAD_TYPE_VIDEO;
    public long downloadDate;
    public String intro;
    public String renderType;

    public DownloadBean(){

    }

    public DownloadBean(String itemid, int status, float progress,
                        String downloadUrl, String savePath, String name, String pic,
                        long totalSize, long currentSize, int type, long downloadDate,
                        String intro, String renderType) {
        this.itemid = itemid;
        this.status = status;
        this.progress = progress;
        this.downloadUrl = downloadUrl;
        this.savePath = savePath;
        this.name = name;
        this.pic = pic;
        this.totalSize = totalSize;
        this.currentSize = currentSize;
        this.type = type;
        this.downloadDate = downloadDate;
        this.intro = intro;
        this.renderType = renderType;
    }

    public static DownloadBean create(String itemid, ProgramDetailModel dataBean, String url, String renderType) {
        DownloadBean bean = new DownloadBean();
        bean.setItemid(itemid);
        bean.setDownloadUrl(url);
        String savePath = AppFileStorage.getDownloadMoviePath()
                + File.separator
                + dataBean.getCode()
                + ".ts";
        bean.setSavePath(savePath);
        bean.setStatus(DownloadStatus.DOWNLOAD_STATUS_NOTSTARTED);
        bean.setName(dataBean.getDisplayName());
        bean.setPic(dataBean.getBigPic());
        bean.setIntro(dataBean.getDescription());
        bean.setProgress(0);
        bean.setCurrentSize(0);
        bean.setType(DownloadType.DOWNLOAD_TYPE_VIDEO);
        bean.setDownloadDate(System.currentTimeMillis());
        bean.setRenderType(renderType);
        return bean;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(long downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public static DownloadBean.Builder createBuilder() {
        return new DownloadBean.Builder();
    }

    public static class Builder {

        public String itemid;
        public int status;
        public float progress;
        public String downloadUrl;
        public String savePath;
        public String name;
        public String pic;
        public long totalSize;
        public long currentSize;
        public int type= DownloadType.DOWNLOAD_TYPE_VIDEO;
        public long downloadDate;
        public String intro;
        public String renderType;

        public DownloadBean build() {
            return new DownloadBean(itemid, status, progress, downloadUrl, savePath, name, pic, totalSize, currentSize, type, downloadDate, intro, renderType);
        }

        public DownloadBean.Builder setItemid(String itemid) {
            this.itemid = itemid;
            return this;
        }

        public DownloadBean.Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public DownloadBean.Builder setProgress(float progress) {
            this.progress = progress;
            return this;
        }

        public DownloadBean.Builder setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public DownloadBean.Builder setSavePath(String savePath) {
            this.savePath = savePath;
            return this;
        }

        public DownloadBean.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public DownloadBean.Builder setPic(String pic) {
            this.pic = pic;
            return this;
        }

        public DownloadBean.Builder setTotalSize(long totalSize) {
            this.totalSize = totalSize;
            return this;
        }

        public DownloadBean.Builder setCurrentSize(long currentSize) {
            this.currentSize = currentSize;
            return this;
        }

        public DownloadBean.Builder setType(int type) {
            this.type = type;
            return this;
        }

        public DownloadBean.Builder setDownloadDate(long downloadDate) {
            this.downloadDate = downloadDate;
            return this;
        }

        public DownloadBean.Builder setIntro(String intro) {
            this.intro = intro;
            return this;
        }

        public DownloadBean.Builder setRenderType(String renderType) {
            this.renderType = renderType;
            return this;
        }
    }

    @Override
    public String toString() {
        return "DownloadBean{" +
                "currentSize=" + currentSize +
                ", itemid='" + itemid + '\'' +
                ", totalSize=" + totalSize +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", savePath='" + savePath + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof DownloadBean) {
                DownloadBean bean = (DownloadBean) o;
                return bean.getItemid().equals(getItemid());
            } else if (o instanceof String) {
                String itemId = (String) o;
                return itemId.equals(o);
            }
        }
        return false;
    }

}
