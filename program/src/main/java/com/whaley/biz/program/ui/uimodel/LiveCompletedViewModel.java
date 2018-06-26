package com.whaley.biz.program.ui.uimodel;

import java.util.List;

/**
 * Created by YangZhi on 2017/10/16 13:54.
 */

public class LiveCompletedViewModel {
    static final String STR_PLAY_COUNT_PREFIX = "%d人已看过";

    private String playCount;

    private String fansCount;

    private String giftCount;

    private String wCoinCount;

    private boolean isContribution;

    private boolean hasMemberRank;

    private List<TabItemViewModel> tabItemViewModels;

    Object serverModel;

    public void convert(Object severModel) {
        this.serverModel = severModel;
    }

    public <M> M getSeverModel() {
        return (M) serverModel;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int count) {
        this.playCount = String.format(STR_PLAY_COUNT_PREFIX, count);
    }

    public String getFansCount() {
        return fansCount;
    }

    public void setFansCount(int count) {
        this.fansCount = String.valueOf(count);
    }

    public String getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int count) {
        this.giftCount = String.valueOf(count);
    }

    public String getwCoinCount() {
        return wCoinCount;
    }

    public void setwCoinCount(int count) {
        this.wCoinCount = String.valueOf(count);
    }

    public boolean isContribution() {
        return isContribution;
    }

    public void setContribution(boolean contribution) {
        isContribution = contribution;
    }

    public void setTabItemViewModels(List<TabItemViewModel> tabItemViewModels) {
        this.tabItemViewModels = tabItemViewModels;
    }

    public List<TabItemViewModel> getTabItemViewModels() {
        return tabItemViewModels;
    }

    public boolean isHasMemberRank() {
        return hasMemberRank;
    }

    public void setHasMemberRank(boolean hasMemberRank) {
        this.hasMemberRank = hasMemberRank;
    }

    public static class TabItemViewModel {
        public static final int TYPE_CONTRIBUTION_RANK = 0;
        public static final int TYPE_MEMBER_RANK = 1;

        private String title;
        private int type;
        private List datas;

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setDatas(List datas) {
            this.datas = datas;
        }

        public List getDatas() {
            return datas;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
