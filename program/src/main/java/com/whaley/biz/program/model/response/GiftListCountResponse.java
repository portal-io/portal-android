package com.whaley.biz.program.model.response;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.program.model.GiftListCountModel;

import java.util.List;

/**
 * Created by YangZhi on 2017/10/12 16:07.
 */

public class GiftListCountResponse extends CMSResponse<GiftListCountModel> implements BaseListResponse<GiftListCountModel,GiftListCountModel.UserCountModel>{

    @Override
    public List<GiftListCountModel.UserCountModel> getList() {
        if(getData()==null)
            return null;
        return getData().getUserCountList();
    }

    @Override
    public boolean isLast() {
        return true;
    }
}
