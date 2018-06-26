package com.whaley.biz.setting.api;

import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.setting.response.UserHistoryResponse;
import com.whaleyvr.core.network.http.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dell on 2017/8/24.
 */

@BaseUrl(BaseUrls.VR_API_AGINOMOTO + "newVR-service/appservice/userHistory/")
public interface UserHistoryApi {

    String DATA_SOURCE = "app";

    @FormUrlEncoded
    @POST("pageSearch")
    Observable<UserHistoryResponse> pageSearch(@Field("uid") String uid, @Field("devideId") String devideId, @Field("page") String page, @Field("size") String size, @Field("dataSource") String dataSource, @Field("sign") String sign);


    @FormUrlEncoded
    @POST("del")
    Observable<CMSResponse> del(@Field("uid") String uid, @Field("devideId") String devideId, @Field("dataSource") String dataSource, @Field("sign") String sign);

    @FormUrlEncoded
    @POST("delByIds")
    Observable<CMSResponse> delByIds(@Field("dataSource") String dataSource, @Field("uid") String uid, @Field("devideId") String devideId, @Field("delIds") String delIds, @Field("sign") String sign);

}