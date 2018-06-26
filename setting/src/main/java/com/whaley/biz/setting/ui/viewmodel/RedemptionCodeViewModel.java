package com.whaley.biz.setting.ui.viewmodel;

import com.whaley.biz.setting.model.RedeemCodeModel;
import com.whaley.biz.setting.model.RedemptionCodeModel;

/**
 * Created by dell on 2017/8/3.
 */

public class RedemptionCodeViewModel {

    public static final int TYPE_BOX = 0;
    public static final int TYPE_CODE = 1;
    public static final int TYPE_NAME = 2;
    public static final int TYPE_VOUCHER = 3;

    public static final String TYPE_RESERVATION_NUMBER = "reservationNumber";
    public static final String TYPE_ACTIVITY = "activity";
    public int type;
    public boolean isHaveCode;
    public boolean isLast;
    public String association;
    public RedemptionCodeModel redemptionCodeModel;
    public RedeemCodeModel redeemCodeModel;

    public RedemptionCodeViewModel(RedeemCodeModel redeemCodeModel, int type) {
        this.type = type;
        this.redeemCodeModel = redeemCodeModel;
        if (TYPE_RESERVATION_NUMBER.equals(redeemCodeModel.getRedeemCodeShowType())) {
            association = "预留号码关联";
        } else {
            association = "活动";
        }
    }

    public RedemptionCodeViewModel(RedemptionCodeModel redemptionCodeModel, int type) {
        this.type = type;
        this.redemptionCodeModel = redemptionCodeModel;
    }

    public RedemptionCodeViewModel(int type) {
        this.type = type;
    }

}
