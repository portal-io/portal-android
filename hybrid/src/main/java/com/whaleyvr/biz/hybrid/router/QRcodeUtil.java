package com.whaleyvr.biz.hybrid.router;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by dell on 2017/9/7.
 */

public class QRcodeUtil {

    public static boolean onClickQrcode(Activity activity) {
        if (!setCameraParams() || ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 400);
            Toast.makeText(activity, "未开启相机权限，如有必要，请到权限管理中设置", Toast.LENGTH_SHORT).show();
            return false;
        }
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
