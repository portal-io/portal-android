package com.whaley.biz.program.model;

import java.io.Serializable;

/**
 * Created by dell on 2018/1/23.
 */

public class SummaryModel implements Serializable {

    private int finishedCount;
    private int cardCount;

    public int getFinishedCount() {
        return finishedCount;
    }

    public void setFinishedCount(int finishedCount) {
        this.finishedCount = finishedCount;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }
}
