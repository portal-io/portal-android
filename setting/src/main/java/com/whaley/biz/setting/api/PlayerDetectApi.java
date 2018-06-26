package com.whaley.biz.setting.api;

import com.whaley.biz.common.response.Response;
import com.whaley.biz.setting.model.DectectInfo;

import io.reactivex.Observable;

import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by dell on 2017/9/4.
 */

public interface PlayerDetectApi {
    @POST("http://storeapi.snailvr.com/client/player")
    Observable<Response> comitDetectResult(@Body DectectInfo dectectInfo);
}
