package com.whaley.biz.setting.update;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.database.Cursor;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.analytics.AnalyticsConfig;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.CommonCMSFunction;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.setting.api.UpdateAPI;
import com.whaley.biz.setting.interactor.Update;
import com.whaley.biz.setting.model.UpdateModel;
import com.whaley.biz.setting.response.UpdateResponse;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.utils.GsonUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/9/5.
 */

public class UpdateHelper implements CommonConstants {

    private final static int IS_UPDATE = 1;

    private long updateId = -1;

    private volatile static UpdateHelper instance;

    public static UpdateHelper getInstance() {
        if (instance == null) {
            synchronized (UpdateHelper.class) {
                if (instance == null) {
                    instance = new UpdateHelper();
                }
            }
        }
        return instance;
    }

    public void checkIfOrNotUpdating(final boolean isSetting, UpdateUIObserver updateUIObserver) {
        Update update = new Update(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        update.buildUseCaseObservable(isSetting)
                .map(new Function<UpdateModel, String>() {
                    @Override
                    public String apply(@NonNull UpdateModel updateModel) throws Exception {
                        if(updateModel.getIsUpdate()==IS_UPDATE){
                            return GsonUtil.getGson().toJson(updateModel);
                        }else {
                            throw new StatusErrorThrowable(0,"");
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(updateUIObserver);
    }

//    public Disposable checkIfOrNotUpdating(final boolean isShowToast) {
//        return Observable.create(new ObservableOnSubscribe<Boolean>() {
//                @Override
//                public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
//                    Boolean isUpdate = false;
//                    if(updateId>=0) {
//                        DownloadManager downloadManager = (DownloadManager) AppContextProvider.getInstance().getContext().getSystemService(Service.DOWNLOAD_SERVICE);
//                        DownloadManager.Query query = new DownloadManager.Query();
//                        query.setFilterById(updateId);
//                        Cursor cursor = downloadManager.query(query);
//                        if (cursor != null && cursor.moveToFirst()) {
//                            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
//                            String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
//                            if (!TextUtils.isEmpty(url)) {
//                                switch (status) {
//                                    case DownloadManager.STATUS_RUNNING:
//                                        isUpdate = true;
//                                        break;
//                                }
//                            }
//                        }
//                        if (cursor != null) {
//                            cursor.close();
//                        }
//                    }
//                    if(isUpdate){
//                        e.onError(new UpdatingException());
//                    }else {
//                        e.onNext(isUpdate);
//                        e.onComplete();
//                    }
//                }
//            }).flatMap(new Function<Boolean, ObservableSource<UpdateModel>>() {
//            @Override
//            public ObservableSource<UpdateModel> apply(@NonNull Boolean aBoolean) throws Exception {
//                return RepositoryManager.create(null).obtainRemoteService(UpdateAPI.class)
//                        .update(VALUE_APP_VERSION_CODE, VALUE_MAC_ADDRESS,
//                                VALUE_DEVICE_MODEL, VALUE_DEVICE_SERIAL, VALUE_VERSION_RELEASE,
//                                VALUE_MAC_ADDRESS, AnalyticsConfig.getChannel(AppContextProvider.getInstance().getContext()))
//                        .map(new CommonCMSFunction<UpdateResponse, UpdateModel>());
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableObserver<UpdateModel>() {
//                    @Override
//                    public void onNext(@NonNull UpdateModel updateModel) {
//                        if(updateModel!=null&&updateModel.getIsUpdate()==IS_UPDATE){
////                            if(activity!=null) {
////                                UpdateDialog.showDialog(activity, updateModel.getFilePath(), updateModel.getVersionName(),
////                                        updateModel.getDescription(), updateModel.getUpdateType());
////                            }
//                        }else{
//                            if(isShowToast) {
//                                showToast("当前已是最新版本，无需更新");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        if(isShowToast) {
//                            if (e instanceof UpdatingException) {
//                                showToast("新版本正在下载中，请稍候");
//                            } else if (e instanceof StatusErrorThrowable) {
//                                showToast("当前已是最新版本，无需更新");
//                            } else {
//                                showToast("检查版本更新失败");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

    private void showToast(String text) {
        Toast.makeText(AppContextProvider.getInstance().getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(long updateId) {
        this.updateId = updateId;
    }
}
