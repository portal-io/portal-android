package com.whaley.biz.portal.ui.view;

import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.portal.model.PortalRecord;
import com.whaley.biz.portal.ui.viewmodel.PortalViewModel;

import java.util.List;

/**
 * Created by dell on 2018/8/2.
 */

public interface PortalView extends LoaderView<List<PortalViewModel>> {
    void updateBalance(float balance);
    void updateRecode(List<PortalRecord> portalRecords);
    void showBind(boolean isBind);
}
