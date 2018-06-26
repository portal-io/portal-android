package com.whaleyvr.biz.download.util;

import android.os.Environment;
import android.os.StatFs;

import com.whaleyvr.biz.download.db.DownloadBean;

import java.io.File;

public class DownloadUtil {

    public static boolean checkAvailableSpace(DownloadBean bean) {
        if (bean == null) {
            return false;
        } else if (bean.getTotalSize() - bean.getCurrentSize() >= DownloadUtil.getAvailableSize()) {
            return false;
        }
        return true;
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

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean deleteFile(String path) {
        if (DownloadUtil.isEmpty(path)) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

}
