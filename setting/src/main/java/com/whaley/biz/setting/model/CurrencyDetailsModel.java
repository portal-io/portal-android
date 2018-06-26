package com.whaley.biz.setting.model;

/**
 * Created by dell on 2017/10/13.
 */

public class CurrencyDetailsModel {

    private ListModel<PayContentModel> orderListPageCache;
    private String totalNum;
    private String priceAmount;
    private String whaleyCurrencyAmount;

    public ListModel<PayContentModel> getOrderListPageCache() {
        return orderListPageCache;
    }

    public void setOrderListPageCache(ListModel<PayContentModel> orderListPageCache) {
        this.orderListPageCache = orderListPageCache;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(String priceAmount) {
        this.priceAmount = priceAmount;
    }

    public String getWhaleyCurrencyAmount() {
        return whaleyCurrencyAmount;
    }

    public void setWhaleyCurrencyAmount(String whaleyCurrencyAmount) {
        this.whaleyCurrencyAmount = whaleyCurrencyAmount;
    }
}

