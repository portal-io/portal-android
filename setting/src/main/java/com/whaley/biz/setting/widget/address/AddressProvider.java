package com.whaley.biz.setting.widget.address;

import com.whaley.biz.setting.widget.address.global.Database;
import com.whaley.biz.setting.widget.address.model.City;
import com.whaley.biz.setting.widget.address.model.County;
import com.whaley.biz.setting.widget.address.model.Province;

import java.util.List;

public interface AddressProvider {
    void provideProvinces(Database database, AddressReceiver<Province> addressReceiver);
    void provideCitiesWith(Database database, int provinceId, AddressReceiver<City> addressReceiver);
    void provideCountiesWith(Database database, int cityId, AddressReceiver<County> addressReceiver);

    interface AddressReceiver<T> {
        void send(List<T> data);
    }
}