package com.whaley.biz.portal.response;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.PortalResponse;
import com.whaley.biz.portal.model.PortalInfo;
import com.whaley.biz.portal.model.PortalRecord;
import com.whaley.biz.portal.model.UserInfo;

import java.util.List;

/**
 * Created by dell on 2018/8/9.
 */

public class PortalRecordResponse extends PortalResponse implements BaseListResponse<PortalResponse,PortalRecord> {

    private List<PortalRecord> records;
    private UserInfo userInfo;
    private PortalInfo portalInfo;

    @Override
    public List getList() {
        return records;
    }

    @Override
    public boolean isLast() {
        return true;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public PortalInfo getPortalInfo() {
        return portalInfo;
    }

    public void setPortalInfo(PortalInfo portalInfo) {
        this.portalInfo = portalInfo;
    }
}
