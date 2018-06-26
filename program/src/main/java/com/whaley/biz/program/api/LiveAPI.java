package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.program.model.response.LiveDetailsResponse;
import com.whaley.biz.program.model.response.LiveListResponse;
import com.whaley.biz.program.model.response.ReserveListResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dell on 2017/8/14.
 */

@BaseUrl(BaseUrls.VR_API_AGINOMOTO+"newVR-service/appservice/")
public interface LiveAPI {

    String TV_FLAG = "?v=1";
    String PATH_LIVE_PROGRAM = "liveProgram/";
    String PATH_LIVE_ORDER = "liveorder/";
    String HOME_LOOK_LIVE = "livereview";

    @GET(PATH_LIVE_ORDER + "list")
    Observable<LiveListResponse> reservation(@Query("uid") String uid, @Query("token") String token, @Query("device_id") String device_id);

    @FormUrlEncoded
    @POST(PATH_LIVE_ORDER + "add")
    Observable<CMSResponse> add(@Field("uid") String uid, @Field("token") String token, @Field("device_id") String device_id, @Field("code") String code);


    @FormUrlEncoded
    @POST(PATH_LIVE_ORDER + "cancel")
    Observable<CMSResponse> cancel(@Field("uid") String uid, @Field("token") String token, @Field("device_id") String device_id, @Field("code") String code);

    @GET(PATH_LIVE_PROGRAM + "findByCode")
    Observable<LiveDetailsResponse> requestLiveDetail(@Query("code") String code, @Query("uid") String uid);

    @GET(PATH_LIVE_ORDER + "listOrderedByUser")
    Observable<ReserveListResponse> requestReserveList(@Query("uid") String uid, @Query("token") String token, @Query("device_id") String device_id);


}
