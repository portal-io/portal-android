package com.whaley.biz.playerui.model;

import com.whaley.wvrplayer.vrplayer.external.event.DataParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YangZhi on 2017/4/17 17:59.
 */

public class PlayData {

    /**
     * 原始地址
     */
    private String orginUrl;

    /**
     * 播放器类型
     */
    private int type;

    /**
     * 播放的内容标题
     */
    private String title;

    /**
     * 播放的资源id
     */
    private String id;

    /**
     * 指定的渲染模式
     */
    private int renderType;

    /**
     * 起始播放进度
     */
    private long progress;

    /**
     * 总时长
     */
    private final long duration;

    /**
     * 是否分屏模式
     */
    private boolean isMonocular;

    /**
     * 是否循环
     */
    private final boolean isLooping;


    /**
     * 是否用硬解码
     */
    private boolean useHardDecoder = true;

    /**
     * 真实播放地址
     */
    private String realUrl;


    /**
     * 自定义数据
     */
    private Map<String, Object> customDatas;


    PlayData(String orginUrl, int type, String title, String id, int renderType, long progress, long duration, boolean isMonocular, boolean isLooping, boolean useHardDecoder, Map<String, Object> customDatas) {
        this.orginUrl = orginUrl;
        this.type = type;
        this.title = title;
        this.id = id;
        this.renderType = renderType;
        this.progress = progress;
        this.duration = duration;
        this.isMonocular = isMonocular;
        this.isLooping = isLooping;
        this.useHardDecoder = useHardDecoder;
        this.realUrl = orginUrl;
        this.customDatas = customDatas;
    }


    public DataParam convert() {
        DataParam dataParam = new DataParam();
        dataParam.path = realUrl;
        dataParam.position = progress;
        dataParam.useHardDecoder = useHardDecoder;
        dataParam.renderType = renderType;
        dataParam.isMonocular = isMonocular;
        dataParam.isLooping = isLooping;
        return dataParam;
    }

    public String getOrginUrl() {
        return orginUrl;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRenderType(int renderType) {
        this.renderType = renderType;
    }

    public int getRenderType() {
        return renderType;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getDuration() {
        return duration;
    }

    public void setMonocular(boolean monocular) {
        isMonocular = monocular;
    }

    public boolean isMonocular() {
        return isMonocular;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public boolean isUseHardDecoder() {
        return useHardDecoder;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    public void setOrginUrl(String orginUrl) {
        this.orginUrl = orginUrl;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public Map<String, Object> getCustomDatas() {
        return customDatas;
    }


    public void putCustomData(String key, Object data) {
        if (customDatas == null) {
            customDatas = new HashMap<>();
        }
        customDatas.put(key, data);
    }

    public <R> R getCustomData(String key) {
        if (customDatas != null) {
            return (R) customDatas.get(key);
        }
        return null;
    }

    public double getDoubleCustomData(String key) {
        Object object = getCustomData(key);
        if (object == null) {
            return 0;
        }
        return (double) object;
    }

    public int getIntegerCustomData(String key) {
        Object object = getCustomData(key);
        if (object == null) {
            return 0;
        }
        return (int) object;
    }

    public long getLongCustomData(String key) {
        Object object = getCustomData(key);
        if (object == null) {
            return 0;
        }
        return (long) object;
    }

    public short getShortCustomData(String key) {
        Object object = getCustomData(key);
        if (object == null) {
            return 0;
        }
        return (short) object;
    }

    public boolean getBooleanCustomData(String key) {
        Object object = getCustomData(key);
        if (object == null) {
            return false;
        }
        return (boolean) object;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof PlayData) {
            PlayData playData = (PlayData) obj;
            if (playData.getId() == null || getId() == null)
                return false;
            return playData.getId().equals(getId()) && playData.getType() == getType();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "PlayData{" +
                "orginUrl='" + orginUrl + '\'' +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", renderType=" + renderType +
                ", progress=" + progress +
                ", duration=" + duration +
                ", isMonocular=" + isMonocular +
                ", isLooping=" + isLooping +
                ", useHardDecoder=" + useHardDecoder +
                ", realUrl='" + realUrl + '\'' +
                ", customDatas=" + customDatas +
                '}';
    }
}
