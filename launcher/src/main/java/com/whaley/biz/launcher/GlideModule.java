package com.whaley.biz.launcher;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.FifoPriorityThreadPoolExecutor;
import com.whaley.core.debug.logger.Log;

/**
 * Created by YangZhi on 2017/9/26 19:00.
 */

public class GlideModule implements com.bumptech.glide.module.GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCacheService(new FifoPriorityThreadPoolExecutor(2));
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context.getApplicationContext(), "image_cache", 1024 * 1024 * 400));
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        BitmapPool bitmapPool;
        int bitmapPoolSize = calculator.getBitmapPoolSize();
        int memoryCacheSize = calculator.getMemoryCacheSize();
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            bitmapPoolSize = (int) (bitmapPoolSize * 0.8f);
//            memoryCacheSize = (int) (memoryCacheSize * 0.8f);
//        }
        bitmapPool = new LruBitmapPool(bitmapPoolSize);
        Log.d("GlideModule", "BitmapPoolSize size = " + bitmapPoolSize);
        Log.d("GlideModule", "MemoryCache size = " + memoryCacheSize);
        Log.d("GlideModule", "Max size = " + ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass());
        Log.d("GlideModule", "Large Max size = " + ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getLargeMemoryClass());
        MemoryCache memoryCache = new LruResourceCache(memoryCacheSize);
        builder.setBitmapPool(bitmapPool);
        builder.setMemoryCache(memoryCache);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
