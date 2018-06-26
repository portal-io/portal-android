package com.whaley.biz.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by dell on 2017/4/6.
 */

public class UpdateModel implements Serializable, Parcelable {

    /**
     * versionCode : 2017061601
     * versionName : 3.0.1
     * fileHash : 2A862513678D1B9C47063BD936ED04E5
     * filePath : http://down.tvmore.com.cn/VR/version/20170621/20170621162650976.apk
     * coPath :
     * moPath : null
     * newContent :
     * description : 版本号版本名测试接入
     * series :
     * type : 0
     * isUpdate : 1
     * updateType : 0
     * versionType : 1
     * bootUpgrade :
     * upgradeImage :
     * upgradeImageMd5 :
     */

    private String versionCode;
    private String versionName;
    private String fileHash;
    private String filePath;
    private String coPath;
    private String newContent;
    private String description;
    private String series;
    private int type;
    private int isUpdate;
    private int updateType;
    private int versionType;
    private String bootUpgrade;
    private String upgradeImage;
    private String upgradeImageMd5;

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCoPath() {
        return coPath;
    }

    public void setCoPath(String coPath) {
        this.coPath = coPath;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public int getVersionType() {
        return versionType;
    }

    public void setVersionType(int versionType) {
        this.versionType = versionType;
    }

    public String getBootUpgrade() {
        return bootUpgrade;
    }

    public void setBootUpgrade(String bootUpgrade) {
        this.bootUpgrade = bootUpgrade;
    }

    public String getUpgradeImage() {
        return upgradeImage;
    }

    public void setUpgradeImage(String upgradeImage) {
        this.upgradeImage = upgradeImage;
    }

    public String getUpgradeImageMd5() {
        return upgradeImageMd5;
    }

    public void setUpgradeImageMd5(String upgradeImageMd5) {
        this.upgradeImageMd5 = upgradeImageMd5;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.versionCode);
        dest.writeString(this.versionName);
        dest.writeString(this.fileHash);
        dest.writeString(this.filePath);
        dest.writeString(this.coPath);
        dest.writeString(this.newContent);
        dest.writeString(this.description);
        dest.writeString(this.series);
        dest.writeInt(this.type);
        dest.writeInt(this.isUpdate);
        dest.writeInt(this.updateType);
        dest.writeInt(this.versionType);
        dest.writeString(this.bootUpgrade);
        dest.writeString(this.upgradeImage);
        dest.writeString(this.upgradeImageMd5);
    }

    public UpdateModel() {
    }

    protected UpdateModel(Parcel in) {
        this.versionCode = in.readString();
        this.versionName = in.readString();
        this.fileHash = in.readString();
        this.filePath = in.readString();
        this.coPath = in.readString();
        this.newContent = in.readString();
        this.description = in.readString();
        this.series = in.readString();
        this.type = in.readInt();
        this.isUpdate = in.readInt();
        this.updateType = in.readInt();
        this.versionType = in.readInt();
        this.bootUpgrade = in.readString();
        this.upgradeImage = in.readString();
        this.upgradeImageMd5 = in.readString();
    }

    public static final Creator<UpdateModel> CREATOR = new Creator<UpdateModel>() {
        public UpdateModel createFromParcel(Parcel source) {
            return new UpdateModel(source);
        }

        public UpdateModel[] newArray(int size) {
            return new UpdateModel[size];
        }
    };
}
