package com.whaley.biz.setting.ui.view;

import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.setting.ui.viewmodel.HistoryViewModel;

import java.util.List;

/**
 * Created by dell on 2017/8/24.
 */

public interface HistoryView extends LoaderView<List<HistoryViewModel>> {

    void showEdit();
    void cancelEdit();
    void update(int position);
    void updateAll();

}

