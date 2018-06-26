package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Author: qxw
 * Date: 2016/11/9
 */

public class TopicListModel extends BaseModel implements Parcelable, Serializable {
    //    name: "新歌声第一期",
//    code: "song_one",
//    position: 0,
//    businessVersion: "1.0",
//    appType: "null",
//    type: 3,
//    parentId: 223,
//    isLeaf: true,
//    bigImageUrl: "http://test-image.tvmore.com.cn/image/get-image/10000004/147763459313575830.jpg",
//    introduction: "新歌声第一期简介",
    private String name;
    private String code;
    private int position;
    private String businessVersion;
    private String appType;
    private int type;
    private int parentId;
    private boolean isLeaf;
    private String bigImageUrl;
    private String introduction;
    private List<TopicModel> arrangeTreeElements;
    private String bizType;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getBusinessVersion() {
        return businessVersion;
    }

    public void setBusinessVersion(String businessVersion) {
        this.businessVersion = businessVersion;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public void setBigImageUrl(String bigImageUrl) {
        this.bigImageUrl = bigImageUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public List<TopicModel> getArrangeTreeElements() {
        return arrangeTreeElements;
    }

    public void setArrangeTreeElements(List<TopicModel> arrangeTreeElements) {
        this.arrangeTreeElements = arrangeTreeElements;
    }

    public TopicListModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeInt(this.position);
        dest.writeString(this.businessVersion);
        dest.writeString(this.appType);
        dest.writeInt(this.type);
        dest.writeInt(this.parentId);
        dest.writeByte(this.isLeaf ? (byte) 1 : (byte) 0);
        dest.writeString(this.bigImageUrl);
        dest.writeString(this.introduction);
        dest.writeTypedList(this.arrangeTreeElements);
        dest.writeString(this.bizType);
    }

    protected TopicListModel(Parcel in) {
        super(in);
        this.name = in.readString();
        this.code = in.readString();
        this.position = in.readInt();
        this.businessVersion = in.readString();
        this.appType = in.readString();
        this.type = in.readInt();
        this.parentId = in.readInt();
        this.isLeaf = in.readByte() != 0;
        this.bigImageUrl = in.readString();
        this.introduction = in.readString();
        this.arrangeTreeElements = in.createTypedArrayList(TopicModel.CREATOR);
        this.bizType = in.readString();
    }

    public static final Creator<TopicListModel> CREATOR = new Creator<TopicListModel>() {
        @Override
        public TopicListModel createFromParcel(Parcel source) {
            return new TopicListModel(source);
        }

        @Override
        public TopicListModel[] newArray(int size) {
            return new TopicListModel[size];
        }
    };
}
