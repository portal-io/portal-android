package com.whaley.biz.program.model.response;


import com.whaley.biz.common.response.BaseResponse;

/**
 * Created by YangZhi on 2016/11/28 17:32.
 */

public class BoxTimeResponse implements BaseResponse{

    /**
     * status	int	状态码 (成功 1 ;0 未设置参数 2011 活动还未开始 2012 活动已经结束)
     * interval	int	间隔时间( 以秒为单位 )
     */

    private int status;

    private int countdown;

    private int current_time;

    private int prevtime;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public boolean checkStatus() {
        return status==1;
    }

    @Override
    public void setStatus(int status) {
        this.status=status;
    }

    @Override
    public String getMsg() {
        switch (status){
            case 1:
                return "成功";
            case 0:
                return "未设置参数";
            case -2001:
                return "缺少参数";
            case -2025:
                return "微鲸通行证Token过期";
            case -2026:
                return "微鲸通行证用户信息获取错误";
            case -2011:
                return "活动还未开始";
            case -2012:
                return "活动已经结束";
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

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }


    public void setCurrent_time(int current_time) {
        this.current_time = current_time;
    }

    public int getCurrent_time() {
        return current_time;
    }

    public void setPrevtime(int prevtime) {
        this.prevtime = prevtime;
    }

    public int getPrevtime() {
        return prevtime;
    }
}
