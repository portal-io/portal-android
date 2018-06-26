package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.model.response.DanMuBoxSwitchResponse;
import com.whaley.biz.program.model.response.OpsResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by YangZhi on 2016/12/16 15:05.
 */

@BaseUrl(BaseUrls.VR_API_AGINOMOTO+"newVR-service/appservice/")
public interface PlayerApi {

    @GET("live/getLiveConfigByCode")
    Observable<DanMuBoxSwitchResponse> getLiveConfigByCode(@Query("code")String code);

    @GET("liveProgram/getLiveOps")
    Observable<OpsResponse> getLiveOps(@Query("code")String code);

    @GET("program/getProgramOps")
    Observable<OpsResponse> getProgramOps(@Query("code")String code);

}
