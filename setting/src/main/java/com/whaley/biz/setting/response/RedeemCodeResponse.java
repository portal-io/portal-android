package com.whaley.biz.setting.response;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.setting.model.RedeemCodeDateModel;
import com.whaley.biz.setting.model.RedeemCodeModel;

import java.util.List;

/**
 * Created by dell on 2017/7/25.
 */

public class RedeemCodeResponse extends CMSResponse<RedeemCodeDateModel> implements BaseListResponse<RedeemCodeDateModel, RedeemCodeModel> {

    @Override
    public List<RedeemCodeModel> getList() {
        RedeemCodeDateModel redeemCodeDateModel = getData();
        if (redeemCodeDateModel != null) {
            return redeemCodeDateModel.getRedeemCodeList();
        }
        return null;
    }

    @Override
    public boolean isLast() {
        return false;
    }

}
