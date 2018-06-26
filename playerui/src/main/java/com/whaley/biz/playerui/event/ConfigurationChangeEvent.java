package com.whaley.biz.playerui.event;

import android.content.res.Configuration;

/**
 * Created by yangzhi on 2017/9/22.
 */

public class ConfigurationChangeEvent {
    Configuration newConfig;

    public Configuration getNewConfig() {
        return newConfig;
    }

    public void setNewConfig(Configuration newConfig) {
        this.newConfig = newConfig;
    }
}
