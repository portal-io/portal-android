package com.whaleyvr.biz.parser.videoparser;

import android.text.TextUtils;

import com.peersless.agent.SecurityInfo;
import com.peersless.security.Security;
import com.peersless.videoParser.callback.IParseCallback;
import com.peersless.videoParser.result.DulElement;
import com.peersless.videoParser.result.ParsedResultInfo;
import com.peersless.videoParser.result.UrlElement;
import com.whaley.core.debug.logger.Log;
import com.whaleyvr.biz.parser.model.VideoAlgorithm;
import com.whaleyvr.biz.parser.model.VideoBitType;
import com.whaleyvr.biz.parser.model.VideoParserBean;
import com.whaleyvr.biz.parser.model.VideoSourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.peersless.videoParser.callback.IParseCallback.ErrorType.ERROR_PARSER_INIT_FAIL;

public class VideoParser {

    private static final String TAG = VideoParser.class.getSimpleName();

    public static Observable<List<VideoParserBean>> parserUrl(final String url) {
        if (VideoParserInit.isAvailable()) {
            return parse(url);
        } else {
            return VideoParserInit.initVideoParser().flatMap(new Function<Boolean, ObservableSource<List<VideoParserBean>>>() {
                @Override
                public ObservableSource<List<VideoParserBean>> apply(@NonNull Boolean aBoolean) throws Exception {
                    if (aBoolean) {
                        return parse(url);
                    } else {
                        return Observable.error(new ParserThrowable(ParserThrowable.MSG_PARSER_INIT_FAIL));
                    }
                }
            });
        }
    }

    private static Observable<List<VideoParserBean>> parse(final String url) {
        final ArrayList<VideoParserBean> btrInfo = new ArrayList<VideoParserBean>();
        Log.d(TAG, "=====Entry Parse===== url : " + url);
        return Observable.create(new ObservableOnSubscribe<List<VideoParserBean>>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<List<VideoParserBean>> e) throws Exception {
                if (url == null || url.isEmpty()) {
                    if (!e.isDisposed())
                        e.onError(new ParserThrowable(ParserThrowable.MSG_ORIGIN_URL_EMPTY));
                } else {
                    com.peersless.videoParser.VideoParser.GetInstance().Parse(url, "", new IParseCallback() {
                        @Override
                        public void onParseEvent(ParseType result, ErrorType errorCode,
                                                 ParsedResultInfo info, String htmlUrl) {
                            Log.d(TAG, "=====Parse Callback===== result : " + result.name());
                            if (result == ParseType.PARSE_OK) {
                                Log.d(TAG, "PARSE_OK");
                                ArrayList<UrlElement> urllist = null;
                                if (info != null) {
                                    Log.d(TAG, "ParsedResultInfo : " + info.toString());
                                    urllist = info.getUrllist();
                                }
                                for (int i = 0; i < urllist.size(); i++) {
                                    UrlElement urlInfo = urllist.get(i);
                                    Log.d(TAG, "urlInfo : " + urlInfo.toString());
                                    VideoParserBean bean = new VideoParserBean();
                                    bean.bitrate = urlInfo.bitrate;
                                    String bittype = urlInfo.bittype;
                                    if (!TextUtils.isEmpty(bittype)) {
                                        switch (bittype) {
                                            case "ST":
                                                bean.videoBitType = VideoBitType.ST;
                                                break;
                                            case "SD":
                                                bean.videoBitType = VideoBitType.SD;
                                                break;
                                            case "HD":
                                                bean.videoBitType = VideoBitType.HD;
                                                break;
                                            case "SDA":
                                                bean.videoBitType = VideoBitType.SDA;
                                                break;
                                            case "SDB":
                                                bean.videoBitType = VideoBitType.SDB;
                                                break;
                                            case "TDA":
                                                bean.videoBitType = VideoBitType.TDA;
                                                break;
                                            case "TDB":
                                                bean.videoBitType = VideoBitType.TDB;
                                                break;
                                            default:
                                                continue;
                                        }
                                    } else {
                                        continue;
                                    }
                                    String algorithm = urlInfo.algorithm;
                                    Log.d(TAG, "algorithm : " + algorithm);
                                    if (!TextUtils.isEmpty(algorithm)) {
                                        switch (algorithm) {
                                            case "sphere":
                                                bean.videoAlgorithm = VideoAlgorithm.SPHERE;
                                                break;
                                            case "oct":
                                                bean.videoAlgorithm = VideoAlgorithm.OCT;
                                                break;
                                            default:
                                                bean.videoAlgorithm = VideoAlgorithm.SPHERE;
                                                break;
                                        }
                                    } else {
                                        bean.videoAlgorithm = VideoAlgorithm.SPHERE;
                                    }
                                    ArrayList<String> list = new ArrayList<String>();
                                    ArrayList<DulElement> dullist = urlInfo.dullist;
                                    for (int k = 0; k < dullist.size(); k++) {
                                        DulElement dulInfo = dullist.get(k);
                                        list.add(k, dulInfo.url);
                                    }
                                    String realurl = list.toString();
                                    bean.url = realurl.substring(1, realurl.length() - 1);
                                    btrInfo.add(bean);
                                }
                                Log.d(TAG, "btrInfo size : " + btrInfo.size());
                                HashMap<String, String> headers = info.getHeadMap();
                                String urlExt = info.getCurext();
                                Iterator<VideoParserBean> it = btrInfo.iterator();
                                while (it.hasNext()) {
                                    VideoParserBean bean = it.next();
                                    Log.d(TAG, "isMoguv : " + info.isMoguv);
                                    if (info.isMoguv == 1) {
                                        bean.videoSourceType = VideoSourceType.MOGUV;
                                        bean.url = handleMoguvSecurity(bean.url, urlExt,
                                                headers);
                                    } else {
                                        bean.videoSourceType = VideoSourceType.NONE_MOGUV;
                                    }
                                    Log.d(TAG, "real url :" + bean.url);
                                }
                                if (btrInfo.size() <= 0) {
                                    VideoParserBean bean = new VideoParserBean();
                                    bean.videoBitType = VideoBitType.ST;
                                    bean.videoAlgorithm = VideoAlgorithm.SPHERE;
                                    bean.videoSourceType = VideoSourceType.NONE_MOGUV;
                                    bean.bitrate = 0;
                                    bean.url = url;
                                    if (bean.url.contains("&flag=")) {
                                        bean.url = bean.url.substring(0, bean.url.indexOf("&flag="));
                                    }
                                    btrInfo.add(bean);
                                }
                                if (!e.isDisposed()) {
                                    e.onNext(btrInfo);
                                    e.onComplete();
                                }
                            } else {
                                Log.d(TAG, "PARSE_FAIL");
                                if (errorCode == ERROR_PARSER_INIT_FAIL) {
                                    if (!e.isDisposed())
                                        e.onError(new ParserThrowable(ParserThrowable.MSG_PARSER_INIT_FAIL));
                                } else {
                                    if (!e.isDisposed())
                                        e.onError(new ParserThrowable(errorCode.name()));
                                }
                            }
                        }
                    });
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static String handleMoguvSecurity(String url, String urlExt,
                                              HashMap<String, String> headers) {
        String resolveUrl = null;
        Log.d(TAG, "urlExt : " + urlExt);
        if (urlExt.equalsIgnoreCase("m3u8")) {
            Map<String, String> params = RequestUrl.getParameterList(url);
            String tokenVal = params.get("token");
            String tsVal = params.get("ts");
            String token = "";
            if (tokenVal != null && tokenVal.length() > 0) {
                token = "token=" + tokenVal;
            }
            if (tsVal != null && tsVal.length() > 0) {
                if (token.length() > 0) {
                    token = token + "&ts=" + tsVal;
                } else {
                    token = "ts=" + tsVal;
                }
            }
//            SecurityInfo.setToken(token);
            String playStrSessionId_;
            if (HttpAgent.getInstance().isSupportUrl(url)) {
                playStrSessionId_ = HttpAgent.getInstance().generateUID(url);
                HttpAgent.getInstance().setRequestHeaders(headers);
                resolveUrl = HttpAgent.getInstance().generatePlayUrl(
                        playStrSessionId_, url, urlExt);
            }
        } else {
            resolveUrl = Security.GetInstance().GetUrl(url,
                    Security.ALG_MOGUV_CDN);
        }
        return resolveUrl;
    }

}
