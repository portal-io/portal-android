package com.whaleyvr.biz.download.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.core.appcontext.AppFileStorage;
import com.whaley.core.router.Executor;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/9/26.
 */

@Route(path = "/download/service/downloadDirInit")
public class DownloadDirInitService implements Executor {

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(Object o, Callback callback) {
        initDownloadDir();
    }

    private void initDownloadDir(){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Object> e) throws Exception {
                if(SharedPreferencesUtil.isFirstInstall()) {
                    SharedPreferencesUtil.setFirstInstall(false);
                    deleteFileList(AppFileStorage.getDownloadPath());
                    deleteFileList(AppFileStorage.getDownloadMoviePath());
                }
                File file = new File(AppFileStorage.getImagePath(), ".nomedia");
                if (!file.exists()) {
                    file.createNewFile();
                }
                file = new File(AppFileStorage.getDownloadPath(), ".nomedia");
                if (!file.exists()) {
                    file.createNewFile();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Object o) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void deleteFileList(String strPath) {
        File dir = new File(strPath);
        if(!dir.exists())
            return;
        File[] files = dir.listFiles();
        if (files == null || files.length == 0)
            return;
        for (File file : files) {
            if (file.isDirectory()) {
                deleteFileList(file.getAbsolutePath());
                file.delete();
            }else {
                file.delete();
            }
        }
    }

}
