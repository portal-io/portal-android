package com.whaleyvr.biz.hybrid;

import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.AppFileStorage;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.MD5Util;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.biz.hybrid.event.DownloadH5PageModel;
import com.whaleyvr.biz.hybrid.event.DownloadH5PageResultModel;
import com.whaleyvr.biz.hybrid.widget.VRWebView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by YangZhi on 2016/11/4 10:21.
 */

public class HybridCacheManager {

    public static final String TAG="HybridCacheManager";

    private CachePolicy cachePolicy;

    private ConcurrentHashMap<Integer,String> callbackMap=new ConcurrentHashMap<>();

    private VRWebView webView;

    private HybridCacheManager(VRWebView vrWebView,CachePolicy cachePolicy){
        this.webView=vrWebView;
        this.cachePolicy=cachePolicy;
    }

    public static HybridCacheManager create(VRWebView vrWebView,CachePolicy cachePolicy){
        return  new HybridCacheManager(vrWebView,cachePolicy);
    }

    public void setWebView(VRWebView webView) {
        this.webView = webView;
    }

    public void setCachePolicy(CachePolicy cachePolicy) {
        this.cachePolicy = cachePolicy;
    }

    public void loadUrl(String url){
        final int cachePoli=cachePolicy.getCachePolicy();
        if(cachePoli==CachePolicy.CACHE_POLICY_NONE){
            webView.loadUrl(url);
            return;
        }
        try {
            String fileName = url;
            Log.d(TAG,"loadUrl url="+fileName);
            //对文件地址做MD5加密
            fileName = MD5Util.getMD5String(fileName);
            File file = new File(AppFileStorage.getH5Path(), fileName);

            if (cachePoli != CachePolicy.CACHE_POLICY_DISK) {
                Log.d(TAG,"loadUrl 下载url");
                DownloadH5PageModel downloadH5PageModel = new DownloadH5PageModel(url, file.getPath());
                int callbackId = downloadH5PageModel.getCallbackId();
                downloadH5PageModel.setShouldLoadUrl(false);
                callbackMap.put(callbackId, url);
                downloadH5Page(downloadH5PageModel);
            }
        }catch (Exception e){
            Log.e(e,"HybridCacheManager loadUrl");
        }finally {
            webView.loadUrl(url);
        }
    }

    private void downloadH5Page(DownloadH5PageModel downloadH5PageModel){
        Router.getInstance().buildExecutor("/download/service/downloadH5Page")
                .putObjParam(downloadH5PageModel)
                .callback(new Executor.Callback<DownloadH5PageResultModel>() {
                    @Override
                    public void onCall(DownloadH5PageResultModel o) {
                        int callbackId=o.getCallbackId();
                        if(callbackMap.containsKey(callbackId)) {
                            String url=callbackMap.get(callbackId);
                            callbackMap.remove(callbackId);
                        }
                    }
                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        //
                    }
                }).excute();
    }


    public WebResourceResponse onInterceptRequest(String url){
        if (StrUtil.isEmpty(url))
            return null;
        //如果URL是本地assets开头则加载本地asset文件
        if(url.startsWith("file:///android_asset/")) {
            int index = url.indexOf("#");
            if (index > -1) {
                url = url.substring(0, index);
            }
            String fileName = url.replace("file:///android_asset/", "");
//                                url.substring(url.lastIndexOf(File.separator) + 1, url.length());
            InputStream inputStream = null;
            try {
                inputStream = AppContextProvider.getInstance().getContext().getAssets().open(fileName);
                WebResourceResponse resourceResponse = new WebResourceResponse(H5Util.getMimeType(url), "UTF-8", inputStream);
                return resourceResponse;
            } catch (IOException e) {
                Log.e("拦截JS加载本地Asset文件失败 "+e.getMessage());
                e.printStackTrace();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception ex) {

                    }
                }
            }
        }

        FileInputStream fileInputStream=null;
        final int cachePoli=cachePolicy.getCachePolicy();
        if(cachePoli!=CachePolicy.CACHE_POLICY_NONE) {
            try {
                Log.d(TAG,"拦截URL地址");
                //只有html文件 是以完整url作为文件名缓存
                String fileName = url.substring(url.lastIndexOf(File.separator) + 1, url.length());
                if(fileName.contains("#")){
                    fileName=fileName.split("#")[0];
                }
                if (!fileName.contains(".") || fileName.endsWith(".html") || fileName.endsWith(".htm")) {
                    Log.d(TAG,"拦截地址为html文件 "+fileName);
                    fileName = url;
                    if (!StrUtil.isEmpty(fileName)) {
                        //对文件地址做MD5加密
                        fileName = MD5Util.getMD5String(fileName);
                        File file = new File(AppFileStorage.getH5Path(), fileName);

                        WebResourceResponse webResourceResponse = null;
                        if (file.exists()) {
                            Log.d(TAG,"拦截地址为html文件 该文件已存在");
                            fileInputStream = new FileInputStream(file);
                            webResourceResponse = new WebResourceResponse("text/html", "UTF-8", fileInputStream);
                            return webResourceResponse;
                        }
                        if (cachePoli != CachePolicy.CACHE_POLICY_DISK) {
                            Log.d(TAG,"拦截地址为html文件 该文件不存在，前往下载");
                            DownloadH5PageModel downloadH5PageModel = new DownloadH5PageModel(url, file.getPath());
                            downloadH5Page(downloadH5PageModel);
                        }
                    }
                }else {
                    //非Html文件 是以最后一个"/"后的字符串作为文件名
                    fileName = url.substring(url.lastIndexOf(File.separator) + 1, url.length());
                    Log.d(TAG,"拦截地址为资源脚本文件 "+fileName);
                    if (!StrUtil.isEmpty(fileName)) {
                        fileName = MD5Util.getMD5String(fileName);
                        File file = new File(AppFileStorage.getH5Path(), fileName);
                        WebResourceResponse webResourceResponse = null;
                        if (file.exists()) {
                            Log.d(TAG,"拦截地址为资源脚本文件 该文件已存在");
                            fileInputStream = new FileInputStream(file);
                            webResourceResponse = new WebResourceResponse(H5Util.getMimeType(url), "UTF-8", fileInputStream);
                            return webResourceResponse;
                        }
                        if (cachePoli != CachePolicy.CACHE_POLICY_DISK) {
                            Log.d(TAG,"拦截地址为资源脚本文件 该文件不存在，前往下载");
                            DownloadH5PageModel downloadH5PageModel = new DownloadH5PageModel(url, file.getPath());
                            downloadH5Page(downloadH5PageModel);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG,"拦截JS加载文件失败"+e.getMessage());
                Log.e("拦截JS加载本地文件失败 "+e.getMessage());
//                e.printStackTrace();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException io) {

                    }

                }
            }
        }
        return null;
    }

    public void destroy(){
        callbackMap.clear();
        webView=null;
    }

}
