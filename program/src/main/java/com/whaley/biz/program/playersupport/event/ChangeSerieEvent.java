package com.whaley.biz.program.playersupport.event;

import com.whaley.biz.playerui.event.PriorityEvent;

/**
 * Created by YangZhi on 2017/8/24 17:18.
 */

public class ChangeSerieEvent extends PriorityEvent {

    private boolean isChangeToNext;

    private int serie;

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public int getSerie() {
        return serie;
    }

    public void setChangeToNext(boolean changeToNext) {
        isChangeToNext = changeToNext;
    }

    public boolean isChangeToNext() {
        return isChangeToNext;
    }
}
