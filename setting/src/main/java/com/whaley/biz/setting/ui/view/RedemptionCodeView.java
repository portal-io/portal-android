package com.whaley.biz.setting.ui.view;

import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.setting.model.RedemptionCodeModel;
import com.whaley.biz.setting.ui.viewmodel.RedemptionCodeViewModel;

import java.util.List;

/**
 * Created by dell on 2017/8/3.
 */

public interface RedemptionCodeView extends LoaderView<List<RedemptionCodeViewModel>> {

    void onSuccessView(RedemptionCodeModel redemptionCodeModel);

}
