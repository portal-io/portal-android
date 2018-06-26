package com.whaley.biz.program.ui.user;

import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.program.ui.user.viewmodel.CollectViewModel;

import java.util.List;

/**
 * Created by dell on 2017/8/1.
 */

public interface CollectView extends LoaderView<List<CollectViewModel>> {

    void onChangeEdit(boolean isEdit);

    void onChangeSelected(boolean isSelected, int position);

    void onChangeCheck();

    void onResetEdit();

    void onRemove();

}
