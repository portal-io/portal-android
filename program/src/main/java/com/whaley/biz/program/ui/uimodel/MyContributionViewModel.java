package com.whaley.biz.program.ui.uimodel;

import java.util.List;

/**
 * Created by YangZhi on 2017/10/13 15:41.
 */

public class MyContributionViewModel {

    private String imageUrl;

    private CharSequence rank;

    private CharSequence liveCount;

    private CharSequence giftCount;

    private CharSequence wcoinCount;

    private List<UserModel> favoriteUsers;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public CharSequence getRank() {
        return rank;
    }

    public void setRank(CharSequence rank) {
        this.rank = rank;
    }

    public CharSequence getLiveCount() {
        return liveCount;
    }

    public void setLiveCount(CharSequence liveCount) {
        this.liveCount = liveCount;
    }

    public CharSequence getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(CharSequence giftCount) {
        this.giftCount = giftCount;
    }

    public CharSequence getWcoinCount() {
        return wcoinCount;
    }

    public void setWcoinCount(CharSequence wcoinCount) {
        this.wcoinCount = wcoinCount;
    }

    public List<UserModel> getFavoriteUsers() {
        return favoriteUsers;
    }

    public void setFavoriteUsers(List<UserModel> favoriteUsers) {
        this.favoriteUsers = favoriteUsers;
    }

    public static class UserModel{
        private String imageUrl;

        private CharSequence userName;

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public CharSequence getUserName() {
            return userName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setUserName(CharSequence userName) {
            this.userName = userName;
        }
    }
}
