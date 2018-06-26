package com.whaley.biz.setting.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.setting.response.UpdateResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author: qxw
 * Date:2017/8/28
 * Introduction:
 */
@BaseUrl(BaseUrls.UPDATE_API)
public interface UpdateAPI {
    @GET("upgrade?appName=smurfs&series=WHALEY_VR_APP")
    Observable<UpdateResponse> update(@Query("version") String version, @Query("mac") String mac,
                                      @Query("ProductModel") String productModel,
                                      @Query("ProductSerial") String productSerial, @Query("ProductVersion") String productVersion,
                                      @Query("WifiMac") String wifiMac, @Query("promotionChannelCode") String promotionChannelCode);
}
