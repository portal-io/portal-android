package com.whaley.biz.program.ui.event;

/**
 * Author: qxw
 * Date:2017/8/30
 * Introduction:
 */

public class FollowEvent {
    public static final int PUBLISHER = 0;
    public static final int FOLLOW_LIST = 1;
    public static final int DETAIL = 2;

    private String code;
    private boolean isFollowed;
    private int page;


    public FollowEvent(String code, boolean isFollowed, int page) {
        this.code = code;
        this.isFollowed = isFollowed;
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }
}
