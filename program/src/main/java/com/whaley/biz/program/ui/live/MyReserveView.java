package com.whaley.biz.program.ui.live;

import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.program.ui.uimodel.ReserveViewModel;

import java.util.List;

/**
 * Created by dell on 2017/8/21.
 */

public interface MyReserveView extends LoaderView<List<ReserveViewModel>> {
    void updatePayStatus();
}
