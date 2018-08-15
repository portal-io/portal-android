package com.whaley.biz.user.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.user.model.portal.AccountModel;
import com.whaley.biz.user.model.portal.SyncResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by dell on 2018/8/8.
 */

@BaseUrl(BaseUrls.WHALEY_PORTAL + "account/")
public interface PortalApi {

    @Headers("Accept: application/json")
    @POST("sync")
    Observable<SyncResponse> sync(@Body AccountModel addressModel);

}
