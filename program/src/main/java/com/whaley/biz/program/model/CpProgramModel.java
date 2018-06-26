package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;
import com.whaley.biz.playerui.model.DataBuilder;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.core.utils.StrUtil;


/**
 * Author: qxw
 * Date: 2017/3/20
 */

public class CpProgramModel extends BaseModel implements Parcelable, ProgramConstants {


    private String code;
    private String score;
    private String displayName;
    private String subtitle;
    private String programType;
    private String videoType;
    private String type;
    private String typeName;
    private String director;
    private String actors;
    private String age;
    private String tags;
    private String tagCode;
    private String superscript;
    private String language;
    private String area;
    private String lunboPic;
    private String smallPic;
    private String bigPic;
    private String source;
    private String description;
    private int status;
    private int pubStatus;
    private String customUrl;
    private String price;
    private int isChargeable;
    private String relatedCode;
    private String centerPic;
    private String radius;
    private String cpCode;
    private StatQueryModel stat;
    private int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public String getSuperscript() {
        return superscript;
    }

    public void setSuperscript(String superscript) {
        this.superscript = superscript;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLunboPic() {
        return lunboPic;
    }

    public void setLunboPic(String lunboPic) {
        this.lunboPic = lunboPic;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getBigPic() {
        return bigPic;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPubStatus() {
        return pubStatus;
    }

    public void setPubStatus(int pubStatus) {
        this.pubStatus = pubStatus;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    public String getRelatedCode() {
        return relatedCode;
    }

    public void setRelatedCode(String relatedCode) {
        this.relatedCode = relatedCode;
    }

    public String getCenterPic() {
        return centerPic;
    }

    public void setCenterPic(String centerPic) {
        this.centerPic = centerPic;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getCpCode() {
        return cpCode;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public StatQueryModel getStat() {
        return stat;
    }

    public void setStat(StatQueryModel stat) {
        this.stat = stat;
    }

    public PlayData getPlayData() {
        int type = TYPE_PLAY_PANO;
        if (VIDEO_TYPE_3D.equals(videoType)) {
            type = TYPE_PLAY_3D;
        }
        return DataBuilder.createBuilder("", type)
                .setId(getCode())
                .setTitle(getDisplayName())
                .setMonocular(true)
                .build();
    }

    public CpProgramModel() {
    }

    public boolean isDrama() {
        return !StrUtil.isEmpty(programType) && ProgramConstants.TYPE_DYNAMIC.equals(programType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.score);
        dest.writeString(this.displayName);
        dest.writeString(this.subtitle);
        dest.writeString(this.programType);
        dest.writeString(this.videoType);
        dest.writeString(this.type);
        dest.writeString(this.typeName);
        dest.writeString(this.director);
        dest.writeString(this.actors);
        dest.writeString(this.age);
        dest.writeString(this.tags);
        dest.writeString(this.tagCode);
        dest.writeString(this.superscript);
        dest.writeString(this.language);
        dest.writeString(this.area);
        dest.writeString(this.lunboPic);
        dest.writeString(this.smallPic);
        dest.writeString(this.bigPic);
        dest.writeString(this.source);
        dest.writeString(this.description);
        dest.writeInt(this.status);
        dest.writeInt(this.pubStatus);
        dest.writeString(this.customUrl);
        dest.writeString(this.price);
        dest.writeInt(this.isChargeable);
        dest.writeString(this.relatedCode);
        dest.writeString(this.centerPic);
        dest.writeString(this.radius);
        dest.writeString(this.cpCode);
        dest.writeParcelable(this.stat, flags);
        dest.writeInt(this.duration);
    }

    protected CpProgramModel(Parcel in) {
        this.code = in.readString();
        this.score = in.readString();
        this.displayName = in.readString();
        this.subtitle = in.readString();
        this.programType = in.readString();
        this.videoType = in.readString();
        this.type = in.readString();
        this.typeName = in.readString();
        this.director = in.readString();
        this.actors = in.readString();
        this.age = in.readString();
        this.tags = in.readString();
        this.tagCode = in.readString();
        this.superscript = in.readString();
        this.language = in.readString();
        this.area = in.readString();
        this.lunboPic = in.readString();
        this.smallPic = in.readString();
        this.bigPic = in.readString();
        this.source = in.readString();
        this.description = in.readString();
        this.status = in.readInt();
        this.pubStatus = in.readInt();
        this.customUrl = in.readString();
        this.price = in.readString();
        this.isChargeable = in.readInt();
        this.relatedCode = in.readString();
        this.centerPic = in.readString();
        this.radius = in.readString();
        this.cpCode = in.readString();
        this.stat = in.readParcelable(StatQueryModel.class.getClassLoader());
        this.duration = in.readInt();
    }

    public static final Parcelable.Creator<CpProgramModel> CREATOR = new Parcelable.Creator<CpProgramModel>() {
        @Override
        public CpProgramModel createFromParcel(Parcel source) {
            return new CpProgramModel(source);
        }

        @Override
        public CpProgramModel[] newArray(int size) {
            return new CpProgramModel[size];
        }
    };
}
