package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.model.response.SearchResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dell on 2017/8/25.
 */

@BaseUrl(BaseUrls.VR_API_AGINOMOTO+"newVR-service/appservice/")
public interface SearchAPI {

    @GET("search/bytitle?v=2")
    Observable<SearchResponse> search(@Query("keyWord") String keyWord);

}