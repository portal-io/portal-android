package com.whaley.biz.program.model.response;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.program.model.GiftMemberCountModel;

import java.util.List;

/**
 * Created by YangZhi on 2017/10/12 16:12.
 */

public class GiftMemberCountResponse extends CMSResponse<GiftMemberCountModel> implements BaseListResponse<GiftMemberCountModel,GiftMemberCountModel.MemberCountModel> {

    @Override
    public List<GiftMemberCountModel.MemberCountModel> getList() {
        if(getData()==null)
            return null;
        return getData().getMemberCountList();
    }

    @Override
    public boolean isLast() {
        return true;
    }
}
