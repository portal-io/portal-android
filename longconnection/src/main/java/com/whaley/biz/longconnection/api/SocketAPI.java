package com.whaley.biz.longconnection.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.longconnection.model.AuthResponse;
import com.whaley.biz.longconnection.model.ComeInReponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by dell on 2017/5/8.
 */

@BaseUrl(BaseUrls.SHOW_API_SNAILVR)
public interface SocketAPI {

    @Headers("Is-Signature:show")
    @GET("user/auth")
    Observable<AuthResponse> auth(@Query("accesstoken") String accesstoken, @Query("device_id") String device_id, @Query("model") String model, @Query("timestamp") String timestamp);

    @Headers("Is-Signature:show")
    @GET("user/guest_auth")
    Observable<AuthResponse> guestAuth(@Query("device_id") String device_id, @Query("model") String model, @Query("timestamp") String timestamp);


    @Headers("Is-Signature:show")
    @GET("room/comein?type=video")
    Observable<ComeInReponse> comein(@Query("sid") String sid, @Query("title") String title, @Query("timestamp") String timestamp);

}
