package com.whaley.biz.program.ui.uimodel;

import com.whaley.biz.program.R;

/**
 * Created by YangZhi on 2017/10/13 16:28.
 */

public class MemberRankItemViewModel {
    private CharSequence name;
    private CharSequence contributeText;
    private CharSequence rank;
    private String imageUrl;
    private int imageBg = R.mipmap.bg_contribute_head_first;
    private int nameColor;
    private int contributeColor;
    private int rankColor;

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public CharSequence getContributeText() {
        return contributeText;
    }

    public void setContributeText(CharSequence contributeText) {
        this.contributeText = contributeText;
    }

    public CharSequence getRank() {
        return rank;
    }

    public void setRank(CharSequence rank) {
        this.rank = rank;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageBg() {
        return imageBg;
    }

    public void setImageBg(int imageBg) {
        this.imageBg = imageBg;
    }

    public int getNameColor() {
        return nameColor;
    }

    public void setNameColor(int nameColor) {
        this.nameColor = nameColor;
    }

    public int getContributeColor() {
        return contributeColor;
    }

    public void setContributeColor(int contributeColor) {
        this.contributeColor = contributeColor;
    }

    public int getRankColor() {
        return rankColor;
    }

    public void setRankColor(int rankColor) {
        this.rankColor = rankColor;
    }
}
