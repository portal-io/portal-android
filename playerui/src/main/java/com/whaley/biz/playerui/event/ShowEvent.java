package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/2 14:36.
 */

public class ShowEvent extends PriorityEvent{

    boolean isAnim;

    public void setAnim(boolean anim) {
        isAnim = anim;
    }

    public boolean isAnim() {
        return isAnim;
    }
}
