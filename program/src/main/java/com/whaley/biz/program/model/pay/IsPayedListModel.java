package com.whaley.biz.program.model.pay;

/**
 * Created by dell on 2017/9/7.
 */

public class IsPayedListModel {

    private String goodsNos;
    private String goodsTypes;

    public IsPayedListModel(String goodsNos, String goodsTypes){
        this.goodsNos = goodsNos;
        this.goodsTypes = goodsTypes;
    }

    public String getGoodsNos() {
        return goodsNos;
    }

    public void setGoodsNos(String goodsNos) {
        this.goodsNos = goodsNos;
    }

    public String getGoodsTypes() {
        return goodsTypes;
    }

    public void setGoodsTypes(String goodsTypes) {
        this.goodsTypes = goodsTypes;
    }
}
