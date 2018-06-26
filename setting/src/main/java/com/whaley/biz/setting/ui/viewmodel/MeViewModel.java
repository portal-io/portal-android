package com.whaley.biz.setting.ui.viewmodel;

/**
 * Created by dell on 2017/8/28.
 */

public class MeViewModel {

    private int type;
    private String name;
    private String text;
    private int rightPic = -1;
    private int leftPic = -1;
    private boolean isDivederBelow;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getLeftPic() {
        return leftPic;
    }

    public void setLeftPic(int leftPic) {
        this.leftPic = leftPic;
    }

    public boolean isDivederBelow() {
        return isDivederBelow;
    }

    public void setDivederBelow(boolean divederBelow) {
        isDivederBelow = divederBelow;
    }
}
