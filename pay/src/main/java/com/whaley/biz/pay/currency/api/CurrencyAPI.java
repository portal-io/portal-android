package com.whaley.biz.pay.currency.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.pay.currency.response.CurrencyCostResponse;
import com.whaley.biz.pay.currency.response.UserAmountResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dell on 2017/10/12.
 */

@BaseUrl(BaseUrls.VR_API_AGINOMOTO)
public interface CurrencyAPI {

    String PREFIX = "newVR-report-service/whaleyCurrency/";

    @Headers("Is-Signature:currency")
    @POST(PREFIX+"userCost")
    @FormUrlEncoded
    Observable<CurrencyCostResponse> userCost(@Field("uid") String uid, @Field("nickName") String nickName, @Field("userHeadUrl") String userHeadUrl,
                                              @Field("buyType") String buyType, @Field("buyParams") String buyParams, @Field("bizParams") String bizParams);

    @Headers("Is-Signature:currency")
    @GET(PREFIX+"userAmount")
    Observable<UserAmountResponse> userAmount(@Query("uid") String uid);

}
