package com.whaley.biz.program.model.pay;

/**
 * Created by dell on 2017/8/22.
 */

public class GoodsModel {

    private String goodsNo;
    private String goodsType;

    public GoodsModel(String goodsNo, String goodsType){
        this.goodsNo = goodsNo;
        this.goodsType = goodsType;
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
}
