package com.whaley.biz.user.interactor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.ui.repository.ClipAvatarService;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.BitmapUtil;
import com.whaley.core.utils.FileUtil;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;

/**
 * Author: qxw
 * Date:2017/8/26
 * Introduction:
 */

public class ClipAvatar extends UseCase<Bitmap, String> {
    public ClipAvatar() {
    }

    public ClipAvatar(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<Bitmap> buildUseCaseObservable(String o) {

        Observable observable = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                ClipAvatarService service = getRepositoryManager().obtainMemoryService(ClipAvatarService.class);
                String savePath = "";
                if (service.isFromGallery()) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Uri uri = service.getPhotoUri();
                    String scheme = uri.getScheme();
                    if ("file".equals(scheme)) {
                        String path = uri.getPath();
                        service.setPhotoFile(new File(path));
                    } else {
                        Cursor cursor = AppContextProvider.getInstance().getContext().getContentResolver().query(service.getPhotoUri(),
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        service.setPhotoFile(new File(picturePath));
                    }
                }
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                long size = FileUtil.getFileSize(service.getPhotoFile());
                final long miniSize = 1048576 / 2;// 1M
                if (size > miniSize) {
                    bitmapOptions.inSampleSize = (int) (size / miniSize);
                } else {
                    bitmapOptions.inSampleSize = 1;
                }
                int degree = readPictureDegree(service.getPhotoFile().getAbsolutePath());
                Bitmap bitmap = BitmapFactory.decodeFile(service.getPhotoFile().getPath(),
                        bitmapOptions);
                bitmap = BitmapUtil.compressImage(bitmap);
                bitmap = rotaingImageView(degree, bitmap);
                if (e.isDisposed())
                    return;
                e.onNext(bitmap);
                e.onComplete();
            }
        });
        return observable;
    }

    private static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            Log.e(e, "");
        }
        return degree;
    }


}
