package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Created by dell on 2017/10/13.
 */

public class CurrencyDetailRepository extends MemoryRepository{

    private String buyNum;

    public String getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(String buyNum) {
        this.buyNum = buyNum;
    }

}
