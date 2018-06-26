package com.whaley.biz.setting.widget.address;

import com.whaley.biz.setting.widget.address.model.City;
import com.whaley.biz.setting.widget.address.model.County;
import com.whaley.biz.setting.widget.address.model.Province;

public interface OnAddressSelectedListener {
    void onAddressSelected(Province province, City city, County county);
}
