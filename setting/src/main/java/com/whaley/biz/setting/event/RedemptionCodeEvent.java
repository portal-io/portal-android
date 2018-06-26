package com.whaley.biz.setting.event;

import com.whaley.biz.setting.model.RedemptionCodeModel;

/**
 * Created by dell on 2017/8/3.
 */

public class RedemptionCodeEvent {

    private RedemptionCodeModel redemptionCodeModel;

    public RedemptionCodeEvent(RedemptionCodeModel redemptionCodeModel){
        this.redemptionCodeModel = redemptionCodeModel;
    }

    public RedemptionCodeModel getRedemptionCodeModel() {
        return redemptionCodeModel;
    }

    public void setRedemptionCodeModel(RedemptionCodeModel redemptionCodeModel) {
        this.redemptionCodeModel = redemptionCodeModel;
    }
}
