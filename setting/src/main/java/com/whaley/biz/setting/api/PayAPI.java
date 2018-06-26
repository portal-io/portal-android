package com.whaley.biz.setting.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.common.response.ListTabResponse;
import com.whaley.biz.setting.model.RedemptionCodeModel;
import com.whaley.biz.setting.response.PayDetailsResponse;
import com.whaley.biz.setting.response.RedeemCodeResponse;
import com.whaley.biz.setting.response.RedemptionCodeRespose;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@BaseUrl(BaseUrls.VR_API_AGINOMOTO)
public interface PayAPI {

    @POST("newVR-report-service/order/userCouponOrderList")
    @FormUrlEncoded
    Observable<PayDetailsResponse> userCouponOrderList(@Field("uid") String uid, @Field("page") int page, @Field("size") int size, @Field("sign") String sign);

    @POST("newVR-report-service/userCoupon/couponList")
    @FormUrlEncoded
    Observable<RedemptionCodeRespose> couponList(@Field("uid") String uid, @Field("page") int page, @Field("size") int size, @Field("sign") String sign);

    @GET("newVR-report-service/redeemCode/listUserUnRedeemed")
    Observable<RedeemCodeResponse> listUserUnRedeemed(@Query("uid") String uid, @Query("phoneNum") String phoneNum, @Query("sign") String sign);

    @POST("newVR-report-service/redeemCode/userDoRedeem")
    @FormUrlEncoded
    Observable<CMSResponse<RedemptionCodeModel>> userDoRedeem(@Field("uid") String uid, @Field("redeemCode") String redeemCode, @Field("sign") String sign);

}
