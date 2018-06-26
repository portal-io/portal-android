package com.whaley.biz.setting.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.setting.model.AddressModel;
import com.whaley.biz.setting.response.AddressResponse;
import com.whaley.biz.setting.response.GiftListResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

@BaseUrl(BaseUrls.VR_API)
public interface GiftAPI {

    @Headers("Is-Signature: whaleyvr")
    @GET("user/myprize")
    Observable<GiftListResponse> requestMyGift(@Query("whaleyuid") String whaleyuid, @Query("timestamp") String timestamp);


    @Headers("Is-Signature: whaleyvr")
    @GET("user/address")
    Observable<AddressResponse> requestAddress(@Query("whaleyuid") String whaleyuid, @Query("timestamp") String timestamp);


    @Headers("Accept: application/json")
    @POST("user/address")
    Observable<AddressResponse> saveAddress(@Body AddressModel addressModel);

}
