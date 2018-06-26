package com.whaley.biz.program.playersupport.model;

/**
 * 清晰度模型
 */
public class DefinitionModel {
    /**
     * 该清晰度播放的地址
     */
    private String url;

    /**
     * 该清晰度播放的渲染模式
     */
    private int renderType;

    /**
     * 该清晰度的清晰度类型 {@link com.longsir.yuimediaplayer.urlparser.bean.VideoBitType}
     */
    private int definition;


    public String getUrl() {
        return url;
    }

    public int getRenderType() {
        return renderType;
    }

    public int getDefinition() {
        return definition;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setRenderType(int renderType) {
        this.renderType = renderType;
    }

    public void setDefinition(int definition) {
        this.definition = definition;
    }
}