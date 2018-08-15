package com.whaley.biz.portal.response;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.PortalResponse;
import com.whaley.biz.portal.model.EarningInfo;
import com.whaley.biz.portal.model.PageInfo;

import java.util.List;

/**
 * Created by dell on 2018/8/10.
 */

public class EarningInfoResponse extends PortalResponse implements BaseListResponse<PortalResponse,EarningInfo> {

    private List<EarningInfo> earningsInfos;
    private PageInfo pagingInfo;

    @Override
    public List<EarningInfo> getList() {
        return earningsInfos;
    }

    @Override
    public boolean isLast() {
        boolean isLast = true;
        if(pagingInfo!=null&&pagingInfo.getNumber()<pagingInfo.getTotalPages()-1){
            isLast = false;
        }
        return isLast;
    }

}
