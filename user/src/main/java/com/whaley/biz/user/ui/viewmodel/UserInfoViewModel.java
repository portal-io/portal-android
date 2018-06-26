package com.whaley.biz.user.ui.viewmodel;

/**
 * Author: qxw
 * Date:2017/7/31
 * Introduction:
 */

public class UserInfoViewModel {
    private String name;
    private String text;
    private int rightPic = -1;
    private int teftPic = -1;
    private boolean isOnClick = true;
    private int checkboxPic = -1;
    private int segmentSize;
    private String[] segmentList;

    public boolean isOnClick() {
        return isOnClick;
    }

    public void setOnClick(boolean onClick) {
        isOnClick = onClick;
    }

    public int getCheckboxPic() {
        return checkboxPic;
    }

    public void setCheckboxPic(int checkboxPic) {
        this.checkboxPic = checkboxPic;
    }

    public int getSegmentSize() {
        return segmentSize;
    }

    public void setSegmentSize(int segmentSize) {
        this.segmentSize = segmentSize;
    }

    public String[] getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(String[] segmentList) {
        this.segmentList = segmentList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRightPic() {
        return rightPic;
    }

    public void setRightPic(int rightPic) {
        this.rightPic = rightPic;
    }

    public int getTeftPic() {
        return teftPic;
    }

    public void setTeftPic(int teftPic) {
        this.teftPic = teftPic;
    }
}
