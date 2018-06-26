package com.whaley.biz.program.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.program.model.response.BoxAuthResponse;
import com.whaley.biz.program.model.response.BoxLotteryResponse;
import com.whaley.biz.program.model.response.BoxTimeResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by YangZhi on 2016/11/28 17:18.
 */

@BaseUrl(BaseUrls.VR_API)
public interface BoxApi {
    String ACTION_TYPE = "baoxiang";//宝箱抽奖暂时写死

    @Headers("Is-Signature: whaleyvr")
    @GET("user/auth")
    Observable<BoxAuthResponse> auth(@Query("accesstoken") String accesstoken,
                                     @Query("device_id") String device_id,
                                     @Query("timestamp") long timestamp);


    @Headers("Is-Signature: whaleyvr")
    @GET("lottery/time")
    Observable<BoxTimeResponse> time(@Query("timestamp") long timestamp, @Query("action") String action, @Query("sortid") String sortid);


    @Headers("Is-Signature: whaleyvr")
    @GET("lottery/start")
    Observable<BoxLotteryResponse> lottery(@Query("timestamp") long timestamp, @Query("action") String action, @Query("sortid") String sortid);
}
