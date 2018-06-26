package com.whaley.biz.user.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTextChangedListener;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.user.R;
import com.whaley.biz.user.R2;
import com.whaley.biz.user.ui.presenter.EditNickNamePresenter;
import com.whaley.core.appcontext.Starter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date:2017/8/3
 * Introduction:
 */

@Route(path = "/user/ui/editNickName")
public class EditNickNameFragment extends BaseMVPFragment<EditNickNamePresenter> implements EditNickNameView {

    public static void goPage(Starter starter) {
        TitleBarActivity.goPage(starter, EditNickNameFragment.class);
    }



    @BindView(R2.id.et_nickname)
    EditText etNickname;


    @OnClick(R2.id.iv_clear)
    void onClearClick() {
        etNickname.setText("");
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_nickname;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.user_title_edit_nickname));
            getTitleBar().setRightText(getString(R.string.user_text_save));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener() {
                @Override
                public void onRightClick(View view) {
                    getPresenter().onSaveClick();
                }
            });
        }
        etNickname.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                getPresenter().onNickNameChanged(s.toString());
            }
        });
    }

    @Override
    public void showNickName(String userName) {
        if (!TextUtils.isEmpty(userName)) {
            etNickname.setText(userName);
            etNickname.setSelection(userName.length());
        }
    }

}
