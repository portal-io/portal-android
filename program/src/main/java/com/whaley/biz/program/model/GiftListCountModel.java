package com.whaley.biz.program.model;

import java.util.List;

/**
 * Created by YangZhi on 2017/10/12 16:00.
 */

public class GiftListCountModel {

    /**
     * liveCode : 519f52b1c87a40b19096e7ecc95bec9f
     * userCountList : [{"uid":"519f52b1c87a40b19096e7ecc95123","nickName":"昵称1","userHeadUrl":"https://www.baidu.com","whaleyCurrentCount":60,"rank":1},{"uid":"519f52b1c87a40b19096e7ecc95123","nickName":"昵称2","userHeadUrl":"https://www.baidu.com","whaleyCurrentCount":10,"rank":2}]
     */

    private String liveCode;
    private List<UserCountModel> userCountList;

    public String getLiveCode() {
        return liveCode;
    }

    public void setLiveCode(String liveCode) {
        this.liveCode = liveCode;
    }

    public List<UserCountModel> getUserCountList() {
        return userCountList;
    }

    public void setUserCountList(List<UserCountModel> userCountList) {
        this.userCountList = userCountList;
    }

    public static class UserCountModel {
        /**
         * uid : 519f52b1c87a40b19096e7ecc95123
         * nickName : 昵称1
         * userHeadUrl : https://www.baidu.com
         * whaleyCurrentCount : 60
         * rank : 1
         */

        private String uid;
        private String nickName;
        private String userHeadUrl;
        private int whaleyCurrentCount;
        private int rank;

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

        public int getWhaleyCurrentCount() {
            return whaleyCurrentCount;
        }

        public void setWhaleyCurrentCount(int whaleyCurrentCount) {
            this.whaleyCurrentCount = whaleyCurrentCount;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }
}
