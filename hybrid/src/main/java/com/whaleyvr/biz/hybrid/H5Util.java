package com.whaleyvr.biz.hybrid;

/**
 * Created by YangZhi on 2016/11/16 21:04.
 */

public class H5Util {

    public static String getMimeType(String url){
        String mimeType=null;
        if(url.endsWith(".js")){
            mimeType="application/x-javascript";
        }else if (url.endsWith(".css")){
            mimeType="text/css";
        }else if(url.endsWith(".svg")){
            mimeType="image/svg+xml";
        }else if(url.endsWith(".png")){
            mimeType="image/png";
        }else if(url.endsWith(".jpg")){
            mimeType="image/jpg";
        }
        if(mimeType!=null)
            return mimeType;
        else
            return "text/html";
    }

}
