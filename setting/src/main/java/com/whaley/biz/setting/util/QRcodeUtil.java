package com.whaley.biz.setting.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.whaley.biz.setting.constant.SettingConstants;
import com.whaley.core.uiframe.view.PageView;

/**
 * Created by dell on 2017/8/7.
 */

public class QRcodeUtil {

    public static final int RESULT_FAILED = 0;
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_CANCEL = 2;

    public static boolean onClickQrcode(Activity activity, Intent intent, int requestCode, PageView pageView) {
        if (!setCameraParams() || ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    SettingConstants.REQUEST_CAMERA);
            if(pageView!=null)
                pageView.showToast("未开启相机权限，如有必要，请到权限管理中设置");
            return false;
        }
        activity.startActivityForResult(intent, requestCode);
        return true;
    }

    public static boolean setCameraParams() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            if (mCamera != null) {
                Camera.Parameters mParameters = mCamera.getParameters();
                mParameters.set("orientation", "portrait");
                mCamera.setParameters(mParameters);
            }
        } catch (Exception e) {
            isCanUse = false;
        }
        try {
            if (mCamera != null) {
                mCamera.release();
            }
        } catch (Exception emCamera) {
            emCamera.printStackTrace();
        }
        return isCanUse;
    }

}
