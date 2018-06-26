package com.whaley.biz.program.model;

import java.io.Serializable;

/**
 * Author: qxw
 * Date: 2016/12/30
 */

public class SeriesModel implements Serializable {
    private String code;
    private int curEpisode;
    private String displayName;
    private boolean isSelected = false;
    private boolean isReal = true;

    public int getCurEpisode() {
        return curEpisode;
    }

    public void setCurEpisode(int curEpisode) {
        this.curEpisode = curEpisode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isReal() {
        return isReal;
    }

    public void setReal(boolean real) {
        isReal = real;
    }
}
