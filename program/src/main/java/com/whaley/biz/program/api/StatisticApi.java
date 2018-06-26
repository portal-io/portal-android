package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.program.model.CountModel;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/8/17.
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO+"newVR-service/appservice/")
public interface StatisticApi {

    @GET("stat/findBySrcCode?")
    Observable<CMSResponse<CountModel>> requestStatistic(@Query("srcCode") String srcCode);

    @FormUrlEncoded
    @POST("stat/updateBySrcCode")
    Observable<CMSResponse<CountModel>> updateCount(@Field("srcCode") String srcCode, @Field("programType") String programType, @Field("videoType") String videoType,
                                     @Field("type") String type);


    @FormUrlEncoded
    @POST("userHistory/add")
    Observable<CMSResponse> add(@Field("uid") String uid, @Field("devideId") String devideId, @Field("playTime") String playTime, @Field("playStatus") String playStatus, @Field("programCode") String programCode, @Field("programType") String programType, @Field("dataSource") String dataSource, @Field("totalPlayTime") String totalPlayTime, @Field("sign") String sign);


}
