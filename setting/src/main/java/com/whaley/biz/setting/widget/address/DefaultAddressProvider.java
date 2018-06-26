package com.whaley.biz.setting.widget.address;

import com.whaley.biz.setting.widget.address.global.Database;
import com.whaley.biz.setting.widget.address.model.City;
import com.whaley.biz.setting.widget.address.model.County;
import com.whaley.biz.setting.widget.address.model.Province;

import java.util.ArrayList;
import java.util.List;

public class DefaultAddressProvider implements AddressProvider {
    @Override
    public void provideProvinces(Database database, final AddressReceiver<Province> addressReceiver) {
        final List<Province> provinceQueryList = database.getAllProvices();
        addressReceiver.send(new ArrayList<>(provinceQueryList));
    }

    @Override
    public void provideCitiesWith(Database database,int provinceId, final AddressReceiver<City> addressReceiver) {
        final List<City> cityQueryList = database.getAllCityByProviceId(provinceId);
        addressReceiver.send(new ArrayList<>(cityQueryList));
    }

    @Override
    public void provideCountiesWith(Database database,int cityId, final AddressReceiver<County> addressReceiver) {
        final List<County> countyQueryList = database.getAllCountyByCityId(cityId);
        addressReceiver.send(new ArrayList<>(countyQueryList));
    }

}
