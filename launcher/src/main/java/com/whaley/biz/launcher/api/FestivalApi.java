package com.whaley.biz.launcher.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.launcher.model.responce.FestivalResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by dell on 2018/1/23.
 */

@BaseUrl(BaseUrls.VR_API_AGINOMOTO + "newVR-report-service/h5event/")
public interface FestivalApi {

    String EVENT_CODE = "springFestival2018";

    @GET("findEventDetail?code=" + EVENT_CODE)
    Observable<FestivalResponse> findEventDetail();

}
