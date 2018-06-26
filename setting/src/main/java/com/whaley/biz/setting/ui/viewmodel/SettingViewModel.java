package com.whaley.biz.setting.ui.viewmodel;

/**
 * Author: qxw
 * Date:2017/7/31
 * Introduction:
 */

public class SettingViewModel {

    public final static int SETTING_CLEAR_CACHE = 1;
    public final static int SETTING_WIFI = 2;
    public final static int SETTING_PLAYER_DETECTOR = 3;
    public final static int SETTING_LEVE = 4;
    public final static int SETTING_UPDATE = 5;
    public final static int SETTING_TEST_API = 6;
    public final static int SETTING_TEST_BI = 7;
    public final static int SETTING_TEST_MIDULE = 8;

    private int type;
    private String name;
    private String text;
    private int rightPic = -1;
    private int teftPic = -1;
    private boolean isOnClick = true;
    private int checkboxPic = -1;
    private boolean isWifiOnly;
    private int segmentSize;
    private String[] segmentList;
    private int segmentIndex;

    public boolean isWifiOnly() {
        return isWifiOnly;
    }

    public void setWifiOnly(boolean wifiOnly) {
        isWifiOnly = wifiOnly;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public int getSegmentIndex() {
        return segmentIndex;
    }

    public void setSegmentIndex(int segmentIndex) {
        this.segmentIndex = segmentIndex;
    }
}
