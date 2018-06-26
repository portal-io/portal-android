package com.whaley.biz.longconnection;

import android.support.v4.util.LruCache;

/**
 * Author: qxw
 * Date:2017/10/18
 * Introduction:
 */

public class CacheManager {
    public static LruCache<String, LongConnectionController> lruCache = new LruCache<>(5);

}
