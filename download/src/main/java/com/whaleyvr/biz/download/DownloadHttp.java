package com.whaleyvr.biz.download;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class DownloadHttp {

    private static final int CONNECT_TIMEOUT = 30;

    private OkHttpClient okHttpClient;

    private static class SingleTon{
        public static final DownloadHttp instance=new DownloadHttp();
    }

    public static DownloadHttp getInstance(){
        return SingleTon.instance;
    }


    private DownloadHttp(){
        initHttpClient();
    }


    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    private void initHttpClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient.Builder buider = new OkHttpClient.Builder();
        buider.retryOnConnectionFailure(true)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging);
        okHttpClient = buider.build();
    }

}
