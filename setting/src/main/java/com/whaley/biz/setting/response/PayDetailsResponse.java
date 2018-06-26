package com.whaley.biz.setting.response;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.setting.model.PayContentModel;
import com.whaley.biz.setting.model.PayDetailsModel;

import java.util.List;

public class PayDetailsResponse extends CMSResponse<PayDetailsModel> implements BaseListResponse<PayDetailsModel, PayContentModel> {

    @Override
    public List<PayContentModel> getList() {
        PayDetailsModel payDetailsModel = getData();
        if (payDetailsModel != null) {
            return payDetailsModel.getOrderListPageCache().getContent();
        }
        return null;
    }

    @Override
    public boolean isLast() {
        PayDetailsModel payDetailsModel = getData();
        if (payDetailsModel != null) {
            return payDetailsModel.getOrderListPageCache().isLast();
        }
        return false;
    }
}
