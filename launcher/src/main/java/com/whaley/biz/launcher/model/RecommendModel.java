package com.whaley.biz.launcher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangZhi on 2017/7/31 15:00.
 */

public class RecommendModel extends BaseModel implements Parcelable, Serializable {

    private String newPicUrl;
    private String name;
    private String code;

    /**
     * 链接类型
     */
    private String linkArrangeType;
    /**
     * 链接值
     */
    private String linkArrangeValue;
    /**
     * 角标url
     */
    private String flagUrl;
    /**
     * 角标code
     */
    private String flagCode;
    /**
     * 角标状态
     */
    private Short flagStatus;

    private int position;
    private String recommendAreaCode;
    private String recommendPageType;
    private int version;
    private String appType;

    /**
     * 版本号
     */
    private String businessVersion;
    private String subtitle;
    private String introduction;
    private String secondJumpType;
    private String secondJumpValue;
    private String secondJumpText;
    private String infAuthorImageUrl;
    private String infAuthorName;
    private String infImageUrl;
    private String infTitle;
    private String infIntroduction;
    private String infUrl;
    private String detailCount;
    private String logoImageUrl;
    private String type;
    private int programPlayTime;
    private String overallViewImgUrl;
    private String videoType;
    private String videoUrl;
    private String programMark;
    private String liveStatus;
    private int isChargeable;
    private String price;
    private String renderType;
    private List<String> recommendAreaCodes;
    private int arrangeShowFlag;
    private int arrangeShowNum;
    private String arrangeShowType;
    private List<ArrangeModel> arrangeElements;
    private long liveBeginTime;
    private CpInfoModel cpInfo;
    private List<CpProgramModel> cpProgramDtos;
    private int cpFollow;
    private long disableTimeDate;
    private int freeTime;
    private PackageModel contentPackageDto;
    private int displayMode;
    private String contentType;
    private int showRate;

    public int getShowRate() {
        return showRate;
    }

    public void setShowRate(int showRate) {
        this.showRate = showRate;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public void setLiveBeginTime(long liveBeginTime) {
        this.liveBeginTime = liveBeginTime;
    }

    public long getLiveBeginTime() {
        return liveBeginTime;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    public int getLiveOrderCount() {
        return liveOrderCount;
    }

    public void setLiveOrderCount(int liveOrderCount) {
        this.liveOrderCount = liveOrderCount;
    }

    public String getRecommendPageType() {
        return recommendPageType;
    }

    public void setRecommendPageType(String recommendPageType) {
        this.recommendPageType = recommendPageType;
    }

    private int liveOrderCount;
    private StatQueryModel statQueryDto;

    public void setRecommendAreaCodes(List<String> recommendAreaCodes) {
        this.recommendAreaCodes = recommendAreaCodes;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    /**
     * 区分推荐位字段(自己本地数据)
     */
    private String recommendAreaType;

    public String getRecommendAreaType() {
        return recommendAreaType;
    }

    public void setRecommendAreaType(String recommendAreaType) {
        this.recommendAreaType = recommendAreaType;
    }

    public StatQueryModel getStatQueryDto() {
        return statQueryDto;
    }

    public void setStatQueryDto(StatQueryModel statQueryDto) {
        this.statQueryDto = statQueryDto;
    }

    public String getLogoImageUrl() {
        return logoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getProgramPlayTime() {
        return programPlayTime;
    }

    public void setProgramPlayTime(int programPlayTime) {
        this.programPlayTime = programPlayTime;
    }

    public String getOverallViewImgUrl() {
        return overallViewImgUrl;
    }

    public void setOverallViewImgUrl(String overallViewImgUrl) {
        this.overallViewImgUrl = overallViewImgUrl;
    }

    public List<String> getRecommendAreaCodes() {
        return recommendAreaCodes;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getProgramMark() {
        return programMark;
    }

    public void setProgramMark(String programMark) {
        this.programMark = programMark;
    }

    public String getNewPicUrl() {
        return newPicUrl;
    }

    public void setNewPicUrl(String newPicUrl) {
        this.newPicUrl = newPicUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCpFollow() {
        return cpFollow;
    }

    public void setCpFollow(int cpFollow) {
        this.cpFollow = cpFollow;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLinkArrangeType() {
        return linkArrangeType;
    }

    public void setLinkArrangeType(String linkArrangeType) {
        this.linkArrangeType = linkArrangeType;
    }

    public String getLinkArrangeValue() {
        return linkArrangeValue;
    }

    public void setLinkArrangeValue(String linkArrangeValue) {
        this.linkArrangeValue = linkArrangeValue;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public String getFlagCode() {
        return flagCode;
    }

    public void setFlagCode(String flagCode) {
        this.flagCode = flagCode;
    }

    public Short getFlagStatus() {
        return flagStatus;
    }

    public void setFlagStatus(Short flagStatus) {
        this.flagStatus = flagStatus;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getRecommendAreaCode() {
        return recommendAreaCode;
    }

    public void setRecommendAreaCode(String recommendAreaCode) {
        this.recommendAreaCode = recommendAreaCode;
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getBusinessVersion() {
        return businessVersion;
    }

    public void setBusinessVersion(String businessVersion) {
        this.businessVersion = businessVersion;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSecondJumpType() {
        return secondJumpType;
    }

    public void setSecondJumpType(String secondJumpType) {
        this.secondJumpType = secondJumpType;
    }

    public String getSecondJumpValue() {
        return secondJumpValue;
    }

    public void setSecondJumpValue(String secondJumpValue) {
        this.secondJumpValue = secondJumpValue;
    }

    public String getSecondJumpText() {
        return secondJumpText;
    }

    public void setSecondJumpText(String secondJumpText) {
        this.secondJumpText = secondJumpText;
    }

    public String getInfAuthorImageUrl() {
        return infAuthorImageUrl;
    }

    public void setInfAuthorImageUrl(String infAuthorImageUrl) {
        this.infAuthorImageUrl = infAuthorImageUrl;
    }

    public String getInfAuthorName() {
        return infAuthorName;
    }

    public void setInfAuthorName(String infAuthorName) {
        this.infAuthorName = infAuthorName;
    }

    public String getInfImageUrl() {
        return infImageUrl;
    }

    public void setInfImageUrl(String infImageUrl) {
        this.infImageUrl = infImageUrl;
    }

    public String getInfTitle() {
        return infTitle;
    }

    public void setInfTitle(String infTitle) {
        this.infTitle = infTitle;
    }

    public String getInfIntroduction() {
        return infIntroduction;
    }

    public void setInfIntroduction(String infIntroduction) {
        this.infIntroduction = infIntroduction;
    }

    public String getInfUrl() {
        return infUrl;
    }

    public void setInfUrl(String infUrl) {
        this.infUrl = infUrl;
    }

    public String getDetailCount() {
        return detailCount;
    }

    public void setDetailCount(String detailCount) {
        this.detailCount = detailCount;
    }

    public CpInfoModel getCpInfo() {
        return cpInfo;
    }

    public void setCpInfo(CpInfoModel cpInfo) {
        this.cpInfo = cpInfo;
    }

    public List<CpProgramModel> getCpProgramDtos() {
        return cpProgramDtos;
    }

    public void setCpProgramDtos(List<CpProgramModel> cpProgramDtos) {
        this.cpProgramDtos = cpProgramDtos;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public RecommendModel() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }


    public int getArrangeShowFlag() {
        return arrangeShowFlag;
    }

    public void setArrangeShowFlag(int arrangeShowFlag) {
        this.arrangeShowFlag = arrangeShowFlag;
    }

    public int getArrangeShowNum() {
        return arrangeShowNum;
    }

    public void setArrangeShowNum(int arrangeShowNum) {
        this.arrangeShowNum = arrangeShowNum;
    }

    public String getArrangeShowType() {
        return arrangeShowType;
    }

    public void setArrangeShowType(String arrangeShowType) {
        this.arrangeShowType = arrangeShowType;
    }

    public List<ArrangeModel> getArrangeElements() {
        return arrangeElements;
    }

    public void setArrangeElements(List<ArrangeModel> arrangeElements) {
        this.arrangeElements = arrangeElements;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.newPicUrl);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.linkArrangeType);
        dest.writeString(this.linkArrangeValue);
        dest.writeString(this.flagUrl);
        dest.writeString(this.flagCode);
        dest.writeValue(this.flagStatus);
        dest.writeInt(this.position);
        dest.writeString(this.recommendAreaCode);
        dest.writeString(this.recommendPageType);
        dest.writeInt(this.version);
        dest.writeString(this.appType);
        dest.writeString(this.businessVersion);
        dest.writeString(this.subtitle);
        dest.writeString(this.introduction);
        dest.writeString(this.secondJumpType);
        dest.writeString(this.secondJumpValue);
        dest.writeString(this.secondJumpText);
        dest.writeString(this.infAuthorImageUrl);
        dest.writeString(this.infAuthorName);
        dest.writeString(this.infImageUrl);
        dest.writeString(this.infTitle);
        dest.writeString(this.infIntroduction);
        dest.writeString(this.infUrl);
        dest.writeString(this.detailCount);
        dest.writeString(this.logoImageUrl);
        dest.writeString(this.type);
        dest.writeInt(this.programPlayTime);
        dest.writeString(this.overallViewImgUrl);
        dest.writeString(this.videoType);
        dest.writeString(this.videoUrl);
        dest.writeString(this.programMark);
        dest.writeString(this.liveStatus);
        dest.writeInt(this.isChargeable);
        dest.writeString(this.price);
        dest.writeString(this.renderType);
        dest.writeStringList(this.recommendAreaCodes);
        dest.writeInt(this.arrangeShowFlag);
        dest.writeInt(this.arrangeShowNum);
        dest.writeString(this.arrangeShowType);
        dest.writeTypedList(this.arrangeElements);
        dest.writeLong(this.liveBeginTime);
        dest.writeParcelable(this.cpInfo, flags);
        dest.writeTypedList(this.cpProgramDtos);
        dest.writeInt(this.liveOrderCount);
        dest.writeParcelable(this.statQueryDto, flags);
        dest.writeString(this.recommendAreaType);
        dest.writeLong(this.disableTimeDate);
        dest.writeInt(this.freeTime);
        dest.writeParcelable(contentPackageDto, flags);
        dest.writeInt(this.displayMode);
        dest.writeString(this.contentType);
    }

    protected RecommendModel(Parcel in) {
        super(in);
        this.newPicUrl = in.readString();
        this.name = in.readString();
        this.code = in.readString();
        this.linkArrangeType = in.readString();
        this.linkArrangeValue = in.readString();
        this.flagUrl = in.readString();
        this.flagCode = in.readString();
        this.flagStatus = (Short) in.readValue(Short.class.getClassLoader());
        this.position = in.readInt();
        this.recommendAreaCode = in.readString();
        this.recommendPageType = in.readString();
        this.version = in.readInt();
        this.appType = in.readString();
        this.businessVersion = in.readString();
        this.subtitle = in.readString();
        this.introduction = in.readString();
        this.secondJumpType = in.readString();
        this.secondJumpValue = in.readString();
        this.secondJumpText = in.readString();
        this.infAuthorImageUrl = in.readString();
        this.infAuthorName = in.readString();
        this.infImageUrl = in.readString();
        this.infTitle = in.readString();
        this.infIntroduction = in.readString();
        this.infUrl = in.readString();
        this.detailCount = in.readString();
        this.logoImageUrl = in.readString();
        this.type = in.readString();
        this.programPlayTime = in.readInt();
        this.overallViewImgUrl = in.readString();
        this.videoType = in.readString();
        this.videoUrl = in.readString();
        this.programMark = in.readString();
        this.liveStatus = in.readString();
        this.isChargeable = in.readInt();
        this.price = in.readString();
        this.renderType = in.readString();
        this.recommendAreaCodes = in.createStringArrayList();
        this.arrangeShowFlag = in.readInt();
        this.arrangeShowNum = in.readInt();
        this.arrangeShowType = in.readString();
        this.arrangeElements = in.createTypedArrayList(ArrangeModel.CREATOR);
        this.liveBeginTime = in.readLong();
        this.cpInfo = in.readParcelable(CpInfoModel.class.getClassLoader());
        this.cpProgramDtos = in.createTypedArrayList(CpProgramModel.CREATOR);
        this.liveOrderCount = in.readInt();
        this.statQueryDto = in.readParcelable(StatQueryModel.class.getClassLoader());
        this.recommendAreaType = in.readString();
        this.disableTimeDate = in.readLong();
        this.freeTime = in.readInt();
        this.contentPackageDto = in.readParcelable(PackageModel.class.getClassLoader());
        this.displayMode = in.readInt();
        this.contentType = in.readString();
    }

    public static final Creator<RecommendModel> CREATOR = new Creator<RecommendModel>() {
        @Override
        public RecommendModel createFromParcel(Parcel source) {
            return new RecommendModel(source);
        }

        @Override
        public RecommendModel[] newArray(int size) {
            return new RecommendModel[size];
        }
    };

    public long getDisableTimeDate() {
        return disableTimeDate;
    }

    public void setDisableTimeDate(long disableTimeDate) {
        this.disableTimeDate = disableTimeDate;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public PackageModel getContentPackageQueryDtos() {
        return contentPackageDto;
    }

    public void setContentPackageQueryDtos(PackageModel contentPackageDto) {
        this.contentPackageDto = contentPackageDto;
    }

}
