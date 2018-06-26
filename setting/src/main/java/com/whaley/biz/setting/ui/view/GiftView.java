package com.whaley.biz.setting.ui.view;

import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.setting.ui.viewmodel.GiftViewModel;

import java.util.List;

/**
 * Created by dell on 2017/8/1.
 */

public interface GiftView extends LoaderView<List<GiftViewModel>> {

    void updateAddress();

}
