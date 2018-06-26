package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.model.response.BoxLeftCountResponse;
import com.whaley.biz.program.model.response.DrawCardResponse;
import com.whaley.biz.program.model.response.FestivalResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dell on 2018/1/23.
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO + "newVR-report-service/h5event/")
public interface SpringFestivalApi {

    String EVENT_CODE = "springFestival2018";

    @GET("getLeftCount")
    Observable<BoxLeftCountResponse> getLeftCount(@Query("uid") String uid, @Query("token") String token);

    @FormUrlEncoded
    @POST("drawCard")
    Observable<DrawCardResponse> drawCard(@Field("uid") String uid, @Field("token") String token, @Field("mobile") String mobile,
                                          @Field("eventCode") String eventCode, @Field("ts") String ts, @Field("sign") String sign);


    @FormUrlEncoded
    @POST("shareCall")
    Observable<BoxLeftCountResponse> shareCall(@Field("uid") String uid, @Field("channel") String channel, @Field("token") String token);

    @GET("findEventDetail?code=" + EVENT_CODE)
    Observable<FestivalResponse> findEventDetail();
}
