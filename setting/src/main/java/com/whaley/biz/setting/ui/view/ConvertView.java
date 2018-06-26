package com.whaley.biz.setting.ui.view;

import android.widget.EditText;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Created by dell on 2017/8/4.
 */

public interface ConvertView extends BasePageView {

    void showBtn();

    void removeBtn();

    EditText getEditText();

}
