package com.whaley.biz.program.model.pay;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/8/21.
 */

public class PayMethodModel implements Parcelable {
//    "whaleyCurrency": 1, //鲸币支付开关（1：开启；0：关闭）
//            "weixin": 1, //微信支付开关（1：开启；0：关闭）
//            "alipay": 1, //支付宝支付开关（1：开启；0：关闭）
//            "appStore": 0 //苹果支付开关（1：开启；0：关闭） 说明：当请求参数appSystem为android时，无此字段

    private int whaleyCurrency;
    private int weixin;
    private int alipay;


    public int getWhaleyCurrency() {
        return whaleyCurrency;
    }

    public void setWhaleyCurrency(int whaleyCurrency) {
        this.whaleyCurrency = whaleyCurrency;
    }

    public int getWeixin() {
        return weixin;
    }

    public void setWeixin(int weixin) {
        this.weixin = weixin;
    }

    public int getAlipay() {
        return alipay;
    }

    public void setAlipay(int alipay) {
        this.alipay = alipay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.whaleyCurrency);
        dest.writeInt(this.weixin);
        dest.writeInt(this.alipay);
    }

    public PayMethodModel() {
    }

    protected PayMethodModel(Parcel in) {
        this.whaleyCurrency = in.readInt();
        this.weixin = in.readInt();
        this.alipay = in.readInt();
    }

    public static final Parcelable.Creator<PayMethodModel> CREATOR = new Parcelable.Creator<PayMethodModel>() {
        @Override
        public PayMethodModel createFromParcel(Parcel source) {
            return new PayMethodModel(source);
        }

        @Override
        public PayMethodModel[] newArray(int size) {
            return new PayMethodModel[size];
        }
    };
}

