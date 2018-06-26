package com.whaley.biz.setting.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.setting.response.CurrencyDetailsResponse;
import com.whaley.biz.setting.response.CurrencyResponse;
import com.whaley.biz.setting.response.PayDetailsResponse;
import com.whaley.biz.setting.response.UserAmountResponse;
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

    String ORDER_PREFIX = "newVR-report-service/order/";

    @Headers("Is-Signature:currency")
    @GET(PREFIX+"userAmount")
    Observable<UserAmountResponse> userAmount(@Query("uid") String uid);

    @GET(PREFIX+"buyConfigList")
    Observable<CurrencyResponse> buyConfigList();

    @Headers("Is-Signature:currency")
    @POST(ORDER_PREFIX+"userWhaleyCurrencyOrderList")
    @FormUrlEncoded
    Observable<CurrencyDetailsResponse> userWhaleyCurrencyOrderList(@Field("uid") String uid, @Field("page") int page, @Field("size") int size);

}
