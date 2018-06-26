package com.whaley.biz.setting.model.download;

import android.text.TextUtils;

import com.whaley.biz.setting.DownloadStatus;
import com.whaley.biz.setting.DownloadType;
import com.whaley.biz.setting.ServerRenderType;
import com.whaley.biz.setting.VideoConstantValue;
import com.whaley.biz.setting.constant.ProgramConstants;
import com.whaley.biz.setting.model.player.DataBuilder;
import com.whaley.biz.setting.model.player.PlayData;
import com.whaley.biz.setting.util.RenderTypeUtil;
import com.whaley.core.appcontext.AppFileStorage;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
    public int type= DownloadType.DOWNLOAD_TYPE_VIDEO;
    public long downloadDate;
    public String intro;
    public String renderType;

    /**
     * 通过URL创建DownloadBean
     *
     * @param url
     * @return
     */
    public static DownloadBean create(String url) {
        String fileName = null;
        try {
            String tail = URLDecoder.decode(url.substring(url.lastIndexOf("/") + 1, url.length()), "utf-8");
            if (TextUtils.isEmpty(tail)) {
                fileName = "VID_" + System.currentTimeMillis();
            } else {
                fileName = tail;
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        String savePath = AppFileStorage.getDownloadPath() + File.separator + System.currentTimeMillis() + fileName;
        return new DownloadBean(url + System.currentTimeMillis(), DownloadStatus.DOWNLOAD_STATUS_NOTSTARTED, 0,
                url, savePath, fileName, "",
                0, 0, DownloadType.DOWNLOAD_TYPE_LOCAL_IMPORT,
                System.currentTimeMillis(), "", "360_2D");
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

    public PlayData getPlayData() {
        return DataBuilder.createBuilder(getSavePath(), ProgramConstants.TYPE_PLAY_LOCALVIDEO)
                .setId(getItemid())
                .setTitle(getName())
                .setRenderType(RenderTypeUtil.getRenderTypeByRenderTypeStr(getRenderType()))
                .putCustomData("key_is_Can_change_render",false)
                .setMonocular(true)
                .build();
    }

}
