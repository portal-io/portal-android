package com.whaley.biz.livegift.model;


import android.content.Context;

import com.whaley.biz.livegift.R;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.StrUtil;

/**
 * Author: qxw
 * Date:2017/10/19
 * Introduction:
 */

public class GiftNoticeModle {

    /**
     * giftPic : http://store.whaley-vr.com/props/gift/2017-04-10/-16383894251491792859068
     * uid : 53051688
     * response_dateline : 1508402308
     * userHeadUrl : http://image.aginomoto.com/whaley?acid=53051688&width=0&height=0&retate=0
     * giftIcon : http://store.whaley-vr.com/props/gift/2017-04-10/-16383894251491792862359
     * memberHeadUrl :
     * nickName : 旧时明月照我心
     * liveCode : 356ffa660d3c468d9172a10ee66f1206
     * giftName : 狗尾巴草
     * giftCode : 5d978e15868b12a689220a01939ec0cd
     * memberName :
     * memberCode :
     */

    private String giftPic;
    private String uid;
    private int response_dateline;
    private String userHeadUrl;
    private String giftIcon;
    private String memberHeadUrl;
    private String nickName;
    private String liveCode;
    private String giftName;
    private String giftCode;
    private String memberName;
    private String memberCode;


    private String textConent;
    private boolean isSelf;


    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public String getTextConent() {
        return textConent;
    }

    public void setTextConent(String textConent) {
        this.textConent = textConent;
    }

    public String getGiftPic() {
        return giftPic;
    }

    public void setGiftPic(String giftPic) {
        this.giftPic = giftPic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getResponse_dateline() {
        return response_dateline;
    }

    public void setResponse_dateline(int response_dateline) {
        this.response_dateline = response_dateline;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    public String getGiftIcon() {
        return giftIcon;
    }

    public void setGiftIcon(String giftIcon) {
        this.giftIcon = giftIcon;
    }

    public String getMemberHeadUrl() {
        return memberHeadUrl;
    }

    public void setMemberHeadUrl(String memberHeadUrl) {
        this.memberHeadUrl = memberHeadUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLiveCode() {
        return liveCode;
    }

    public void setLiveCode(String liveCode) {
        this.liveCode = liveCode;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftCode() {
        return giftCode;
    }

    public void setGiftCode(String giftCode) {
        this.giftCode = giftCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }


    public boolean equals(GiftNoticeModle giftNoticeModle) {
        if (StrUtil.isEmpty(uid) || StrUtil.isEmpty(giftCode)) {
            return false;
        }
        if (!uid.equals(giftNoticeModle.getUid())) {
            return false;
        }
        if (!giftCode.equals(giftNoticeModle.getGiftCode())) {
            return false;
        }
        if (StrUtil.isEmpty(memberCode) && StrUtil.isEmpty(giftNoticeModle.memberCode)) {
            return true;
        }
        if (!StrUtil.isEmpty(memberCode) && !StrUtil.isEmpty(giftNoticeModle.memberCode) && giftCode.equals(giftNoticeModle.getGiftCode())) {
            return true;
        }
        return false;
    }

    public void saveTextContent() {
        if (StrUtil.isEmpty(memberCode)) {
            setTextConent(String.format(AppContextProvider.getInstance().getContext().getString(R.string.send_gift_name), getGiftName()));
        } else {
            setTextConent(String.format(AppContextProvider.getInstance().getContext().getString(R.string.send_reward_gift_name), getMemberName(), getGiftName()));
        }
    }
}
