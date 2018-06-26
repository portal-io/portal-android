package com.whaley.biz.setting.interactor;

import android.app.DownloadManager;
import android.app.Service;
import android.database.Cursor;
import android.text.TextUtils;

import com.whaley.biz.setting.update.UpdateHelper;
import com.whaley.biz.setting.update.UpdatingException;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;

/**
 * Author: qxw
 * Date:2018/1/11
 * Introduction:
 */

public class UpdateDownloading extends UseCase<Boolean, Boolean> {

    public UpdateDownloading(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread){
        super(repositoryManager, executeThread, postExecutionThread);
    }
    @Override
    public Observable<Boolean> buildUseCaseObservable(final Boolean aBoolean) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                Boolean isUpdate = false;
                if (UpdateHelper.getInstance().getUpdateId() >= 0) {
                    DownloadManager downloadManager = (DownloadManager) AppContextProvider.getInstance().getContext().getSystemService(Service.DOWNLOAD_SERVICE);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(UpdateHelper.getInstance().getUpdateId());
                    Cursor cursor = downloadManager.query(query);
                    if (cursor != null && cursor.moveToFirst()) {
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                        if (!TextUtils.isEmpty(url)) {
                            switch (status) {
                                case DownloadManager.STATUS_RUNNING:
                                    isUpdate = true;
                                    break;
                            }
                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    if (e.isDisposed())
                        return;
                    if (isUpdate) {
                        e.onError(new UpdatingException());
                    }else {
                        e.onNext(isUpdate);
                        e.onComplete();
                    }
                }else {
                    e.onNext(isUpdate);
                    e.onComplete();
                }
            }
        });
    }
}
