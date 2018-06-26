package com.whaley.biz.program.ui.live.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Created by dell on 2017/8/21.
 */

public class MyReserveRepository extends MemoryRepository {

    private String goodsNos;

    private String goodsType;

    public String getGoodsNos() {
        return goodsNos;
    }

    public void setGoodsNos(String goodsNos) {
        this.goodsNos = goodsNos;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
}
