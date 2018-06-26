package com.whaley.biz.longconnection.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/5/9.
 */

public class RoomModel implements Parcelable {
    /**
     * roomid : 1
     * cateid : 2
     * type : show
     * uid : 1
     * nickname : 1033
     * charm : 1477281
     * follownum : 41
     * livenum : 1
     * name : 测试房间
     * avatar :
     * title : 赶紧来吧
     * image : http://ofwioq6pq.bkt.clouddn.com/mj0105fl03.jpg
     * intro :
     * content :
     * credits1 : 494351
     * credits2 : 494351
     * votedata :
     * status : 0
     * posid : 103
     * dateline : 0
     * starttime : 0
     * order : 0
     * disabled : 0
     * memberrankcsv : 1
     * sid :
     * allowdanmaku : 1
     */

    private int roomid;
    private String cateid;
    private String type;
    private String uid;
    private String nickname;
    private String charm;
    private String follownum;
    private int livenum;
    private String name;
    private String avatar;
    private String title;
    private String image;
    private String intro;
    private String content;
    private String credits1;
    private String credits2;
    private String votedata;
    private String status;
    private String posid;
    private String dateline;
    private String starttime;
    private String order;
    private String disabled;
    private String memberrankcsv;
    private String sid;
    private String allowdanmaku;

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public void setCateid(String cateid) {
        this.cateid = cateid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCharm(String charm) {
        this.charm = charm;
    }

    public void setFollownum(String follownum) {
        this.follownum = follownum;
    }

    public void setLivenum(int livenum) {
        this.livenum = livenum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCredits1(String credits1) {
        this.credits1 = credits1;
    }

    public void setCredits2(String credits2) {
        this.credits2 = credits2;
    }

    public void setVotedata(String votedata) {
        this.votedata = votedata;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPosid(String posid) {
        this.posid = posid;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public void setMemberrankcsv(String memberrankcsv) {
        this.memberrankcsv = memberrankcsv;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setAllowdanmaku(String allowdanmaku) {
        this.allowdanmaku = allowdanmaku;
    }

    public int getRoomid() {
        return roomid;
    }

    public String getCateid() {
        return cateid;
    }

    public String getType() {
        return type;
    }

    public String getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCharm() {
        return charm;
    }

    public String getFollownum() {
        return follownum;
    }

    public int getLivenum() {
        return livenum;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getIntro() {
        return intro;
    }

    public String getContent() {
        return content;
    }

    public String getCredits1() {
        return credits1;
    }

    public String getCredits2() {
        return credits2;
    }

    public String getVotedata() {
        return votedata;
    }

    public String getStatus() {
        return status;
    }

    public String getPosid() {
        return posid;
    }

    public String getDateline() {
        return dateline;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getOrder() {
        return order;
    }

    public String getDisabled() {
        return disabled;
    }

    public String getMemberrankcsv() {
        return memberrankcsv;
    }

    public String getSid() {
        return sid;
    }

    public String getAllowdanmaku() {
        return allowdanmaku;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.roomid);
        dest.writeString(this.cateid);
        dest.writeString(this.type);
        dest.writeString(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(this.charm);
        dest.writeString(this.follownum);
        dest.writeInt(this.livenum);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.intro);
        dest.writeString(this.content);
        dest.writeString(this.credits1);
        dest.writeString(this.credits2);
        dest.writeString(this.votedata);
        dest.writeString(this.status);
        dest.writeString(this.posid);
        dest.writeString(this.dateline);
        dest.writeString(this.starttime);
        dest.writeString(this.order);
        dest.writeString(this.disabled);
        dest.writeString(this.memberrankcsv);
        dest.writeString(this.sid);
        dest.writeString(this.allowdanmaku);
    }

    public RoomModel() {
    }

    protected RoomModel(Parcel in) {
        this.roomid = in.readInt();
        this.cateid = in.readString();
        this.type = in.readString();
        this.uid = in.readString();
        this.nickname = in.readString();
        this.charm = in.readString();
        this.follownum = in.readString();
        this.livenum = in.readInt();
        this.name = in.readString();
        this.avatar = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.intro = in.readString();
        this.content = in.readString();
        this.credits1 = in.readString();
        this.credits2 = in.readString();
        this.votedata = in.readString();
        this.status = in.readString();
        this.posid = in.readString();
        this.dateline = in.readString();
        this.starttime = in.readString();
        this.order = in.readString();
        this.disabled = in.readString();
        this.memberrankcsv = in.readString();
        this.sid = in.readString();
        this.allowdanmaku = in.readString();
    }

    public static final Creator<RoomModel> CREATOR = new Creator<RoomModel>() {
        public RoomModel createFromParcel(Parcel source) {
            return new RoomModel(source);
        }

        public RoomModel[] newArray(int size) {
            return new RoomModel[size];
        }
    };
}
