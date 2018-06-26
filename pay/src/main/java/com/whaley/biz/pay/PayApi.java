package com.whaley.biz.pay;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.pay.model.FreeViewModel;
import com.whaley.biz.pay.model.program.PayMethodModel;
import com.whaley.biz.pay.model.program.PayResultModel;
import com.whaley.biz.pay.response.CouponInfoResponse;
import com.whaley.biz.pay.response.NormalOrderResponse;
import com.whaley.biz.pay.response.PayResultResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO)
public interface PayApi {

    String PAY = " newVR-report-service/";
    String CHECK = "newVR-service/appservice/";
    String ORDER = PAY + "order/";

    /**
     * 支付返回回调
     *
     * @param orderNo
     * @param payMethod
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST(ORDER + "appPayFinishBack")
    Observable<CMSResponse<String>> appPayFinishBack(@Field("orderNo") String orderNo, @Field("payMethod") String payMethod, @Field("sign") String sign);

    /**
     * 创建第三方订单
     *
     * @param uid
     * @param goodsNo
     * @param goodsType
     * @param price
     * @param payMethod
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST(ORDER + "createNormalOrder")
    Observable<NormalOrderResponse> createNormalOrder(
            @Field("uid") String uid, @Field("goodsNo") String goodsNo,
            @Field("goodsType") String goodsType,
            @Field("price") String price, @Field("payMethod") String payMethod,
            @Field("sign") String sign);

    /**
     * 查询是否付费
     *
     * @param uid
     * @param goodsNo
     * @param goodsType
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST(ORDER + "goodsPayed")
    Observable<PayResultResponse> goodsPayed(
            @Field("uid") String uid, @Field("goodsNo") String goodsNo,
            @Field("goodsType") String goodsType,
            @Field("sign") String sign);

    /**
     * 批量查询是否购买
     *
     * @param uid
     * @param goodsNo
     * @param goodsType
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST(ORDER + "goodsPayedList")
    Observable<CMSResponse<List<PayResultModel>>> goodsPayedList(
            @Field("uid") String uid, @Field("goodsNos") String goodsNo,
            @Field("goodsTypes") String goodsType,
            @Field("sign") String sign);

    /**
     * 支付渠道检查
     *
     * @param appSystem
     * @param appVerion
     * @return
     */

    @GET(PAY + "payMethod/payMethodList")
    Observable<CMSResponse<PayMethodModel>> payMethodList(@Query("appSystem") String appSystem, @Query("appVerion") String appVerion);


    @GET(PAY + "coupon/findByCode")
    Observable<CouponInfoResponse> getCouponInfo(@Query("code") String code, @Query("uid") String uid);


    @GET(CHECK + "liveProgram/freeViewCheck")
    Observable<CMSResponse<FreeViewModel>> freeViewCheck(@Query("code") String code, @Query("deviceno") String deviceno);

}
