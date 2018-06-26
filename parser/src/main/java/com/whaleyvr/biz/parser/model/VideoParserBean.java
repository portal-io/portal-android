package com.whaleyvr.biz.parser.model;

import java.io.Serializable;

/**
 * Created by dell on 2016/7/28.
 */
public class VideoParserBean implements Serializable{

    public int videoSourceType;
    public int videoBitType;
    public int videoAlgorithm;
    public String url;
    public int bitrate;

    public VideoParserBean(int videoBitType, int videoAlgorithm, int videoSourceType, String url, int bitrate){
        this.videoSourceType = videoSourceType;
        this.videoBitType = videoBitType;
        this.videoAlgorithm = videoAlgorithm;
        this.url = url;
        this.bitrate = bitrate;
    }

    public VideoParserBean(){

    }

}
