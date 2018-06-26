package com.whaleyvr.biz.hybrid;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by dell on 2017/8/8.
 */

public interface H5Api {

    @GET
    Call<ResponseBody> requestGet(@Url String url, @HeaderMap Map<String, String> headersMap, @QueryMap Map<String, String> queryMap);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> requestPost(@Url String url, @HeaderMap Map<String, String> headersMap, @FieldMap Map<String, String> fieldMap);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST
    Call<ResponseBody> requestPost(@Url String url, @HeaderMap Map<String, String> headersMap, @Body RequestBody body);


}
