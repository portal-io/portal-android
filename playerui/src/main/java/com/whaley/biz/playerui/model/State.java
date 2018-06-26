package com.whaley.biz.playerui.model;

/**
 * Created by YangZhi on 2017/4/17 17:47.
 */

public class State {

    public static final int STATE_INIT=0;

    public static final int STATE_PREPARING=1;

    public static final int STATE_STARTED=2;

    public static final int STATE_PAUSED=3;

    public static final int STATE_ERROR=4;

    public static final int STATE_COMPLETED=5;

    private int mState = STATE_INIT;

    public int getCurrentState() {
        return mState;
    }

    public boolean changeState(int state){
        if(mState == state && state != STATE_ERROR){
            return false;
        }
        mState=state;
        return true;
    }

    public void reset(){
        mState = STATE_INIT;
    }
}
