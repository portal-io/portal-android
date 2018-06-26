package com.whaley.biz.common.http;


import com.whaley.biz.common.constants.BaseUrls;
import com.whaley.biz.sign.SignType;
import com.whaley.biz.sign.SignUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.DebugUtil;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.core.network.http.GsonConverterFactory;
import com.whaleyvr.core.network.http.HttpManager;
import com.whaleyvr.core.network.http.TestUrlProvider;
import com.whaleyvr.core.network.http.interceptor.AddCookiesInterceptor;
import com.whaleyvr.core.network.http.interceptor.ReceivedCookiesInterceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by yangzhi on 16/8/5.
 */
public class HttpProvider {

    private static TestUrlProvider testUrlProvider;

    private static final int CONNECT_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;

    private static final String BASE_URL = "http://storeapi-1.snailvr.com/";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static Retrofit.Builder createRetrofitBuilder() {
        final Map<String, String> KEY_MAP = new HashMap<String, String>() {
            {

//                put(KEY_APP_NAME, VALUE_APP_NAME);
//                put(KEY_APP_VERSION, VALUE_APP_VERSION);
//                put(KEY_APP_VERSION_CODE, VALUE_APP_VERSION_CODE);
//                put(KEY_SYSTEM_NAME, VALUE_SYSTEM_NAME);
//                put(KEY_SYSTEM_VERSION, VALUE_SYSTEM_VERSION);
            }
        };
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                //添加公共参数
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();
                        HttpUrl.Builder builder = originalHttpUrl.newBuilder();
                        Iterator<Map.Entry<String, String>> iter = KEY_MAP.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<String, String> entry = iter.next();
                            String key = entry.getKey();
                            String value = entry.getValue();
                            if (!originalHttpUrl.toString().contains(key + "=")) {
                                builder.addQueryParameter(key, value);
                            }
                        }
                        HttpUrl url = builder.build();
                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url);
                        requestBuilder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
                        Request request = requestBuilder.build();
                        String isSignature = request.header("Is-Signature");
                        if (!StrUtil.isEmpty(isSignature)) {
                            if (isSignature.contains("whaleyvr")) {
                                request = addSign(request, SignType.TYPE_WHALEYVR);
                            } else if (isSignature.contains("pay")) {
                                request = addSign(request, SignType.TYPE_PAY);
                            } else if (isSignature.contains("show")) {
                                request = addSign(request, SignType.TYPE_SHOW);
                            } else if (isSignature.contains("user_history")) {
                                request = addSign(request, HttpManager.getInstance().isTest() ? SignType.TYPE_TEST_USER_HISTORY : SignType.TYPE_USER_HISTORY);
                            } else if (isSignature.contains("currency")) {
                                request = addSign(request, HttpManager.getInstance().isTest() ? SignType.TYPE_TEST_CURRENCY : SignType.TYPE_CURRENCY);
                            }
                        }
                        return chain.proceed(request);
                    }

//                    private Request addCommonParams(Request request) throws UnsupportedEncodingException {
//                        if (request.method().equals("POST")) {
//                            if (request.body() instanceof FormBody) {
//                                FormBody.Builder bodyBuilder = new FormBody.Builder();
//                                FormBody formBody = (FormBody) request.body();
//
//                                Iterator<Map.Entry<String, String>> iter = KEY_MAP.entrySet().iterator();
//                                while (iter.hasNext()) {
//                                    Map.Entry<String, String> entry = iter.next();
//                                    String key = entry.getKey();
//                                    String value = entry.getValue();
//                                    bodyBuilder.addEncoded(key, value);
//                                }
//                                formBody = bodyBuilder.build();
//                                request = request.newBuilder().post(formBody).build();
//                            }
//                        }
//                        return request;
//                    }


                    private Request addSign(Request request, int signType) throws UnsupportedEncodingException {
                        if (request.method().equals("GET")) {
                            HttpUrl httpUrl = request.url();
                            Set<String> nameSet = httpUrl.queryParameterNames();
                            Iterator<String> nameIterator = nameSet.iterator();
                            SignUtil.SignBuilder builder = SignUtil.builder();
                            builder.signType(signType);
                            while (nameIterator.hasNext()) {
                                String name = nameIterator.next();
                                String value = httpUrl.queryParameterValues(name).size() > 0 ? httpUrl.queryParameterValues(name).get(0) : "";
                                builder.put(name, value);
                            }
                            httpUrl = httpUrl.newBuilder().addQueryParameter("sign", builder.getSign()).build();
                            request = request.newBuilder().url(httpUrl).build();
                        } else if (request.method().equals("POST")) {
                            if (request.body() instanceof FormBody) {
                                FormBody.Builder bodyBuilder = new FormBody.Builder();
                                FormBody formBody = (FormBody) request.body();
                                SignUtil.SignBuilder builder = SignUtil.builder();
                                builder.signType(signType);
                                for (int i = 0; i < formBody.size(); i++) {
                                    bodyBuilder.addEncoded(formBody.name(i), formBody.value(i));
                                    builder.put(formBody.encodedName(i), URLDecoder.decode(formBody.encodedValue(i), "UTF-8"));
                                }
                                String isCommonParameters = request.header("Is-CommonParameters");
                                if (!StrUtil.isEmpty(isCommonParameters) && isCommonParameters.contains("true")) {
                                    Iterator<Map.Entry<String, String>> iter = KEY_MAP.entrySet().iterator();
                                    while (iter.hasNext()) {
                                        Map.Entry<String, String> entry = iter.next();
                                        String key = entry.getKey();
                                        String value = entry.getValue();
                                        bodyBuilder.addEncoded(key, value);
                                        builder.put(key, URLDecoder.decode(value, "UTF-8"));
                                    }
                                }
                                formBody = bodyBuilder.addEncoded("sign", builder.getSign()).build();
                                request = request.newBuilder().post(formBody).build();
                            }
                        }
                        return request;
                    }
                })
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor());
        if (Debug.isDebug())
            okHttpBuilder.addInterceptor(logging);
        OkHttpClient client = okHttpBuilder
                .build();
        Retrofit.Builder builder = new Retrofit.Builder().client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        return builder;
    }

    public static TestUrlProvider getTestUrlProvider() {
        if (testUrlProvider == null)
            testUrlProvider = new TestUrlProvider() {
                @Override
                public String getTestUrl(String baseUrl) {
                    String testUrl = baseUrl;
                    if (baseUrl.contains(BaseUrls.VR_API_AGINOMOTO)) {
                        testUrl =  baseUrl.replace(BaseUrls.VR_API_AGINOMOTO, BaseUrls.VR_API_AGINOMOTO_TEST);
                    } else if (baseUrl.contains(BaseUrls.STORE_API)) {
                        testUrl =  baseUrl.replace(BaseUrls.STORE_API, BaseUrls.STORE_API_TEST);
                    } else if (baseUrl.contains(BaseUrls.WHALEY_ACCOUNT)) {
                        testUrl =  baseUrl.replace(BaseUrls.WHALEY_ACCOUNT, BaseUrls.WHALEY_ACCOUNT_TEST);
                    } else if (baseUrl.contains(BaseUrls.SHOW_API_SNAILVR)) {
                        testUrl =  baseUrl.replace(BaseUrls.SHOW_API_SNAILVR, BaseUrls.SHOW_API_SNAILVR_TEST);
                    } else if (baseUrl.contains(BaseUrls.UPDATE_API)) {
                        testUrl =  baseUrl.replace(BaseUrls.UPDATE_API, BaseUrls.UPDATE_API_TEST);
                    } else if (baseUrl.contains(BaseUrls.VR_API)) {
                        testUrl =  baseUrl.replace(BaseUrls.VR_API, BaseUrls.VR_API_TEST);
                    }
                    return testUrl;
                }
            };
        return testUrlProvider;
    }
}
