package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.common.response.PortalResponse;
import com.whaley.biz.program.model.portal.PortalVideoModel;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dell on 2018/8/8.
 */

@BaseUrl(BaseUrls.WHALEY_PORTAL + "report/")
public interface PortalApi {

    @Headers("Accept: application/json")
    @POST("play")
    Observable<PortalResponse> play(@Body PortalVideoModel addressModel, @Query("access_token")String access_token);

}
