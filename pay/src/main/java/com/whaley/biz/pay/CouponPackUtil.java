package com.whaley.biz.pay;


import com.whaley.biz.pay.model.CouponModel;
import com.whaley.biz.pay.model.CouponPackModel;
import com.whaley.biz.pay.model.PackageModel;
import com.whaley.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by dell on 2017/8/17.
 */

public class CouponPackUtil {

    public static String TYPE_CONTENT_PACKGE = "content_packge";//节目包类型

    public static CouponPackModel getCouponPackModel(CouponModel couponModel, List<PackageModel> packageModels) {
        CouponPackModel couponPackModel = new CouponPackModel();
        List<CouponModel> couponModelList = new ArrayList<>();
        if (couponModel != null && !StrUtil.isEmpty(couponModel.getPrice())) {
            couponModel.viewName = couponModel.getDisplayName();
            couponModelList.add(couponModel);
            couponPackModel.price = getPrice(couponModel);
        }
        if (packageModels != null && packageModels.size() > 0) {
            for (PackageModel packageModel : packageModels) {
                if (packageModel != null && packageModel.getCouponDto() != null) {
                    String price = getContent(packageModel.getType());
                    packageModel.getCouponDto().viewName = price;
                    packageModel.getCouponDto().isTopic = false;
                    couponModelList.add(packageModel.getCouponDto());
                    if (packageModel.getType() == 0) {
                        couponPackModel.isSet = true;
                        couponPackModel.price = getPrice(packageModel.getCouponDto());
                    }
                }
            }
        }
        couponPackModel.couponModelList = couponModelList;
        return couponPackModel;
    }

    public static String getContent(int type) {
        if (type == 0) {
            return "购买该合集";
        }
        return "购买该节目包(推荐)";
    }

    private static String getPrice(CouponModel couponModel) {
        String price;
        if (TYPE_CONTENT_PACKGE.equals(couponModel.getRelatedType())) {
            price = String.format(Locale.CHINESE, "购买合集观看券 ¥%s",
                    PayUtil.fromFenToYuan(couponModel.getPrice()));
        } else {
            price = String.format(Locale.CHINESE, "购买观看券 ¥%s",
                    PayUtil.fromFenToYuan(couponModel.getPrice()));
        }
        return price;
    }


    public static String add(String original, String add) {
        if (StrUtil.isEmpty(original)) {
            original = add;
        } else {
            original = original + "," + add;
        }
        return original;
    }


}
