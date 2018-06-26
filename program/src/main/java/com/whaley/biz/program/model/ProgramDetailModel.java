package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;


import com.whaley.biz.program.constants.VideoType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: qxw
 * Date: 2016/11/8
 * 视频详情页
 */

public class ProgramDetailModel extends BaseModel implements Serializable ,Parcelable{

    public static final String TYPE_FOOTBALL = "football";

    private String code;
    private String parentCode;
    private String score;
    private int curEpisode;
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
    private int duration;
    private int totalEpisode;
    private List<MediaModel> mediaDtos;
    private List<MediaModel> downloadDtos;
    private List<MediaModel> medias;
    private StatQueryModel stat;
    private int isChargeable;
    private String price;
    private String relatedCode;
    private List<SeriesModel> series;

    private String cpCode;
    private String name;
    private String headPic;
    private int fansCount;
    private int isFollow;
    private long disableTimeDate;
    private int freeTime;
    private List<PackageModel> contentPackageQueryDtos;
    private CouponModel couponDto;
    private String behavior;

    private String bgPic;
    private String vipPic;

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public CouponModel getCouponDto() {
        return couponDto;
    }

    public void setCouponDto(CouponModel couponDto) {
        this.couponDto = couponDto;
    }

    public long getDisableTimeDate() {
        return disableTimeDate;
    }

    public void setDisableTimeDate(long disableTimeDate) {
        this.disableTimeDate = disableTimeDate;
    }


    public String getCpCode() {
        return cpCode;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }


    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getHeadPic() {
        return headPic;
    }

    public List<SeriesModel> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesModel> series) {
        this.series = series;
    }

    public int getCurEpisode() {
        return curEpisode;
    }

    public void setCurEpisode(int curEpisode) {
        this.curEpisode = curEpisode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
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

    public List<MediaModel> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaModel> medias) {
        this.medias = medias;
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
        if(videoType!=null && videoType.equals(VideoType.VIDEO_TYPE_MORETV_2D) && getType().equals("tv")){
            videoType = VideoType.VIDEO_TYPE_MORETV_TV;
            return videoType;
        }
        return videoType;
    }

    public String getVideoFormat(){
        if(videoType!=null&&(videoType.equals(VideoType.VIDEO_TYPE_MORETV_2D)||videoType.equals(VideoType.VIDEO_TYPE_MORETV_TV))){
            return "2d";
        }
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalEpisode() {
        return totalEpisode;
    }

    public void setTotalEpisode(int totalEpisode) {
        this.totalEpisode = totalEpisode;
    }

    public List<MediaModel> getMediaDtos() {
        return mediaDtos;
    }

    public void setMediaDtos(List<MediaModel> mediaDtos) {
        this.mediaDtos = mediaDtos;
    }

    public List<MediaModel> getDownloadDtos() {
        return downloadDtos;
    }

    public void setDownloadDtos(List<MediaModel> downloadDtos) {
        this.downloadDtos = downloadDtos;
    }

    public StatQueryModel getStat() {
        return stat;
    }

    public void setStat(StatQueryModel stat) {
        this.stat = stat;
    }

    public ProgramDetailModel() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRelatedCode() {
        return relatedCode;
    }

    public void setRelatedCode(String relatedCode) {
        this.relatedCode = relatedCode;
    }

    public List<PackageModel> getContentPackageQueryDtos() {
        return contentPackageQueryDtos;
    }

    public void setContentPackageQueryDtos(List<PackageModel> contentPackageQueryDtos) {
        this.contentPackageQueryDtos = contentPackageQueryDtos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.code);
        dest.writeString(this.parentCode);
        dest.writeString(this.score);
        dest.writeInt(this.curEpisode);
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
        dest.writeInt(this.duration);
        dest.writeInt(this.totalEpisode);
        dest.writeTypedList(this.mediaDtos);
        dest.writeTypedList(this.downloadDtos);
        dest.writeTypedList(this.medias);
        dest.writeParcelable(this.stat, flags);
        dest.writeInt(this.isChargeable);
        dest.writeString(this.price);
        dest.writeString(this.relatedCode);
        dest.writeList(this.series);
        dest.writeString(this.cpCode);
        dest.writeString(this.name);
        dest.writeString(this.headPic);
        dest.writeInt(this.fansCount);
        dest.writeInt(this.isFollow);
        dest.writeLong(this.disableTimeDate);
        dest.writeInt(this.freeTime);
        dest.writeTypedList(this.contentPackageQueryDtos);
        dest.writeParcelable(this.couponDto, flags);
        dest.writeString(this.behavior);
        dest.writeString(this.bgPic);
        dest.writeString(this.vipPic);
    }

    protected ProgramDetailModel(Parcel in) {
        super(in);
        this.code = in.readString();
        this.parentCode = in.readString();
        this.score = in.readString();
        this.curEpisode = in.readInt();
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
        this.duration = in.readInt();
        this.totalEpisode = in.readInt();
        this.mediaDtos = in.createTypedArrayList(MediaModel.CREATOR);
        this.downloadDtos = in.createTypedArrayList(MediaModel.CREATOR);
        this.medias = in.createTypedArrayList(MediaModel.CREATOR);
        this.stat = in.readParcelable(StatQueryModel.class.getClassLoader());
        this.isChargeable = in.readInt();
        this.price = in.readString();
        this.relatedCode = in.readString();
        this.series = new ArrayList<SeriesModel>();
        in.readList(this.series, SeriesModel.class.getClassLoader());
        this.cpCode = in.readString();
        this.name = in.readString();
        this.headPic = in.readString();
        this.fansCount = in.readInt();
        this.isFollow = in.readInt();
        this.disableTimeDate = in.readLong();
        this.freeTime = in.readInt();
        this.contentPackageQueryDtos = in.createTypedArrayList(PackageModel.CREATOR);
        this.couponDto = in.readParcelable(CouponModel.class.getClassLoader());
        this.behavior = in.readString();
        this.bgPic = in.readString();
        this.vipPic = in.readString();
    }

    public static final Creator<ProgramDetailModel> CREATOR = new Creator<ProgramDetailModel>() {
        @Override
        public ProgramDetailModel createFromParcel(Parcel source) {
            return new ProgramDetailModel(source);
        }

        @Override
        public ProgramDetailModel[] newArray(int size) {
            return new ProgramDetailModel[size];
        }
    };

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getVipPic() {
        return vipPic;
    }

    public void setVipPic(String vipPic) {
        this.vipPic = vipPic;
    }

    public String getBgPic() {
        return bgPic;
    }

    public void setBgPic(String bgPic) {
        this.bgPic = bgPic;
    }
}
