package com.whaleyvr.biz.hybrid;

import android.Manifest;
import android.os.Environment;
import android.support.v4.util.LruCache;

import com.google.gson.Gson;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.FileUtils;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.MD5Util;
import com.whaley.core.utils.PermissionUtil;
import com.whaley.core.utils.StrUtil;

import java.io.File;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2016/9/14 3:07.
 */
public class CacheManager {

    private static final String PERMISSION_READ = Manifest.permission.READ_EXTERNAL_STORAGE;

    private static final String PERMISSION_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private String cachePath;

    private String appDirName;

    private String cacheDirName;

    private Gson gson = GsonUtil.getGson();

    private LruCache<String, Object> memoryCache;

    private Object lock = new Object();


    private CacheManager() {
        memoryCache = new LruCache<>(30);
    }


    private static class SingleTon {
        static final CacheManager instance = new CacheManager();
    }

    public static CacheManager getInstance() {
        return SingleTon.instance;
    }


    public void setAppDirName(String appDirName) {
        this.appDirName = appDirName;
    }

    public void setCacheDirName(String cacheDirName) {
        this.cacheDirName = cacheDirName;
    }


    private boolean checkReadPermission() {
        return PermissionUtil.checkPermission(PERMISSION_READ);
    }

    private boolean checkWritePermission() {
        return PermissionUtil.checkPermission(PERMISSION_WRITE);
    }

    public String getCachePath() {
        if (!StrUtil.isEmpty(this.cachePath)) {
            return this.cachePath;
        }
        String cachePath;
        if (checkExternalStorageState()) {
            cachePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + appDirName + File.separator + cacheDirName;
        } else {
            cachePath = AppContextProvider.getInstance().getContext().getCacheDir().getAbsolutePath() + File.separator + cacheDirName;
        }
        File file = new File(cachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.cachePath = cachePath;
        return cachePath;
    }

    private static boolean checkExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean checkHasCache(String key) {
        synchronized (lock) {
            File file = new File(getCachePath() + File.separator + MD5Util.getMD5String(key));
            return file.exists();
        }
    }

    public void get(final String key, final Type type, final CacheGetter cacheGetter) {
        synchronized (lock) {
            Object data = memoryCache.get(key);
            if (data == null) {
                if (!checkReadPermission()) {
                    return;
                }
                Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                        synchronized (lock) {
                            Object data = null;
                            StringBuilder stringBuilder = FileUtils.readFile(getCachePath() + File.separator + MD5Util.getMD5String(key), "UTF-8");
                            if (stringBuilder != null) {
                                data = gson.fromJson(stringBuilder.toString(), type);
                            }
                            e.onNext(data);
                            e.onComplete();
                        }
                    }
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<Object>() {
                            @Override
                            public void onNext(@NonNull Object o) {
                                if (o != null) {
                                    cacheGetter.onGetCache(o);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                cacheGetter.onFailue(e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } else {
                try {
                    cacheGetter.onGetCache(data);
                } catch (Exception e) {
                    Log.e(e, "CacheManager getCache");
                    cacheGetter.onFailue(e);
                }
            }
        }
    }


    public void get(final String key, final CacheGetter<String> cacheGetter) {
        synchronized (lock) {
            Object data = memoryCache.get(key);
            if (data == null) {
                if (!checkReadPermission()) {
                    return;
                }
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        synchronized (lock) {
                            String json = null;
                            StringBuilder stringBuilder = FileUtils.readFile(getCachePath() + File.separator + MD5Util.getMD5String(key), "UTF-8");
                            if (stringBuilder != null) {
                                json = stringBuilder.toString();
                            }
                            e.onNext(json);
                            e.onComplete();
                        }
                    }
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<String>() {
                            @Override
                            public void onNext(@NonNull String o) {
                                if (o != null) {
                                    cacheGetter.onGetCache((String) o);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                cacheGetter.onFailue(e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } else {
                try {
                    cacheGetter.onGetCache((String) data);
                } catch (Exception e) {
                    Log.e(e, "CacheManager getCache");
                    cacheGetter.onFailue(e);
                }
            }
        }
    }


    public void save(final String key, final Object data) {
        synchronized (lock) {
            memoryCache.put(key, data);
            if (!checkWritePermission()) {
                return;
            }
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                    synchronized (lock) {
                        String json = gson.toJson(data);
                        FileUtils.writeFile(getCachePath() + File.separator + MD5Util.getMD5String(key), json);
                        e.onComplete();
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }


    public void save(final String key, final String jsonData) {
        synchronized (lock) {
            memoryCache.put(key, jsonData);
            if (!PermissionUtil.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                    synchronized (lock) {
                        String json = jsonData;
                        FileUtils.writeFile(getCachePath() + File.separator + MD5Util.getMD5String(key), json);
                        e.onComplete();
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    public interface CacheGetter<T> {
        void onGetCache(T cacheData);

        void onFailue(Throwable throwable);
    }

}
