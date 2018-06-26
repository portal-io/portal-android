package com.whaley.biz.setting.ui.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/8/1.
 */

public class AddressViewModel implements Parcelable {

    private String name = "";
    private String mobile = "";
    private String address = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.address);
    }

    public AddressViewModel() {
    }

    protected AddressViewModel(Parcel in) {
        this.name = in.readString();
        this.mobile = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<AddressViewModel> CREATOR = new Parcelable.Creator<AddressViewModel>() {
        public AddressViewModel createFromParcel(Parcel source) {
            return new AddressViewModel(source);
        }

        public AddressViewModel[] newArray(int size) {
            return new AddressViewModel[size];
        }
    };

}
