package com.whaley.biz.setting.event;

import com.whaley.biz.setting.ui.viewmodel.AddressViewModel;

/**
 * Created by dell on 2017/8/3.
 */

public class ModifyAddressEvent {

    private AddressViewModel addressViewModel;

    public ModifyAddressEvent(AddressViewModel addressViewModel){
        this.addressViewModel = addressViewModel;
    }

    public AddressViewModel getAddressViewModel() {
        return addressViewModel;
    }

    public void setAddressViewModel(AddressViewModel addressViewModel) {
        this.addressViewModel = addressViewModel;
    }
}
