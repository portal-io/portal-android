package com.whaley.biz.launcher.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */

public class SplashRepository extends MemoryRepository {

    private String logoImageUrl;

    private String channelName;

    public String getLogoImageUrl() {
        return logoImageUrl;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
        setLogoImageUrl("file:///android_asset/channel/"+channelName+".png");
    }

}
