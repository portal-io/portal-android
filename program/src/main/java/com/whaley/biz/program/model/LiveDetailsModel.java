package com.whaley.biz.program.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.model.base.BaseModel;
import com.whaley.biz.playerui.model.DataBuilder;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.core.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2017/8/14.
 */

public class LiveDetailsModel extends BaseModel implements Parcelable, Serializable {

    public static final String TYPE_FOOTBALL = "live_football";
    public static final String TYPE_FUN = "live_fun";
    public static final String TYPE_SHOW = "live_show";

    private String code;
    private String address;

    private List<LiveGuestModel> guests;

    private String displayName;

    private String type;

    private int displayMode;  // 1（横屏）；0（竖屏）

    private String subtitle;

    private String superscript;

    private String poster;

    private String pic;

    private String description;

    private int liveStatus;

    private int pubStatus;

    private String source;

    private long beginTime;

    private long endTime;

    private List<LiveMediaModel> liveMediaDtos;

    private StatQueryModel stat;

    private int liveOrderCount;

    private int hasOrder;

    private int orderNum;

    private int isChargeable;

    private String price;

    private boolean liveOrdered;

    private int timeLeftSeconds;
    private int freeTime;
    private String behavior;

    private String bgPic;

    private String vipPic;

    private int isGift;

    private int isTip;

    private String giftTemplate;
    private String memberTemplate;

    private List<PackageModel> contentPackageQueryDtos;

    private CouponModel couponDto;

    private String floatName1;
    private String floatName2;
    private String floatName3;

    private String floatPic1;
    private String floatPic2;
    private String floatPic3;

    private String floatUrl1;
    private String floatUrl2;
    private String floatUrl3;

    public String getFloatName1() {
        return floatName1;
    }

    public void setFloatName1(String floatName1) {
        this.floatName1 = floatName1;
    }

    public String getFloatName2() {
        return floatName2;
    }

    public void setFloatName2(String floatName2) {
        this.floatName2 = floatName2;
    }

    public String getFloatName3() {
        return floatName3;
    }

    public void setFloatName3(String floatName3) {
        this.floatName3 = floatName3;
    }

    public String getFloatPic1() {
        return floatPic1;
    }

    public void setFloatPic1(String floatPic1) {
        this.floatPic1 = floatPic1;
    }

    public String getFloatPic2() {
        return floatPic2;
    }

    public void setFloatPic2(String floatPic2) {
        this.floatPic2 = floatPic2;
    }

    public String getFloatPic3() {
        return floatPic3;
    }

    public void setFloatPic3(String floatPic3) {
        this.floatPic3 = floatPic3;
    }

    public String getFloatUrl1() {
        return floatUrl1;
    }

    public void setFloatUrl1(String floatUrl1) {
        this.floatUrl1 = floatUrl1;
    }

    public String getFloatUrl2() {
        return floatUrl2;
    }

    public void setFloatUrl2(String floatUrl2) {
        this.floatUrl2 = floatUrl2;
    }

    public String getFloatUrl3() {
        return floatUrl3;
    }

    public void setFloatUrl3(String floatUrl3) {
        this.floatUrl3 = floatUrl3;
    }

    public String getMemberTemplate() {
        return memberTemplate;
    }

    public void setMemberTemplate(String memberTemplate) {
        this.memberTemplate = memberTemplate;
    }

    public CouponModel getCouponDto() {
        return couponDto;
    }

    public void setCouponDto(CouponModel couponDto) {
        this.couponDto = couponDto;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public int getIsTip() {
        return isTip;
    }

    public boolean isTip() {
        return (isTip == 1 && !StrUtil.isEmpty(memberTemplate));
    }

    public void setIsTip(int isTip) {
        this.isTip = isTip;
    }

    public String getBgPic() {
        return bgPic;
    }


    public String getGiftTemplate() {
        return giftTemplate;
    }

    public void setGiftTemplate(String giftTemplate) {
        this.giftTemplate = giftTemplate;
    }

    public void setBgPic(String bgPic) {
        this.bgPic = bgPic;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public int getTimeLeftSeconds() {
        return timeLeftSeconds;
    }

    public void setTimeLeftSeconds(int timeLeftSeconds) {
        this.timeLeftSeconds = timeLeftSeconds;
    }

    public boolean isLiveOrdered() {
        return liveOrdered;
    }

    public void setLiveOrdered(boolean liveOrdered) {
        this.liveOrdered = liveOrdered;
    }

    public int getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(int isChargeable) {
        this.isChargeable = isChargeable;
    }

    public int getHasOrder() {
        return hasOrder;
    }

    public void setHasOrder(int hasOrder) {
        this.hasOrder = hasOrder;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public void setPubStatus(int pubStatus) {
        this.pubStatus = pubStatus;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getLiveOrderCount() {
        return liveOrderCount;
    }

    public void addLiveOrderCount() {
        liveOrderCount = liveOrderCount + 1;
    }

    public void minusLiveOrderCount() {
        liveOrderCount = liveOrderCount - 1;
    }

    public void setLiveOrderCount(int liveOrderCount) {
        this.liveOrderCount = liveOrderCount;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public long getEndTime() {
        return endTime;
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

    public int getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
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

    public Integer getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(Integer liveStatus) {
        this.liveStatus = liveStatus;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public List<LiveMediaModel> getLiveMediaDtos() {
        return liveMediaDtos;
    }

    public void setLiveMediaDtos(List<LiveMediaModel> liveMediaDtos) {
        this.liveMediaDtos = liveMediaDtos;
    }

    public Integer getPubStatus() {
        return pubStatus;
    }

    public void setPubStatus(Integer pubStatus) {
        this.pubStatus = pubStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public List<LiveGuestModel> getGuests() {
        if (guests != null && guests.size() > 4) {
            guests = guests.subList(0, 4);
        }
        return guests;
    }

    public void setGuests(List<LiveGuestModel> guests) {
        this.guests = guests;
    }

    public StatQueryModel getStat() {
        return stat;
    }

    public void setStat(StatQueryModel stat) {
        this.stat = stat;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<PackageModel> getContentPackageQueryDtos() {
        return contentPackageQueryDtos;
    }

    public void setContentPackageQueryDtos(List<PackageModel> contentPackageQueryDtos) {
        this.contentPackageQueryDtos = contentPackageQueryDtos;
    }

    public String getVipPic() {
        return vipPic;
    }

    public void setVipPic(String vipPic) {
        this.vipPic = vipPic;
    }

    public PlayData getPlayData() {
        return DataBuilder.createBuilder("", ProgramConstants.TYPE_PLAY_LIVE)
                .setId(code)
                .setTitle(displayName)
                .setMonocular(true)
                .putCustomData(PlayerDataConstants.LIVE_INIT_BACKGROUND_IMAGE, poster)
                .putCustomData(PlayerDataConstants.ORIENTATION_IS_LANDSCAPE, getDisplayMode() == 1)
                .build();
    }

    public LiveDetailsModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.code);
        dest.writeString(this.address);
        dest.writeTypedList(this.guests);
        dest.writeString(this.displayName);
        dest.writeString(this.type);
        dest.writeInt(this.displayMode);
        dest.writeString(this.subtitle);
        dest.writeString(this.superscript);
        dest.writeString(this.poster);
        dest.writeString(this.pic);
        dest.writeString(this.description);
        dest.writeInt(this.liveStatus);
        dest.writeInt(this.pubStatus);
        dest.writeString(this.source);
        dest.writeLong(this.beginTime);
        dest.writeLong(this.endTime);
        dest.writeTypedList(this.liveMediaDtos);
        dest.writeParcelable(this.stat, flags);
        dest.writeInt(this.liveOrderCount);
        dest.writeInt(this.hasOrder);
        dest.writeInt(this.orderNum);
        dest.writeInt(this.isChargeable);
        dest.writeString(this.price);
        dest.writeByte(this.liveOrdered ? (byte) 1 : (byte) 0);
        dest.writeInt(this.timeLeftSeconds);
        dest.writeInt(this.freeTime);
        dest.writeString(this.behavior);
        dest.writeString(this.bgPic);
        dest.writeString(this.vipPic);
        dest.writeInt(this.isGift);
        dest.writeInt(this.isTip);
        dest.writeTypedList(this.contentPackageQueryDtos);
        dest.writeParcelable(this.couponDto, flags);
    }

    protected LiveDetailsModel(Parcel in) {
        super(in);
        this.code = in.readString();
        this.address = in.readString();
        this.guests = in.createTypedArrayList(LiveGuestModel.CREATOR);
        this.displayName = in.readString();
        this.type = in.readString();
        this.displayMode = in.readInt();
        this.subtitle = in.readString();
        this.superscript = in.readString();
        this.poster = in.readString();
        this.pic = in.readString();
        this.description = in.readString();
        this.liveStatus = in.readInt();
        this.pubStatus = in.readInt();
        this.source = in.readString();
        this.beginTime = in.readLong();
        this.endTime = in.readLong();
        this.liveMediaDtos = in.createTypedArrayList(LiveMediaModel.CREATOR);
        this.stat = in.readParcelable(StatQueryModel.class.getClassLoader());
        this.liveOrderCount = in.readInt();
        this.hasOrder = in.readInt();
        this.orderNum = in.readInt();
        this.isChargeable = in.readInt();
        this.price = in.readString();
        this.liveOrdered = in.readByte() != 0;
        this.timeLeftSeconds = in.readInt();
        this.freeTime = in.readInt();
        this.behavior = in.readString();
        this.bgPic = in.readString();
        this.vipPic = in.readString();
        this.isGift = in.readInt();
        this.isTip = in.readInt();
        this.contentPackageQueryDtos = in.createTypedArrayList(PackageModel.CREATOR);
        this.couponDto = in.readParcelable(CouponModel.class.getClassLoader());
    }

    public static final Creator<LiveDetailsModel> CREATOR = new Creator<LiveDetailsModel>() {
        @Override
        public LiveDetailsModel createFromParcel(Parcel source) {
            return new LiveDetailsModel(source);
        }

        @Override
        public LiveDetailsModel[] newArray(int size) {
            return new LiveDetailsModel[size];
        }
    };
}

