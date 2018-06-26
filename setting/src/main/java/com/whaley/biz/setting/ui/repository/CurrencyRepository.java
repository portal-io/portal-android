package com.whaley.biz.setting.ui.repository;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Created by dell on 2017/10/12.
 */

public class CurrencyRepository extends MemoryRepository {

    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
