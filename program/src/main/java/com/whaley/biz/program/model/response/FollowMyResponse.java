package com.whaley.biz.program.model.response;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.program.model.CpFollowInfoModel;
import com.whaley.biz.program.model.FollowMyModel;

import java.util.List;

/**
 * Author: qxw
 * Date: 2017/3/22
 */

public class FollowMyResponse extends CMSResponse<FollowMyModel> implements BaseListResponse<FollowMyModel, CpFollowInfoModel> {


    @Override
    public List<CpFollowInfoModel> getList() {
        if (getData() != null) {
            if (getData().getLatestUpdated() != null && getData().getLatestUpdated().size() > 0) {
                return getData().getLatestUpdated();
            }
        }
        return null;
    }


    @Override
    public boolean isLast() {
        if (getData() != null) {
            if (getData().getPageNumber() < getData().getTotalPages()) {
                return false;
            }
        }
        return true;
    }


}
