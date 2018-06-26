package com.whaley.biz.setting.response;

import com.whaley.biz.common.response.Response;
import com.whaley.biz.setting.model.AddressModel;

public class AddressResponse extends Response {

    private AddressModel member_addressdata;

    public AddressModel getMember_addressdata() {
        return member_addressdata;
    }

    public void setMember_addressdata(AddressModel member_addressdata) {
        this.member_addressdata = member_addressdata;
    }

    @Override
    public AddressModel getData() {
        return member_addressdata;
    }
}
