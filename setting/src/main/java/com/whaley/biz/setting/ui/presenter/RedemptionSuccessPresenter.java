package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.setting.constant.ProgramConstants;
import com.whaley.biz.setting.constant.SettingConstants;
import com.whaley.biz.setting.event.RedemptionSuccessEvent;
import com.whaley.biz.setting.model.RedemptionCodeModel;
import com.whaley.biz.setting.router.FormatPageModel;
import com.whaley.biz.setting.router.GoPageUtil;
import com.whaley.biz.setting.ui.repository.RedemptionSuccessRepository;
import com.whaley.biz.setting.ui.view.RedemptionSuccessView;
import com.whaley.core.inject.annotation.Repository;

/**
 * Created by dell on 2017/9/4.
 */

public class RedemptionSuccessPresenter extends BasePagePresenter<RedemptionSuccessView> implements ProgramConstants{

    public final static String STR_PARAM_REDEMPTION = "str_param_redemption";

    @Repository
    RedemptionSuccessRepository redemptionSuccessRepository;

    public RedemptionSuccessPresenter(RedemptionSuccessView view) {
        super(view);
    }

    public RedemptionSuccessRepository getRedemptionSuccessRepository() {
        return redemptionSuccessRepository;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            RedemptionCodeModel data = arguments.getParcelable(STR_PARAM_REDEMPTION);
            getRedemptionSuccessRepository().setData(data);
            if(data!=null) {
                EventController.postEvent(new RedemptionSuccessEvent(SettingConstants.EVENT_REDEMTION_SUCCCESS, data.getRelatedCode()));
            }
        }
    }

    public void onRedemptionVoucherClick() {
        RedemptionCodeModel redemptionCodeModel = getRedemptionSuccessRepository().getData();
        if(redemptionCodeModel==null)
            return;
        if (TYPE_CONTENT_PACKGE.equals(redemptionCodeModel.getRelatedType())) {
            GoPageUtil.goPage(getStater(), FormatPageModel.getPackagePageModel(redemptionCodeModel.getRelatedCode()));
        }
        else if (TYPE_RECORDED.equals(redemptionCodeModel.getRelatedType())) {
            GoPageUtil.goPage(getStater(), FormatPageModel.getPlayerPageModel(redemptionCodeModel.getPlayData(), false));
        }
        else if (TYPE_LIVE.equals(redemptionCodeModel.getRelatedType())) {
            int status = redemptionCodeModel.getLiveStatus();
            if (LIVE_STATE_BEING == status) {
                GoPageUtil.goPage(getStater(), FormatPageModel.getPlayerPageModel(redemptionCodeModel.getPlayData(), true));
            } else if (LIVE_STATE_BEFORE == status) {
                GoPageUtil.goPage(getStater(), FormatPageModel.getLiveBeforePageModel(redemptionCodeModel.getRelatedCode()));
            } else {
                GoPageUtil.goPage(getStater(), FormatPageModel.getLiveAfterPageModel(redemptionCodeModel.getRelatedCode()));
            }
        }
    }

}
