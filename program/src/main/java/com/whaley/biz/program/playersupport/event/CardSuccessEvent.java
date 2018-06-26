package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.playerui.event.PriorityEvent;

/**
 * Created by dell on 2018/1/23.
 */

public class CardSuccessEvent extends PriorityEvent {

    private int finished;
    private String name;
    private String totleName;
    private int leftCount;
    private int type;
    private int totalCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotleName() {
        return totleName;
    }

    public void setTotleName(String totleName) {
        this.totleName = totleName;
    }

    public int getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(int leftCount) {
        this.leftCount = leftCount;
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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
