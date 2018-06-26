package com.whaley.biz.program.ui.arrange.repository;

import com.whaley.biz.program.model.CouponModel;

import java.util.List;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

public interface PackageService extends TopicService {
    void setPay(boolean isPay);

    boolean isPay();

    void setChargeable(boolean isChargeable);

    void setCouponModels(List<CouponModel> couponModels);

    void setPayString(String payString);

    boolean isHasBeenPaid();

    void setHasBeenPaid(boolean hasBeenPaid);

    void setGoodsNos(String goodsNos);

    void setGoodsTypes(String goodsTypes);

    String getGoodsNos();

    String getGoodsTypes();

    void setPayList(boolean isPayList);

    void setPackage(boolean isPackage);

    boolean isPayList();
}
