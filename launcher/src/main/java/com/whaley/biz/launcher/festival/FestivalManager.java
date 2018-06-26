package com.whaley.biz.launcher.festival;

import com.whaley.biz.launcher.model.FestivalModel;
import com.whaley.biz.launcher.util.SharedPreferencesUtil;

/**
 * Created by dell on 2018/1/26.
 */

public class FestivalManager {

    private static FestivalManager sInstance = null;

    private FestivalModel festivalModel;

    private FestivalManager() {

    }

    public static synchronized FestivalManager getInstance() {
        if (sInstance == null) {
            sInstance = new FestivalManager();
        }
        return sInstance;
    }

    public FestivalModel getFestivalModel() {
        return festivalModel;
    }

    public void setFestivalModel(FestivalModel festivalModel) {
        this.festivalModel = festivalModel;
    }

    public boolean isFestivalAvailable(){
        if(festivalModel == null)
            return false;
        long nowTime = System.currentTimeMillis();
        if(nowTime>=festivalModel.getEnableTime()&&nowTime<festivalModel.getDisableTime()){
            return true;
        }
        return false;
    }

    public String getUrl(){
        if(festivalModel == null)
            return null;
        return festivalModel.getUrl();
    }

}
