package com.whaley.biz.program.model;

import java.util.List;

/**
 * Created by YangZhi on 2017/10/12 16:09.
 */

public class GiftMemberCountModel {

    /**
     * liveCode : 519f52b1c87a40b19096e7ecc95bec9f
     * memberCountList : [{"memberCode":"code002","memberName":"自动化测试002","memberHeadUrl":"http://test-image.tvmore.com.cn/image/get-image/10000004/15021582801713255731.jpg/zoom/1680/1050","userCount":4,"whaleyCurrentCount":40,"rank":1,"userList":[{"uid":"1231321","nickName":"昵称","userHeadUrl":"https://www.baidu.com"}]},{"memberCode":"code003","memberName":"自动化测试003","memberHeadUrl":"http://test-image.tvmore.com.cn/image/get-image/10000004/1502158310308438029.jpg/zoom/800/551","userCount":2,"whaleyCurrentCount":20,"rank":2,"userList":[{"uid":"1231321","nickName":"昵称","userHeadUrl":"https://www.baidu.com"}]},{"memberCode":"自动化测试001","memberName":"自动化测试001","memberHeadUrl":"http://test-image.tvmore.com.cn/image/get-image/10000004/15021582571329373692.jpg/zoom/1920/1080","userCount":1,"whaleyCurrentCount":10,"rank":3,"userList":[{"uid":"1231321","nickName":"昵称","userHeadUrl":"https://www.baidu.com"}]},{"memberCode":"123331234","memberName":"123331234Name","memberHeadUrl":"http://test-image.tvmore.com.cn/image/get-image/10000004/15014857722696839063.jpg/zoom/1024/768","userCount":0,"whaleyCurrentCount":0,"rank":0,"userList":null}]
     */

    private String liveCode;
    private List<MemberCountModel> memberCountList;

    public String getLiveCode() {
        return liveCode;
    }

    public void setLiveCode(String liveCode) {
        this.liveCode = liveCode;
    }

    public List<MemberCountModel> getMemberCountList() {
        return memberCountList;
    }

    public void setMemberCountList(List<MemberCountModel> memberCountList) {
        this.memberCountList = memberCountList;
    }

    public static class MemberCountModel {
        /**
         * memberCode : code002
         * memberName : 自动化测试002
         * memberHeadUrl : http://test-image.tvmore.com.cn/image/get-image/10000004/15021582801713255731.jpg/zoom/1680/1050
         * userCount : 4
         * whaleyCurrentCount : 40
         * rank : 1
         * userList : [{"uid":"1231321","nickName":"昵称","userHeadUrl":"https://www.baidu.com"}]
         */

        private String memberCode;
        private String memberName;
        private String memberHeadUrl;
        private int userCount;
        private int whaleyCurrentCount;
        private int rank;
        private List<UserModel> userList;

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

        public int getUserCount() {
            return userCount;
        }

        public void setUserCount(int userCount) {
            this.userCount = userCount;
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

        public List<UserModel> getUserList() {
            return userList;
        }

        public void setUserList(List<UserModel> userList) {
            this.userList = userList;
        }

        public static class UserModel {
            /**
             * uid : 1231321
             * nickName : 昵称
             * userHeadUrl : https://www.baidu.com
             */

            private String uid;
            private String nickName;
            private String userHeadUrl;

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
        }
    }
}
