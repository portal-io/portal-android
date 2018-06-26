package com.whaleyvr.biz.parser.provider;

import android.content.Context;
import android.support.v4.util.LruCache;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.peersless.security.Security;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;

/**
 * Created by dell on 2017/9/19.
 */

@Route(path = "/parser/service/decrypt")
public class DecryptExecutor implements Executor<String> {

    static final LruCache<String,String> MEMORY_CACHE = new LruCache<>(20);

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(String s, final Callback callback) {
        String cache = MEMORY_CACHE.get(s);
        if(cache!=null){
            callback.onCall(cache);
        }else {
            String decryptedUrl = Security.GetInstance().standardDecrypt(s, Security.ALG_HELIOS_PAY);
            MEMORY_CACHE.put(s, decryptedUrl);
            callback.onCall(decryptedUrl);
        }
    }

}
