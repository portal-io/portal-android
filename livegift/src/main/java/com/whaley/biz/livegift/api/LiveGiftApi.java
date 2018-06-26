package com.whaley.biz.livegift.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.livegift.model.response.GiftTempInfoResponse;
import com.whaley.biz.livegift.model.response.MemberTemplateResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author: qxw
 * Date:2017/10/12
 * Introduction:
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO)
public interface LiveGiftApi {

    @GET("vr-gift/gift/giftTempInfo?pageNum=0&pageSize=50")
    Observable<GiftTempInfoResponse> getGiftTempInfo(@Query("giftTempCode") String giftTempCode);

    @GET("/newVR-service/appservice/template/findByLiveCode")
    Observable<MemberTemplateResponse> findByLiveCode(@Query("code") String code);

}
