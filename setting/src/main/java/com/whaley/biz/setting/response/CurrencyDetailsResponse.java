package com.whaley.biz.setting.response;

import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.setting.model.CurrencyDetailsModel;
import com.whaley.biz.setting.model.PayContentModel;

import java.util.List;

/**
 * Created by dell on 2017/10/13.
 */

public class CurrencyDetailsResponse extends CMSResponse<CurrencyDetailsModel> implements BaseListResponse<CurrencyDetailsModel, PayContentModel> {

    @Override
    public List<PayContentModel> getList() {
        CurrencyDetailsModel payDetailsModel = getData();
        if (payDetailsModel != null) {
            return payDetailsModel.getOrderListPageCache().getContent();
        }
        return null;
    }

    @Override
    public boolean isLast() {
        CurrencyDetailsModel payDetailsModel = getData();
        if (payDetailsModel != null) {
            return payDetailsModel.getOrderListPageCache().isLast();
        }
        return false;
    }

}
