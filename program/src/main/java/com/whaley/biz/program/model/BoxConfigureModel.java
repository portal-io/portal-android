package com.whaley.biz.program.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2018/1/23.
 */

public class BoxConfigureModel implements Serializable {


    /**
     * summary : {"finishedCount":0,"cardCount":4}
     * cardGroups : [{"cardList":[{"code":"gong","name":"恭","count":1},{"code":"xi","name":"喜","count":2},{"code":"fa","name":"发","count":0},{"code":"cai","name":"财","count":0}],"code":"bbb","displayName":"现金红包","finished":1,"type":1},{"cardList":[{"code":"chun","name":"春","count":1},{"code":"jie","name":"节","count":0},{"code":"kuai","name":"快","count":0},{"code":"le","name":"乐","count":0}],"displayName":"实物奖品","finished":0,"type":2}]
     * awardList : [{"mobile":"136****2345","type":1,"desc":"现金"},{"mobile":"138****2345","type":2,"desc":"vr一体机"}]
     */

    private SummaryModel summary;
    private List<CardGroupsModel> cardGroups;
    private List<AwardListModel> awardList;

    public void setSummary(SummaryModel summary) {
        this.summary = summary;
    }

    public void setCardGroups(List<CardGroupsModel> cardGroups) {
        this.cardGroups = cardGroups;
    }

    public void setAwardList(List<AwardListModel> awardList) {
        this.awardList = awardList;
    }

    public SummaryModel getSummary() {
        return summary;
    }

    public List<CardGroupsModel> getCardGroups() {
        return cardGroups;
    }

    public List<AwardListModel> getAwardList() {
        return awardList;
    }

}
