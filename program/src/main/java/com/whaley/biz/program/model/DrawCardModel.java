package com.whaley.biz.program.model;

import java.io.Serializable;

/**
 * Created by dell on 2018/1/23.
 */

public class DrawCardModel implements Serializable {
    /**
     * code : G72IBM0Y
     * finished : 1
     * relCardType : xi
     */

    private String code;
    private int finished;
    private String relCardType;
    private String relCardName;
    private String relCardsName;
    private int relCardGrpType;
    private int relCardsCnt;

    public void setCode(String code) {
        this.code = code;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public void setRelCardType(String relCardType) {
        this.relCardType = relCardType;
    }

    public String getCode() {
        return code;
    }

    public int getFinished() {
        return finished;
    }

    public String getRelCardType() {
        return relCardType;
    }

    public String getRelCardName() {
        return relCardName;
    }

    public void setRelCardName(String relCardName) {
        this.relCardName = relCardName;
    }

    public int getRelCardsCnt() {
        return relCardsCnt;
    }

    public void setRelCardsCnt(int relCardsCnt) {
        this.relCardsCnt = relCardsCnt;
    }

    public String getRelCardsName() {
        return relCardsName;
    }

    public void setRelCardsName(String relCardsName) {
        this.relCardsName = relCardsName;
    }

    public int getRelCardGrpType() {
        return relCardGrpType;
    }

    public void setRelCardGrpType(int relCardGrpType) {
        this.relCardGrpType = relCardGrpType;
    }
}
