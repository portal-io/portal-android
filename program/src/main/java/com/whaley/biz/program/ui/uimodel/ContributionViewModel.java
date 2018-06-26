package com.whaley.biz.program.ui.uimodel;

import android.text.SpannableString;

import java.util.List;

/**
 * Created by YangZhi on 2017/10/12 12:32.
 */

public class ContributionViewModel {
    private String name;

    private CharSequence contribute;

    private Object image;

    private boolean isLoginUser;

    private boolean isRealData = true;

    private String rank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CharSequence getContribute() {
        return contribute;
    }

    public void setContribute(CharSequence contribute) {
        this.contribute = contribute;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public boolean isLoginUser() {
        return isLoginUser;
    }

    public void setLoginUser(boolean loginUser) {
        isLoginUser = loginUser;
    }

    public boolean isRealData() {
        return isRealData;
    }

    public void setRealData(boolean realData) {
        isRealData = realData;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }
}
