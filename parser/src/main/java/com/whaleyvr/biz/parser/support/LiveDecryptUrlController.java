package com.whaleyvr.biz.parser.support;

import android.text.TextUtils;

import com.peersless.agent.SecurityInfo;
import com.peersless.security.Security;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by YangZhi on 2017/9/7 17:08.
 */

public class LiveDecryptUrlController extends BaseController {

    @Subscribe(priority = 50)
    public void onPrepareStartPlayEvent(PrepareStartPlayEvent prepareStartPlayEvent) {
        if (prepareStartPlayEvent.getMaxPriority() < 50) {
            return;
        }
        PlayData playData = prepareStartPlayEvent.getPlayData();
//        if (playData.getBooleanCustomData("key_need_decrypt_url")) {
//            String decryptedUrl = Security.GetInstance().standardDecrypt(playData.getRealUrl(), Security.ALG_HELIOS_PAY);
//            if (!StrUtil.isEmpty(decryptedUrl)) {
//                playData.setRealUrl(decryptedUrl);
//            }
//        }
        playData.setRealUrl(getLiveUrl(playData.getRealUrl()));
    }

    private String getLiveUrl(String playUrl) {
        String temp[] = null;
        try {
            temp = playUrl.split("&flag");
            return temp[0];
        } catch (Exception e) {

        }
        return playUrl;
    }

    @Subscribe(priority = 1)
    public void onPrepareStartPlayEnd(PrepareStartPlayEvent prepareStartPlayEvent) {
        if(prepareStartPlayEvent.getMaxPriority()<1)
            return;
        setToken(prepareStartPlayEvent.getPlayData());
    }

    private void setToken(PlayData playData) {
        if (playData != null && !TextUtils.isEmpty(playData.getRealUrl())) {
            String path = null;
            Log.d("parser setToken realUrl : " + playData.getRealUrl());
            try {
                path = URLDecoder.decode(playData.getRealUrl(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("parser setToken path : " + path);
            if (!TextUtils.isEmpty(path) && path.contains("?")) {
                String token = path.substring(path.lastIndexOf("?") + 1);
                Log.d("parser setToken token : " + token);
                SecurityInfo.setToken(token);
            }
        }
    }
}
