package com.whaley.biz.program.model.response;


import com.whaley.biz.common.response.BaseResponse;

/**
 * Created by YangZhi on 2016/11/28 17:20.
 */

public class BoxAuthResponse implements BaseResponse {

    /**
     * status	int	状态码 (成功 1 ;2001 缺少参数 2025 微鲸通行证Token过期 2026 微鲸通行证用户信息获取错误 2011 活动还未开始 2012 活动已经结束)
     * memberdata	object	会员信息 (详见 返回字段说明 )
     * uid	int	uid
     * memberdata.nickname	string	用户昵称
     * memberdata.mobile	string	手机号码
     * memberdata.gender	int	性别
     * memberdata.avatar	string	用户头像的链接 (例如:http://image.aginomoto.com/whaley?acid=39279708)
     * memberdata.email	string	用户邮箱
     * memberdata.whaleyuid	string	微鲸通行证Uid
     * interval	int	间隔时间( 以秒为单位 )
     */

    private int status;

    private int uid;

    private int interval;



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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
