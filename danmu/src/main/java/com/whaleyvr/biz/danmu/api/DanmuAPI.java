package com.whaleyvr.biz.danmu.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaleyvr.biz.danmu.model.DmModel;
import com.whaleyvr.biz.danmu.response.AuthResponse;
import com.whaleyvr.biz.danmu.response.ComeInReponse;
import com.whaleyvr.biz.danmu.response.DmComitResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dell on 2017/5/8.
 */

@BaseUrl(BaseUrls.SHOW_API_SNAILVR)
public interface DanmuAPI {

    @Headers("Is-Signature:show")
    @GET("user/auth")
    Observable<AuthResponse> auth(@Query("accesstoken") String accesstoken, @Query("device_id") String device_id, @Query("model") String model, @Query("timestamp") String timestamp);

    @Headers("Is-Signature:show")
    @GET("user/guest_auth")
    Observable<AuthResponse> guestAuth(@Query("device_id") String device_id, @Query("model") String model, @Query("timestamp") String timestamp);


    @Headers("Is-Signature:show")
    @GET("room/comein?type=video")
    Observable<ComeInReponse> comein(@Query("sid") String sid, @Query("title") String title, @Query("timestamp") String timestamp);

    @Headers("Accept: application/json")
    @POST("room/danmaku")
    Observable<DmComitResponse> comitDm(@Body DmModel danmuModel);

}
