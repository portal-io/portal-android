package com.whaley.biz.setting.model;

public class PayDetailsModel {

    private ListModel<PayContentModel> orderListPageCache;
    private String totalNum;
    private String sumAmount;

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

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }
}
