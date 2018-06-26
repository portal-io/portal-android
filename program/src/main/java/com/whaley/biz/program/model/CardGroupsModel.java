package com.whaley.biz.program.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2018/1/23.
 */

public class CardGroupsModel implements Serializable {

    private String code;
    private String displayName;
    private int finished;
    private int type;
    private List<CardListModel> cardList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<CardListModel> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardListModel> cardList) {
        this.cardList = cardList;
    }
}
