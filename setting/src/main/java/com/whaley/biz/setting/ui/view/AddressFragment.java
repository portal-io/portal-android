package com.whaley.biz.setting.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.ui.presenter.AddressPresenter;
import com.whaley.biz.setting.ui.viewmodel.AddressViewModel;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.utils.StrUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/3.
 */

public class AddressFragment extends BaseMVPFragment<AddressPresenter> implements AddressView{

    @BindView(R2.id.et_name)
    EditText etName;
    @BindView(R2.id.et_number)
    EditText etNumber;
    @BindView(R2.id.et_full_address)
    EditText etFullAddress;
    @BindView(R2.id.btn_submit)
    TextView btnSubmit;

    public static void goPage(Starter starter, AddressViewModel addressViewModel) {
        Intent intent = TitleBarActivity.createIntent(starter, AddressFragment.class.getName(), null, false);
        intent.putExtra(AddressPresenter.STR_ADDRESS, addressViewModel);
        starter.startActivityForResult(intent, 0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_modify_address;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if(getTitleBar()!=null){
            getTitleBar().setTitleText("修改地址");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        etName.addTextChangedListener(new watcher(R2.id.et_name));
        etNumber.addTextChangedListener(new watcher(R2.id.et_number));
        etFullAddress.addTextChangedListener(new watcher(R2.id.et_full_address));
        AddressViewModel addressViewModel = getPresenter().getAddressRepository().getAddressViewModel();
        if (addressViewModel != null) {
            etName.setText(addressViewModel.getName());
            etNumber.setText(addressViewModel.getMobile());
            etFullAddress.setText(addressViewModel.getAddress());
        }
        if(!TextUtils.isEmpty(etName.getText())) {
            etName.setSelection(etName.getText().length());
        }
    }

    @OnClick(R2.id.btn_submit)
    void submit() {
        if (StrUtil.isMobileNo(etNumber.getText().toString())) {
            AddressViewModel addressViewModel = getPresenter().getAddressRepository().getAddressViewModel();
            addressViewModel.setName(etName.getText().toString());
            addressViewModel.setAddress(etFullAddress.getText().toString());
            addressViewModel.setMobile(etNumber.getText().toString());
            getPresenter().CheckLogin();
        } else {
            showToast("请出入正确手机号");
        }
    }

    public class watcher implements TextWatcher {
        private int editTextId;

        public watcher(int id) {
            this.editTextId = id;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (editTextId) {
                case R2.id.et_name:
                case R2.id.et_number:
                case R2.id.et_full_address:
                    if (!etName.getText().toString().isEmpty() && !etNumber.getText().toString().isEmpty() && !etFullAddress.getText().toString().isEmpty()) {
                        btnSubmit.setEnabled(true);
                    } else {
                        btnSubmit.setEnabled(false);
                    }
                    break;
            }

        }
    }

}
