package com.whaley.biz.playerui.model;

/**
 * Created by YangZhi on 2017/4/23 7:27.
 */

public class Setting {

    private boolean reStartOnNetworkChanged;


    public Setting setReStartOnNetworkChanged(boolean reStartOnNetworkChanged) {
        this.reStartOnNetworkChanged = reStartOnNetworkChanged;
        return this;
    }

    public boolean isReStartOnNetworkChanged() {
        return reStartOnNetworkChanged;
    }
}
