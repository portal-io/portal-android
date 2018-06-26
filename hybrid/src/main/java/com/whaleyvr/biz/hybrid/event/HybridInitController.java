package com.whaleyvr.biz.hybrid.event;

import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.whaley.biz.common.CommonConstants;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.AppFileStorage;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/9.
 */

public class HybridInitController {

    private volatile static HybridInitController instance;

    private HybridInitController() {

    }

    public static HybridInitController init() {
        if (instance == null) {
            synchronized (HybridInitController.class) {
                if (instance == null) {
                    instance = new HybridInitController();
                }
            }
        }
        return instance;
    }

    SharedPreferences sharedPreferences = AppContextProvider.getInstance().getContext().getSharedPreferences("App_Init", AppContextProvider.getInstance().getContext().MODE_PRIVATE);

    static final String KEY_ZIPH5_ISUNZIPED = CommonConstants.VALUE_APP_VERSION_NAME + "_zipH5_IsUnZiped";

    public void checkH5Zip() {
        boolean isUnZiped = sharedPreferences.getBoolean(KEY_ZIPH5_ISUNZIPED, false);
        if (isUnZiped)
            return;
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                File h5ZipFile = new File(AppFileStorage.getH5Path(), "h5.zip");
                if (h5ZipFile.exists()) {
                    h5ZipFile.delete();
                }
                h5ZipFile.mkdirs();
                h5ZipFile.createNewFile();
                AssetManager assetManager;
                InputStream is = null;
                try {
                    assetManager = AppContextProvider.getInstance().getContext().getAssets();
                    is = assetManager.open("h5.zip");
                    boolean isCopy = FileUtils.writeFile(h5ZipFile, is);
                    if (isCopy) {
                        FileUtils.unZipFile(h5ZipFile.getPath(), AppFileStorage.getH5Path());
                        sharedPreferences.edit().putBoolean(KEY_ZIPH5_ISUNZIPED, true).commit();
                    }
                } catch (IOException e1) {
                    Log.e(e1, "checkH5Zip");
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e2) {
                            //
                        }
                    }
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).subscribe(new DisposableObserver() {
            @Override
            public void onNext(@NonNull Object o) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

}
