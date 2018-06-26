package com.whaley.biz.common.utils;

import android.os.Environment;
import android.os.StatFs;
import android.widget.Toast;

import com.whaley.core.appcontext.AppContextProvider;

import java.io.File;

/**
 * Created by dell on 2017/8/24.
 */

public class SpaceUtil {

    public static long getTotalSize() {
        String sdcard = Environment.getExternalStorageState();
        String state = Environment.MEDIA_MOUNTED;
        if (sdcard.equals(state)) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

    public static long getAvailableSize() {
        String sdcard = Environment.getExternalStorageState();
        String state = Environment.MEDIA_MOUNTED;
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        if (sdcard.equals(state)) {
            long blockSize = statFs.getBlockSize();
            long blockavailable = statFs.getAvailableBlocks();
            long blockavailableTotal = blockSize * blockavailable;
            return blockavailableTotal;
        } else {
            return -1;
        }
    }

    public static boolean checkAvailableSpace(long fileSize) {
        if (fileSize >= SpaceUtil.getAvailableSize()) {
            Toast.makeText(AppContextProvider.getInstance().getContext(), "剩余存储空间不足", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
