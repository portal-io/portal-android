package com.whaley.biz.setting.response;

import com.whaley.biz.common.response.ListTabResponse;
import com.whaley.biz.common.response.Response;
import com.whaley.biz.setting.model.CollectModel;
import com.whaley.biz.setting.model.PrizeDataModel;

import java.util.List;

public class GiftListResponse extends ListTabResponse<PrizeDataModel> {

    /**
     * status : 1
     * list : [{"id":"5","whaleyuid":"39279708","nickname":"春","mobile":"","create_time":"1480562349","status":"1","sid":"1","name":"大疆无人机","action":"","actionid":"0","actiontxt":"","picture":""}]
     * addressdata : {"whaleyuid":"39279708","username":"","mobile":"18221004130","province":"上海","city":"浦东新区","address":"盛夏路666号"}
     * param_get : {"whaleyuid":"39279708"}
     */

    private List<PrizeDataModel> prizesdata;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean checkStatus() {
        return status == 1 || status == 200;
    }

    @Override
    public List<PrizeDataModel> getList() {
        if (getPrizesdata() != null)
            return getPrizesdata();
        else {
            return null;
        }
    }

    @Override
    public boolean isLast() {
        return true;
    }

    public List<PrizeDataModel> getPrizesdata() {
        return prizesdata;
    }

    public void setPrizesdata(List<PrizeDataModel> prizesdata) {
        this.prizesdata = prizesdata;
    }
}
