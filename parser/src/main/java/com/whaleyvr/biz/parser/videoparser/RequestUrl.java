package com.whaleyvr.biz.parser.videoparser;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RequestUrl {

    private static InputStreamReader is = null;
    private static final String UserAgent = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.12 (KHTML, like Gecko) Maxthon/3.0 Chrome/18.0.966.0 Safari/535.12";

    public static String getResponseByMethod(String url, HashMap<String, String> params, int method) {
        String requestString = "";
        HttpClient httpClient = new DefaultHttpClient();

        HttpParams httpParams = httpClient.getParams();
        Header header = new BasicHeader("User-Agent", UserAgent);
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpResponse response = null;
        try {
            if (method == 0) {
                Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    url = url + "&" + entry.getKey().toString() + "=" + entry.getValue().toString();
                }

                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader(header);
                response = httpClient.execute(httpGet);
            } else if (method == 1) {

                HttpPost httpPost = new HttpPost(url);
                List<NameValuePair> postparam = new ArrayList<NameValuePair>();
                // 建立一个NameValuePair数组，用于存储欲传送的参数
                Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    postparam.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
                }
                // 添加参数
                httpPost.setEntity(new UrlEncodedFormEntity(postparam, "UTF-8"));
                httpPost.addHeader(header);
                response = httpClient.execute(httpPost);
            } else {
                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader(header);
                response = httpClient.execute(httpGet);
            }
            if (response.getStatusLine().getStatusCode() == 200) {
                if (response.toString() != null && response.toString() != "") {
                    StringBuilder builder = new StringBuilder();
                    is = new InputStreamReader(response.getEntity().getContent(), "utf-8");
                    BufferedReader bufferedReader = new BufferedReader(is);
                    for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
                        builder.append(s);
                    }

                    requestString = builder.toString();
                }

            } else {
                return "Error";
            }

            is.close();
        } catch (Exception e) {
            return "Error";
        }
        return requestString;
    }

    /**
     * 返回接口内容
     * 
     * @param url
     * @return String JSON string or Error
     */
    public static String getResponseDefult(String url) {
        String responseString = "";
        HttpClient httpClient = new DefaultHttpClient();

        HttpParams httpParams = httpClient.getParams();
        Header header = new BasicHeader("User-Agent", UserAgent);
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpResponse response = null;
        try {

            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader(header);
            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 206) {
                if (response.toString() != null && response.toString() != "") {
                    StringBuilder builder = new StringBuilder();
                    is = new InputStreamReader(response.getEntity().getContent(), "utf-8");
                    BufferedReader bufferedReader = new BufferedReader(is);
                    for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
                        builder.append(s + "\r\n");
                    }

                    responseString = builder.toString();
                }

            } else {
                return "Response status Error";
            }

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return responseString;
    }

    public static Map<String, String> getParameterList(String uri) {
        Map<String, String> paramList = new HashMap<String, String>();
        if (uri == null)
            return paramList;
        int paramIdx = uri.indexOf('?');
        if (paramIdx < 0)
            return paramList;
        while (paramIdx > 0) {
            int eqIdx = uri.indexOf('=', paramIdx + 1);
            String name = uri.substring(paramIdx + 1, eqIdx);
            int nextParamIdx = uri.indexOf('&', eqIdx + 1);
            String value = uri.substring(eqIdx + 1, nextParamIdx > 0 ? nextParamIdx : uri.length());
            paramList.put(name, value);
            paramIdx = nextParamIdx;
        }
        return paramList;
    }
}
