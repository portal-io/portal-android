package com.whaley.biz.portal.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.common.response.PortalResponse;
import com.whaley.biz.portal.model.PortalCollectModel;
import com.whaley.biz.portal.response.EarningInfoResponse;
import com.whaley.biz.portal.response.PortalRecordResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dell on 2018/8/8.
 */

@BaseUrl(BaseUrls.WHALEY_PORTAL)
public interface PortalApi {

    @GET("account/info")
    Observable<PortalRecordResponse> records(@Query("access_token") String access_token);

    @GET("portal/earnings")
    Observable<EarningInfoResponse> earnings(@Query("access_token") String access_token,@Query("pageNum") int pageNum,@Query("pageSize") int pageSize);

    @Headers("Accept: application/json")
    @POST("portal/collect")
    Observable<PortalResponse> collect(@Body PortalCollectModel portalCollectModel, @Query("access_token")String access_token);

}
