package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.model.response.GiftListCountResponse;
import com.whaley.biz.program.model.response.GiftMemberCountResponse;
import com.whaley.biz.program.model.response.GiftMyCountResponse;
import com.whaley.biz.program.model.response.GiftTotalCountResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by YangZhi on 2017/10/12 15:53.
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO+"vr-gift/")
public interface VRGiftApi {

    @GET("live/totalCount")
    Observable<GiftTotalCountResponse> totalCount(@Query("liveCode")String liveCode);

    @GET("live/listCount")
    Observable<GiftListCountResponse> listCount(@Query("liveCode")String liveCode);

    @GET("live/memberCount")
    Observable<GiftMemberCountResponse> memberCount(@Query("liveCode")String liveCode);

    @GET("user/myGiftCount")
    Observable<GiftMyCountResponse> myGiftCount(@Query("uid")String uid);
}
