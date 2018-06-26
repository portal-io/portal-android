package com.whaley.biz.program.model.response;


import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.program.model.PrizeDataModel;

/**
 * Created by YangZhi on 2016/11/28 17:37.
 */

public class BoxLotteryResponse implements BaseResponse {
    /**
     * status	int	状态码 (中奖 1 ;未中奖 0; 2001 关联信息不存在 2007 谢谢参与 2008 该奖品数已为空 2010 该视频未关联奖品 2011 活动还未开始 2012 活动已经结束 2013 你抽奖次数太过频繁)
     * msg	string	错误信息
     * sid	int	奖品ID
     * name	string	奖品名称
     * picture	string	奖品图片url
     */

    private int status;

    private String msg;

    private PrizeDataModel prizedata;

    private String name;

    private String picture;

    private int countdown;


    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public boolean checkStatus() {
        return status == 1;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String getMsg() {
        switch (status) {
            case 1:
                return "成功";
            case 0:
                return "未中奖";
            case -2001:
                return "关联信息不存在";
            case -2007:
                return "谢谢参与";
            case -2008:
                return "该奖品数已为空";
            case -2010:
                return "该视频未关联奖品";
            case -2011:
                return "活动还未开始";
            case -2012:
                return "活动已经结束";
            case -2013:
                return "你抽奖次数太过频繁";
        }
        return null;
    }


    @Override
    public void setMsg(String msg) {

    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public void setCache(boolean cache) {

    }

    @Override
    public boolean isCache() {
        return false;
    }

    @Override
    public Object getData() {
        return null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public PrizeDataModel getPrizedata() {
        return prizedata;
    }

    public void setPrizedata(PrizeDataModel prizedata) {
        this.prizedata = prizedata;
    }
}
