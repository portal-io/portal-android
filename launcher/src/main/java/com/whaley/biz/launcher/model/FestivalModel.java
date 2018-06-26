package com.whaley.biz.launcher.model;

import java.io.Serializable;

/**
 * Created by dell on 2018/1/25.
 */

public class FestivalModel implements Serializable {
    /**
     * code : springFestival2018
     * nowTime : 1514736000000 //服务器当前时间
     * relArrangeCodes : aaaa,bbb,ccc,ddd,eee,fff,ggg //活动关联专辑编码，后台可以手动提前录入，前端依据活动时间及当前时间取对应专辑跳转（去收集）
     * displayName : 2018春节活动-测试
     * enableTime : 1514736000000 //开始时间
     * description : 测试-春节活动描述
     * url : http://testurl/abc?123
     * relCodes : aaa|bbb|ccc|ddd|eee
     * updateTime : null
     * totalCount : null
     * type : h5event
     * relatedType : null
     * createTime : null
     * relatedCode : null
     * id : null
     * usedCount : null
     * disableTime : 1519056000000 //结束时间
     * status : 1
     */

    private String code;
    private long nowTime;
    private String displayName;
    private long enableTime;
    private String description;
    private String url;
    private String relCodes;
    private String updateTime;
    private String totalCount;
    private String type;
    private String relatedType;
    private String createTime;
    private String relatedCode;
    private String id;
    private String usedCount;
    private long disableTime;
    private int status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getNowTime() {
        return nowTime;
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(long enableTime) {
        this.enableTime = enableTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRelCodes() {
        return relCodes;
    }

    public void setRelCodes(String relCodes) {
        this.relCodes = relCodes;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRelatedCode() {
        return relatedCode;
    }

    public void setRelatedCode(String relatedCode) {
        this.relatedCode = relatedCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(String usedCount) {
        this.usedCount = usedCount;
    }

    public long getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(long disableTime) {
        this.disableTime = disableTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
