package com.whaley.biz.setting.model.player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2017/8/25.
 */

public class DataBuilder {

    /**
     * 原始地址
     */
    private final String orginUrl;

    /**
     * 播放器类型
     */
    private final int type;

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
    private long duration;

    /**
     *
     */
    private boolean isMonocular;

    /**
     * 是否循环
     */
    private boolean isLooping;


    /**
     * 是否用硬解码
     */
    private boolean useHardDecoder = true;

    private Map<String,Object> customDatas;

    private DataBuilder(String orginUrl, int type){
        this.orginUrl=orginUrl;
        this.type=type;
    }

    public static DataBuilder createBuilder(String orginUrl,int type){
        DataBuilder dataBuilder=new DataBuilder(orginUrl,type);
        return dataBuilder;
    }

    public PlayData build(){
        return new PlayData(orginUrl,type,title,id,renderType,progress,duration,isMonocular,isLooping,useHardDecoder,customDatas);
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public DataBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置资源ID
     * @param id
     * @return
     */
    public DataBuilder setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 设置渲染模式
     * @param renderType
     * @return
     */
    public DataBuilder setRenderType(int renderType) {
        this.renderType = renderType;
        return this;
    }

    /**
     * 设置初始进度
     * @param progress
     * @return
     */
    public DataBuilder setProgress(long progress) {
        this.progress = progress;
        return this;
    }

    /**
     * 设置总时长
     * @param duration
     * @return
     */
    public DataBuilder setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    /**
     *
     * @param monocular
     * @return
     */
    public DataBuilder setMonocular(boolean monocular) {
        isMonocular = monocular;
        return this;
    }

    /**
     * 是否使用循环
     * @param looping
     * @return
     */
    public DataBuilder setLooping(boolean looping) {
        isLooping = looping;
        return this;
    }

    /**
     * 是否使用硬解
     * @param useHardDecoder
     * @return
     */
    public DataBuilder setUseHardDecoder(boolean useHardDecoder) {
        this.useHardDecoder = useHardDecoder;
        return this;
    }

    public DataBuilder putCustomData(String key,Object data){
        if(customDatas == null){
            customDatas = new HashMap<>();
        }
        customDatas.put(key,data);
        return this;
    }

    public DataBuilder setCustomData(Map<String,Object> customDatas){
        this.customDatas = customDatas;
        return this;
    }
}

