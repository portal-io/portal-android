package com.whaley.biz.program.playersupport.model;

/**
 * Created by dell on 2017/8/29.
 */

public class PathInfo {

    private String url = "";
    private String bittype = "";
    private String renderType =  "";

    public PathInfo(String url, String bittype, String renderType){
        this.url = url;
        this.bittype = bittype;
        this.renderType = renderType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBittype() {
        return bittype;
    }

    public void setBittype(String bittype) {
        this.bittype = bittype;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }
}
