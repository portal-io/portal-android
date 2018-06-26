package com.whaley.biz.setting.ui.view;

import android.support.v4.app.FragmentManager;

import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.setting.ui.viewmodel.CurrencyViewModel;

import java.util.List;

/**
 * Created by dell on 2017/10/12.
 */

public interface CurrencyView extends LoaderView<List<CurrencyViewModel>> {

    void updateAmount(int amount);
    FragmentManager getFragmentManager();
    void showCurencyContent();

}
