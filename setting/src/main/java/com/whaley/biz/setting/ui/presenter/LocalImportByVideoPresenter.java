package com.whaley.biz.setting.ui.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.setting.constant.SettingConstants;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.db.LocalVideoBean;
import com.whaley.biz.setting.db.LocalVideoDatabseManager;
import com.whaley.biz.setting.event.ImportVideoEvent;
import com.whaley.biz.setting.event.PermissionGrantedEvent;
import com.whaley.biz.setting.ui.repository.LocalImportByVideoRepository;
import com.whaley.biz.setting.ui.view.LocalImportByVideoView;
import com.whaley.biz.setting.ui.viewmodel.LocalVideoImportViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.AppFileStorage;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.utils.DisplayUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/7.
 */

public class LocalImportByVideoPresenter extends BasePagePresenter<LocalImportByVideoView> {

    @Repository
    LocalImportByVideoRepository repository;

    public LocalImportByVideoPresenter(LocalImportByVideoView view) {
        super(view);
    }

    public LocalImportByVideoRepository getLocalImportByVideoRepository(){
        return repository;
    }

    private Disposable getVideoDisposable, fetchImageDisposable;

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        EventController.regist(this);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (ContextCompat.checkSelfPermission(getUIView().getAttachActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getUIView().getAttachActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    SettingConstants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            getLocalVideo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionGrantedEvent event) {
        if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(event.getType())) {
            getLocalVideo();
        }
    }

    private void getLocalVideo() {
        if(getVideoDisposable!=null){
            getVideoDisposable.dispose();
        }
        getVideoDisposable = Observable.create(new ObservableOnSubscribe<List<LocalVideoImportViewModel>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<LocalVideoImportViewModel>> e) throws Exception {
                e.onNext(retrieveLocalVideoData());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<LocalVideoImportViewModel>>() {
                    @Override
                    public void onNext(@NonNull List<LocalVideoImportViewModel> localVideoImportViewModels) {
                        getLocalImportByVideoRepository().setLocalVideoBeanList(localVideoImportViewModels);
                        if (getUIView() != null) {
                            if (localVideoImportViewModels != null && localVideoImportViewModels.size() > 0) {
                                getUIView().updateData();
                                FetchImage();
                            } else {
                                getUIView().noData();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private List<LocalVideoImportViewModel> retrieveLocalVideoData() throws Exception {
        List<LocalVideoImportViewModel> localVideoBeanList = new ArrayList<>();
        if (getUIView() == null || getUIView().getAttachActivity() == null)
            return localVideoBeanList;
        String[] mediaColumns = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION};
        Cursor cursor = null;
        try {
            cursor = getUIView().getAttachActivity().getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns,
                    null, null, MediaStore.Video.Media.DISPLAY_NAME + " ASC");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    LocalVideoImportViewModel info = new LocalVideoImportViewModel();
                    info.id = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    if (TextUtils.isEmpty(path) || !new File(path).exists())
                        continue;
                    info.path = path;
                    String name = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    name = checkString(name);
                    if (TextUtils.isEmpty(name)) {
                        int position = path.lastIndexOf("/");
                        if (position < 0) {
                            name = "Unknown";
                        } else {
                            name = path.substring(position + 1);
                        }
                    }
                    int p = name.lastIndexOf(".");
                    if (p > 0) {
                        name = name.substring(0, p);
                    }
                    info.name = name;
                    info.size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    info.duration = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    info.picPath = AppFileStorage.getImagePath() + File.separator + info.name
                            + ".png";
                    localVideoBeanList.add(info);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return localVideoBeanList;
    }

    private void FetchImage() {
        if(fetchImageDisposable!=null){
            fetchImageDisposable.dispose();
        }
        fetchImageDisposable = Observable.create(new ObservableOnSubscribe<ImageModel>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ImageModel> e) throws Exception {
                List<LocalVideoImportViewModel> alist = new ArrayList<>();
                alist.addAll(getLocalImportByVideoRepository().getLocalVideoBeanList());
                for (LocalVideoImportViewModel item : alist) {
                    String picPath = retrieveVideoThumbnail(item);
                    if (!TextUtils.isEmpty(picPath)) {
                        e.onNext(new ImageModel(alist.indexOf(item), picPath));
                    }
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ImageModel>() {
            @Override
            public void onNext(@NonNull ImageModel imageModel) {
                if (getUIView() != null) {
                    getUIView().updateImage(imageModel.position, imageModel.picPath);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    static class ImageModel{
        public int position;
        public String picPath;
        public ImageModel(int position, String picPath){
            this.position = position;
            this.picPath = picPath;
        }
    }

    private String retrieveVideoThumbnail(LocalVideoImportViewModel info) {
        Activity activity = getUIView().getAttachActivity();
        if (activity == null)
            return null;
        File img = new File(AppFileStorage.getImagePath() + File.separator + info.name
                + ".png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        int scale = (options.outHeight / DisplayUtil.convertDIP2PX(68))
                > (options.outWidth / DisplayUtil.convertDIP2PX(120))
                ? (options.outWidth / DisplayUtil.convertDIP2PX(68))
                : (options.outHeight / DisplayUtil.convertDIP2PX(120));
        options.inSampleSize = scale;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = null;
        if (img.isFile() && img.exists()) {
            bm = BitmapFactory.decodeFile(AppFileStorage.getImagePath() + File.separator
                    + info.name + ".png", options);
            info.picPath = img.getAbsolutePath();
        }
        if (null == bm) {
            bm = MediaStore.Video.Thumbnails.getThumbnail(getUIView().getAttachActivity()
                            .getContentResolver(), info.id,
                    MediaStore.Images.Thumbnails.MINI_KIND, options);
            if (null != bm) {
                info.picPath = saveBitmap(bm, info.name + ".png");
            }
        }
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        return info.picPath;
    }

    private String saveBitmap(Bitmap bm, String videoName) {
        File f = new File(AppFileStorage.getImagePath() + File.separator + videoName);
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            return null;
        }
    }

    private String checkString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String returnStr = "";
        if (str.indexOf("'") != -1) {
            returnStr = str.replace("'", "''");
            str = returnStr;
        }
        return str;
    }

    public void onPlayerClick(int position) {
        LocalVideoImportViewModel bean = getLocalImportByVideoRepository().getLocalVideoBeanList().get(position);
        bean.isCheck = !bean.isCheck;
        getUIView().updateData();
        getUIView().updateCheck();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventController.unRegist(this);
        if(getVideoDisposable!=null){
            getVideoDisposable.dispose();
        }
        if(fetchImageDisposable!=null){
            fetchImageDisposable.dispose();
        }
    }

    public void onAllClick(boolean isAllClick){
       if(isAllClick){
           checkAll();
       }else{
           unCheckAll();
       }
        getUIView().updateCheck();
    }

    public void checkAll() {
        for (LocalVideoImportViewModel bean : getLocalImportByVideoRepository().getLocalVideoBeanList()) {
            bean.isCheck = true;
        }
    }

    public void unCheckAll() {
        for (LocalVideoImportViewModel bean : getLocalImportByVideoRepository().getLocalVideoBeanList()) {
            bean.isCheck = false;
        }
    }

    public int getCheckNum() {
        int num = 0;
        for (LocalVideoImportViewModel bean : getLocalImportByVideoRepository().getLocalVideoBeanList()) {
            if (bean.isCheck) {
                num++;
            }
        }
        return num;
    }

    public int getTotalNum() {
        return getLocalImportByVideoRepository().getLocalVideoBeanList().size();
    }

    public void importVideo() {
        List<LocalVideoBean> dbBeans = new ArrayList<>();
        for (LocalVideoImportViewModel bean : getLocalImportByVideoRepository().getLocalVideoBeanList()) {
            if (bean.isCheck) {
                dbBeans.add(LocalVideoBean.create(bean));
            }
        }
        if (dbBeans.size() <= 0) {
            getUIView().showToast("未选择任何项");
            return;
        }
        LocalVideoDatabseManager.getInstance().insertOrReplaceList(dbBeans);
        DialogUtil.showDialogCustomConfirm(getUIView().getAttachActivity(),
                AppContextProvider.getInstance().getContext().getString(R.string.tip_import_success), null,
                AppContextProvider.getInstance().getContext().getString(R.string.text_btn_go_check),
                AppContextProvider.getInstance().getContext().getString(R.string.text_btn_continue_import),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getUIView().showToast(AppContextProvider.getInstance().getContext().getString(R.string.tip_import_complete));
                        EventBus.getDefault().post(new ImportVideoEvent());
                        getUIView().finishView();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }, true);
    }

}
