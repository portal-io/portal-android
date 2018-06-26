package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.ui.view.AddressView;
import com.whaley.biz.setting.ui.viewmodel.AddressViewModel;

/**
 * Created by dell on 2017/8/3.
 */

public class AddressRepository extends MemoryRepository{

    private AddressViewModel addressViewModel;

    public AddressViewModel getAddressViewModel() {
        return addressViewModel;
    }

    public void setAddressViewModel(AddressViewModel addressViewModel) {
        this.addressViewModel = addressViewModel;
    }
}
