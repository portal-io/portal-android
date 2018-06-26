package com.whaley.biz.setting.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;

import com.whaley.biz.common.utils.SpaceUtil;
import com.whaley.biz.setting.model.download.DownloadBean;
import com.whaley.core.uiframe.view.PageView;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dell on 2017/8/1.
 */

public class SettingUtil {

    public static String getDateFromSeconds(String seconds) {
        if (seconds == null)
            return "";
        else {
            Date date = new Date();
            try {
                date.setTime(Long.parseLong(seconds) * 1000);
            } catch (NumberFormatException nfe) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
            return sdf.format(date);
        }
    }

    public static String getMonthFromSeconds(String seconds) {
        if (TextUtils.isEmpty(seconds))
            return "";
        else {
            Date date = new Date();
            try {
                date.setTime(Long.parseLong(seconds) * 1000);
            } catch (NumberFormatException nfe) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("M月");
            return sdf.format(date);
        }
    }

    public static String getYearFromSeconds(String seconds) {
        if (TextUtils.isEmpty(seconds))
            return "";
        else {
            Date date = new Date();
            try {
                date.setTime(Long.parseLong(seconds) * 1000);
            } catch (NumberFormatException nfe) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            return sdf.format(date);
        }
    }

    /**
     * 转化金钱数目 ===  分>元
     *
     * @param fen
     * @return
     */
    public static String fromFenToYuan(final String fen) {
        String yuan = "";
        final int MULTIPLIER = 100;
        try {
            yuan = new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
        } catch (Exception e) {
            //
        }
        return yuan;
    }

    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#0.0");
        String fileSizeString = "--";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static boolean checkAvailableSpace(DownloadBean bean, PageView pageView) {
        if (bean == null) {
            return false;
        } else if (bean.getTotalSize() - bean.getCurrentSize() >= SpaceUtil.getAvailableSize()) {
            if (pageView != null) {
                pageView.showToast("剩余存储空间不足");
            }
            return false;
        }
        return true;
    }

    /**
     * 适配api24及以上，根据uri获取图片的绝对路径
     */
    public static String getRealPathFromUri_AboveApi24(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    /**
     * 适配api23以下,根据uri获取图片的绝对路径
     */
    public static String getRealPathFromUri_Api11To23(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};

        CursorLoader loader = new CursorLoader(context, uri, projection, null,
                null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 阿拉伯数字转化为中文数字
     *
     * @param intInput
     * @return
     */
    public static String ToCH(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) // 個
        {
            sd += GetCH(intInput);
            return sd;
        } else if (si.length() == 2)// 十
        {
            if (si.substring(0, 1).equals("1"))
                sd += "十";
            else
                sd += (GetCH(intInput / 10) + "十");
            sd += ToCH(intInput % 10);
        } else if (si.length() == 3)// 百
        {
            sd += (GetCH(intInput / 100) + "百");
            if (String.valueOf(intInput % 100).length() < 2)
                sd += "零";
            sd += ToCH(intInput % 100);
        } else if (si.length() == 4)// 千
        {
            sd += (GetCH(intInput / 1000) + "千");
            if (String.valueOf(intInput % 1000).length() < 3)
                sd += "零";
            sd += ToCH(intInput % 1000);
        } else if (si.length() == 5)// 萬
        {
            sd += (GetCH(intInput / 10000) + "萬");
            if (String.valueOf(intInput % 10000).length() < 4)
                sd += "零";
            sd += ToCH(intInput % 10000);
        }

        return sd;
    }

    private static String GetCH(int input) {
        String sd = "";
        switch (input) {
            case 1:
                sd = "一";
                break;
            case 2:
                sd = "二";
                break;
            case 3:
                sd = "三";
                break;
            case 4:
                sd = "四";
                break;
            case 5:
                sd = "五";
                break;
            case 6:
                sd = "六";
                break;
            case 7:
                sd = "七";
                break;
            case 8:
                sd = "八";
                break;
            case 9:
                sd = "九";
                break;
            default:
                break;
        }
        return sd;
    }

    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (context.getPackageManager()
                    .getApplicationEnabledSetting(
                            "com.android.providers.downloads") == context
                    .getPackageManager().COMPONENT_ENABLED_STATE_DISABLED_USER
                    || context.getPackageManager()
                    .getApplicationEnabledSetting(
                            "com.android.providers.downloads") == context
                    .getPackageManager().COMPONENT_ENABLED_STATE_DISABLED
                    || context.getPackageManager()
                    .getApplicationEnabledSetting(
                            "com.android.providers.downloads") == context
                    .getPackageManager().COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {

                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getPercentage(long playTime, long totalPlayTime) {
        if (totalPlayTime == 0) {
            return "1%";
        }
        long percentage = playTime * 100 / totalPlayTime;
        if (percentage < 1) {
            percentage = 1;
        }
        return percentage + "%";
    }

}
