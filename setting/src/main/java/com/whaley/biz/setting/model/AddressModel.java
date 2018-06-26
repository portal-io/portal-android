package com.whaley.biz.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.whaley.biz.common.CommonConstants;


public class AddressModel implements Parcelable {

//    name	收货姓名
//    mobile	收货手机号码
//    province	所在省
//    city	所在市
//    county	所在县/区
//    address	街道详细地址

    private String name;
    private String mobile;
    private String province;
    private String city;
    private String county;
    private String address;

    private String whaleyuid;
    private String timestamp;
    private String systemname = CommonConstants.VALUE_SYSTEM_NAME;
    private String systemver = CommonConstants.VALUE_SYSTEM_VERSION;
    private String appname = CommonConstants.VALUE_APP_NAME;
    private String appver = CommonConstants.VALUE_APP_VERSION_NAME;
    private String appvercode = CommonConstants.VALUE_APP_VERSION_CODE;
    private String sign;


//    whaleyuid	int	yes	微鲸UID
//    name	string	yes	POST / 收货人姓名
//    mobile	int	yes	POST / 手机号码
//    province	string	yes	POST / 所在省
//    city	string	yes	POST / 所在市
//    county	string	yes	POST / 所在 区/县
//    address	string	yes	POST / 收货人街道地址
//    timestamp	int	yes	客户端请求时间戳,30秒过期,签名方法见 相关文档-签名机制
//    systemname	string	yes	系统标识 (版本控制使用,ios 或 android)
//    systemver	string	yes	系统版本号(版本控制使用, 类型说明: int 或 float 或者 类 1.0.2 之类 由数字和小数点组成的字符串)
//    appname	string	yes	APP名称(版本控制使用)
//    appver	string	yes	APP版本名(版本控制使用)
//    appvercode	string	yes	APP版本号(版本控制使用, 类型说明: int 或 float 或者 类 1.0.2 之类 由数字和小数点组成的字符串)

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWhaleyuid() {
        return whaleyuid;
    }

    public void setWhaleyuid(String whaleyuid) {
        this.whaleyuid = whaleyuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSystemname() {
        return systemname;
    }

    public void setSystemname(String systemname) {
        this.systemname = systemname;
    }

    public String getSystemver() {
        return systemver;
    }

    public void setSystemver(String systemver) {
        this.systemver = systemver;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAppver() {
        return appver;
    }

    public void setAppver(String appver) {
        this.appver = appver;
    }

    public String getAppvercode() {
        return appvercode;
    }

    public void setAppvercode(String appvercode) {
        this.appvercode = appvercode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.county);
        dest.writeString(this.address);
        dest.writeString(this.whaleyuid);
        dest.writeString(this.timestamp);
        dest.writeString(this.systemname);
        dest.writeString(this.systemver);
        dest.writeString(this.appname);
        dest.writeString(this.appver);
        dest.writeString(this.appvercode);
        dest.writeString(this.sign);
    }

    public AddressModel() {
    }

    protected AddressModel(Parcel in) {
        this.name = in.readString();
        this.mobile = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.county = in.readString();
        this.address = in.readString();
        this.whaleyuid = in.readString();
        this.timestamp = in.readString();
        this.systemname = in.readString();
        this.systemver = in.readString();
        this.appname = in.readString();
        this.appver = in.readString();
        this.appvercode = in.readString();
        this.sign = in.readString();
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel source) {
            return new AddressModel(source);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };
}
