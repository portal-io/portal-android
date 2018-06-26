package com.whaley.biz.pay;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.pay.response.DeviceResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dell on 2017/8/28.
 */

@BaseUrl(BaseUrls.VR_API_AGINOMOTO + "newVR-report-service/userPlayDevice/")
public interface UserPlayDeviceApi {

    @FormUrlEncoded
    @POST("report")
    Observable<DeviceResponse> report(@Field("uid") String uid,
                                      @Field("deviceId") String deviceId, @Field("sign") String sign);

    @FormUrlEncoded @POST("query") Observable<DeviceResponse> query(@Field("uid") String uid,
                                                                    @Field("deviceId") String deviceId, @Field("sign") String sign);

}
