package com.whaleyvr.biz.download;

import com.whaleyvr.biz.download.api.DownloadApi;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DownloadHelper {
    private static final int CONNECT_TIMEOUT = 60;


    private static final int WRITE_TIMEOUT = 60;

    private static final int READ_TIMEOUT = 100;

    private static OkHttpClient okHttpClient;

    private static DownloadApi api;

    private static DownloadApi getDownloadApi(){
        if(api==null) {
            if (okHttpClient == null) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                OkHttpClient.Builder buider = new OkHttpClient.Builder();
                buider.retryOnConnectionFailure(true)
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT,TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)
                        .addInterceptor(logging);
                okHttpClient = buider
                        .build();
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://storeapi-1.snailvr.com/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient).build();
            api = retrofit.create(DownloadApi.class);
        }
        return api;
    }

    public static Observable<ResponseBody> downloadFile(DownloadObserver<ResponseBody> observer){
        return getDownloadObservable(observer,true);
    }


    public static Observable<ResponseBody> getDownloadObservable(DownloadObserver<ResponseBody> observer, boolean isShouldRange) {
        DownloadApi api=getDownloadApi();
        Observable<ResponseBody> observable;
        if(isShouldRange)
            observable=api.download(observer.getDownloadBean().getDownloadUrl(),"bytes="+observer.getDownloadBean().getCurrentSize()+"-");
        else
            observable=api.download(observer.getDownloadBean().getDownloadUrl());
        return observable;
    }

    public static Observable<ResponseBody> getDownloadObservable(DownloadObserver<ResponseBody> observer) {
        return getDownloadObservable(observer,true);
    }

    public static void executeDownload(Observable<ResponseBody> observable, DownloadObserver<ResponseBody> observer){
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribeWith(observer);
    }

}
