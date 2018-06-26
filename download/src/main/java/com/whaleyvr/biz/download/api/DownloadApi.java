package com.whaleyvr.biz.download.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadApi {

    /**
     * @param fileUrl
     * @param range   "bytes=currentSize-"
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String fileUrl, @Header("Range") String range);


    /**
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String fileUrl);
}
