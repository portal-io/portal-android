package com.whaley.biz.playerui.event;

/**
 * Created by YangZhi on 2017/8/2 14:35.
 */

public class HideEvent extends PriorityEvent{
    boolean isAnim;

    public boolean isAnim() {
        return isAnim;
    }

    public void setAnim(boolean anim) {
        isAnim = anim;
    }
}
