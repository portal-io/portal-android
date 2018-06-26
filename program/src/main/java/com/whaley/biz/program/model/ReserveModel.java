package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.playerui.model.DataBuilder;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;

import java.util.List;

/**
 * Created by dell on 2017/8/21.
 */

public class ReserveModel implements Parcelable {

    private int id;
    private String programType;
    private String videoType;
    private String code;
    private String address;
    private String displayName;
    private String type;
    private String typeName;
    private String subtitle;
    private String superscript;
    private String poster;
    private String pic;
    private String description;
    private String customUrl;
    private int liveStatus;
    private int status;
    private String source;
    private long beginTime;
    private long endTime;
    private long createTime;
    private long updateTime;
    private long publishTime;
    private StatQueryModel stat;
    private int liveOrderCount;
    private String price;
    private int isChargeable;
    private int isDanmu;
    private int isLottery;
    private String centerPic;
    private String radius;
    private boolean liveOrdered;
    private int timeLeftSeconds;
    private List<LiveGuestModel> guests;
    private List<LiveMediaModel> liveMediaDtos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSuperscript() {
        return superscript;
    }

    public void setSuperscript(String superscript) {
        this.superscript = superscript;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public StatQueryModel getStat() {
        return stat;
    }

    public void setStat(StatQueryModel stat) {
        this.stat = stat;
    }

    public int getLiveOrderCount() {
        return liveOrderCount;
    }

    public void setLiveOrderCount(int liveOrderCount) {
        this.liveOrderCount = liveOrderCount;
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

    public int getIsDanmu() {
        return isDanmu;
    }

    public void setIsDanmu(int isDanmu) {
        this.isDanmu = isDanmu;
    }

    public int getIsLottery() {
        return isLottery;
    }

    public void setIsLottery(int isLottery) {
        this.isLottery = isLottery;
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

    public boolean isLiveOrdered() {
        return liveOrdered;
    }

    public void setLiveOrdered(boolean liveOrdered) {
        this.liveOrdered = liveOrdered;
    }

    public int getTimeLeftSeconds() {
        return timeLeftSeconds;
    }

    public void setTimeLeftSeconds(int timeLeftSeconds) {
        this.timeLeftSeconds = timeLeftSeconds;
    }

    public List<LiveGuestModel> getGuests() {
        return guests;
    }

    public void setGuests(List<LiveGuestModel> guests) {
        this.guests = guests;
    }

    public List<LiveMediaModel> getLiveMediaDtos() {
        return liveMediaDtos;
    }

    public void setLiveMediaDtos(List<LiveMediaModel> liveMediaDtos) {
        this.liveMediaDtos = liveMediaDtos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.programType);
        dest.writeString(this.videoType);
        dest.writeString(this.code);
        dest.writeString(this.address);
        dest.writeString(this.displayName);
        dest.writeString(this.type);
        dest.writeString(this.typeName);
        dest.writeString(this.subtitle);
        dest.writeString(this.superscript);
        dest.writeString(this.poster);
        dest.writeString(this.pic);
        dest.writeString(this.description);
        dest.writeString(this.customUrl);
        dest.writeInt(this.liveStatus);
        dest.writeInt(this.status);
        dest.writeString(this.source);
        dest.writeLong(this.beginTime);
        dest.writeLong(this.endTime);
        dest.writeLong(this.createTime);
        dest.writeLong(this.updateTime);
        dest.writeLong(this.publishTime);
        dest.writeParcelable(this.stat, 0);
        dest.writeInt(this.liveOrderCount);
        dest.writeString(this.price);
        dest.writeInt(this.isChargeable);
        dest.writeInt(this.isDanmu);
        dest.writeInt(this.isLottery);
        dest.writeString(this.centerPic);
        dest.writeString(this.radius);
        dest.writeByte(liveOrdered ? (byte) 1 : (byte) 0);
        dest.writeInt(this.timeLeftSeconds);
        dest.writeTypedList(guests);
        dest.writeTypedList(liveMediaDtos);
    }

    public ReserveModel() {
    }

    protected ReserveModel(Parcel in) {
        this.id = in.readInt();
        this.programType = in.readString();
        this.videoType = in.readString();
        this.code = in.readString();
        this.address = in.readString();
        this.displayName = in.readString();
        this.type = in.readString();
        this.typeName = in.readString();
        this.subtitle = in.readString();
        this.superscript = in.readString();
        this.poster = in.readString();
        this.pic = in.readString();
        this.description = in.readString();
        this.customUrl = in.readString();
        this.liveStatus = in.readInt();
        this.status = in.readInt();
        this.source = in.readString();
        this.beginTime = in.readLong();
        this.endTime = in.readLong();
        this.createTime = in.readLong();
        this.updateTime = in.readLong();
        this.publishTime = in.readLong();
        this.stat = in.readParcelable(StatQueryModel.class.getClassLoader());
        this.liveOrderCount = in.readInt();
        this.price = in.readString();
        this.isChargeable = in.readInt();
        this.isDanmu = in.readInt();
        this.isLottery = in.readInt();
        this.centerPic = in.readString();
        this.radius = in.readString();
        this.liveOrdered = in.readByte() != 0;
        this.timeLeftSeconds = in.readInt();
        this.guests = in.createTypedArrayList(LiveGuestModel.CREATOR);
        this.liveMediaDtos = in.createTypedArrayList(LiveMediaModel.CREATOR);
    }

    public static final Parcelable.Creator<ReserveModel> CREATOR = new Parcelable.Creator<ReserveModel>() {
        public ReserveModel createFromParcel(Parcel source) {
            return new ReserveModel(source);
        }

        public ReserveModel[] newArray(int size) {
            return new ReserveModel[size];
        }
    };

    public PlayData getPlayData() {
        return DataBuilder.createBuilder("", ProgramConstants.TYPE_PLAY_LIVE)
                .setId(code)
                .setTitle(displayName)
                .setMonocular(true)
                .build();
    }

}
