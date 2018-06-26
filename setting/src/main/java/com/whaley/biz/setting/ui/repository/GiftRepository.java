package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;
import com.whaley.biz.setting.ui.viewmodel.AddressViewModel;

/**
 * Created by dell on 2017/8/1.
 */

public class GiftRepository extends MemoryRepository{

    private AddressViewModel addressViewModel;

    private String address;

    private String btnText;

    private boolean isFromUnity;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public AddressViewModel getAddressViewModel() {
        return addressViewModel;
    }

    public void setAddressViewModel(AddressViewModel addressViewModel) {
        this.addressViewModel = addressViewModel;
    }

    public boolean isFromUnity() {
        return isFromUnity;
    }

    public void setFromUnity(boolean fromUnity) {
        isFromUnity = fromUnity;
    }
}
