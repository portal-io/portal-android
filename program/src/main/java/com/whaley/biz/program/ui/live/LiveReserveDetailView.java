package com.whaley.biz.program.ui.live;

import android.support.v4.app.FragmentManager;

import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.program.ui.uimodel.ReserveDetailViewModel;

/**
 * Created by dell on 2017/8/17.
 */

public interface LiveReserveDetailView extends BasePageView{

    void updateData(ReserveDetailViewModel reserveDetailViewModel);
    void updateReserve(boolean isReserve);
    void showPay(boolean isBuyVisible);
    FragmentManager getChildFragmentManager();

}
