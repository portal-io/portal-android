package com.whaley.biz.program.model.pay;

/**
 * Created by dell on 2017/9/7.
 */

public class PayResultModel {

    private String uid;
    private boolean result;
    private String goodsNo;
    private String goodsType;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public static PayResultModel obtain(String code){
        PayResultModel payResultModel = new PayResultModel();
        payResultModel.setGoodsNo(code);
        return payResultModel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PayResultModel other = (PayResultModel) obj;
        if(this.getGoodsNo()!=null && this.getGoodsNo().equals(other.getGoodsNo()))
            return true;
        return false;
    }

}
