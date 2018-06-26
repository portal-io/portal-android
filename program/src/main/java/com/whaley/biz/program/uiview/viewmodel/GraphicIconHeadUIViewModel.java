package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.CpFollowInfoModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.core.utils.DateUtils;

import java.text.SimpleDateFormat;

/**
 * Author: qxw
 * Date: 2017/3/22
 */

public class GraphicIconHeadUIViewModel extends BaseUIViewModel {

    private String imgUrl;
    private String name;
    private String time;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean isCanClick() {
        return true;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_GRAPHIC_ICON_HEAD;
    }

    public void convert(CpFollowInfoModel cpProgramModel) {
        if (cpProgramModel != null) {
            setName(cpProgramModel.getName());
            setImgUrl(cpProgramModel.getHeadPic());
            setTime(getConversionTime(cpProgramModel.getUpdateTime()));
            setPageModel(FormatPageModel.goPageModelPublisher(cpProgramModel.getCpCode()));
        }
    }

    private String getConversionTime(String time) {
        String timeString = "";
        long sdateLong = Long.parseLong(time);
        int headType = DateUtils.timeDistance(sdateLong);
        if (headType >= 0) {
            timeString = getTimeDay(sdateLong);
        } else if (headType == -1) {
            timeString = "一天前";
        } else if (headType == -2) {
            timeString = "两天前";
        } else if (headType < -2 && headType > -7) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
            String daySting = sdf.format(sdateLong);
            timeString = daySting;
        } else if (headType <= -7 && headType > -14) {
            timeString = "一周前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            String daySting = sdf.format(sdateLong);
            timeString = daySting;
        }
        return timeString;
    }

    private String getTimeDay(long sdateLong) {
        long time = (System.currentTimeMillis() - sdateLong) / 1000;
        if (time < 60) {
            return time + "秒钟前";
        }
        time = time / 60;
        if (time < 60) {
            return time + "分钟前";
        }
        time = time / 60;
        return time + "小时前";
    }

}
