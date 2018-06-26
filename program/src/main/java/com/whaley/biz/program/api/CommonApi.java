package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.model.response.StringResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Author: qxw
 * Date:2017/9/12
 * Introduction:
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO + "newVR-service/appservice/")
public interface CommonApi {
//    @GET("stat/findBySrcCode?")
//    Call<CountsResponse> requestStatistic(@Query("srcCode") String srcCode);
//
//    @FormUrlEncoded
//    @POST("stat/updateBySrcCode")
//    Call<CountsResponse> updateCount(@Field("srcCode") String srcCode, @Field("programType") String programType, @Field("videoType") String videoType,
//                                     @Field("type") String type);

    @Headers("Is-Signature:user_history")
    @FormUrlEncoded
    @POST("userHistory/add")
    Observable<StringResponse> add(@Field("uid") String uid,
                                   @Field("devideId") String devideId,
                                   @Field("playTime") String playTime,
                                   @Field("playStatus") String playStatus,
                                   @Field("programCode") String programCode,
                                   @Field("programType") String programType,
                                   @Field("dataSource") String dataSource,
                                   @Field("totalPlayTime") String totalPlayTime);


}
