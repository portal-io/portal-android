package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.response.ProgramDetailResponse;
import com.whaley.biz.program.model.response.ProgramDramaDetailResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yangzhi on 2017/8/15.
 */
@BaseUrl(BaseUrls.VR_API_AGINOMOTO)
public interface ProgramApi {

    @GET(ProgramConstants.AGINOMOTO_APPSERVICE+"program/findByCode?s=1")
    Observable<ProgramDetailResponse> requestProgramDetail(@Query("code") String code, @Query("uid") String uid);

    @GET("newVR-service/dynaprogram/findByCode")
    Observable<ProgramDramaDetailResponse> requestProgramDramaDetail(@Query("code") String code, @Query("uid") String uid);

}
