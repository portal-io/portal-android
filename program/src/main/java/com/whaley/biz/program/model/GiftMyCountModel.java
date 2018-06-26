package com.whaley.biz.program.model;

import java.util.List;

/**
 * Created by YangZhi on 2017/10/12 16:14.
 */

public class GiftMyCountModel {


    /**
     * uid : 123123
     * nickName : 昵称
     * userHeadUrl : https://www.baidu.com
     * totalRank : 1
     * liveCount : 6
     * giftCount : 6
     * whaleyCurrentCount : 60
     * favoriteMemberList : [{"memberCode":"code002","memberName":"名称2","memberHeadUrl":"https://www.baidu.com","favoriteRank":0},{"memberCode":"code003","memberName":"名称3","memberHeadUrl":"https://www.baidu.com","favoriteRank":1},{"memberCode":"code001","memberName":"名称1","memberHeadUrl":"https://www.baidu.com","favoriteRank":2}]
     */

    private String uid;
    private String nickName;
    private String userHeadUrl;
    private int totalRank;
    private int liveCount;
    private int giftCount;
    private int whaleyCurrentCount;
    private List<FavoriteMemberModel> favoriteMemberList;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    public int getTotalRank() {
        return totalRank;
    }

    public void setTotalRank(int totalRank) {
        this.totalRank = totalRank;
    }

    public int getLiveCount() {
        return liveCount;
    }

    public void setLiveCount(int liveCount) {
        this.liveCount = liveCount;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    public int getWhaleyCurrentCount() {
        return whaleyCurrentCount;
    }

    public void setWhaleyCurrentCount(int whaleyCurrentCount) {
        this.whaleyCurrentCount = whaleyCurrentCount;
    }

    public List<FavoriteMemberModel> getFavoriteMemberList() {
        return favoriteMemberList;
    }

    public void setFavoriteMemberList(List<FavoriteMemberModel> favoriteMemberList) {
        this.favoriteMemberList = favoriteMemberList;
    }

    public static class FavoriteMemberModel {
        /**
         * memberCode : code002
         * memberName : 名称2
         * memberHeadUrl : https://www.baidu.com
         * favoriteRank : 0
         */

        private String memberCode;
        private String memberName;
        private String memberHeadUrl;
        private int favoriteRank;

        public String getMemberCode() {
            return memberCode;
        }

        public void setMemberCode(String memberCode) {
            this.memberCode = memberCode;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberHeadUrl() {
            return memberHeadUrl;
        }

        public void setMemberHeadUrl(String memberHeadUrl) {
            this.memberHeadUrl = memberHeadUrl;
        }

        public int getFavoriteRank() {
            return favoriteRank;
        }

        public void setFavoriteRank(int favoriteRank) {
            this.favoriteRank = favoriteRank;
        }
    }
}
