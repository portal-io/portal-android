package com.whaley.biz.setting.update;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.setting.R;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.AppFileStorage;

import java.io.File;

/**
 * Created by dell on 2017/9/5.
 */

public class UpdateDialog {

    private final static int MIN_CLICK_INTERVAL = 1500;

    public static void showDialog(final Activity activity, final String downloadUrl, final String versionName, String releaseNotes, int updateType){
        showDialog(activity,downloadUrl,versionName,releaseNotes,updateType,null);
    }
    public static void showDialog(final Activity activity, final String downloadUrl, final String versionName, String releaseNotes, int updateType, final View.OnClickListener onClickListener) {
        final View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update, null);
        viewDialog.setTag(0);
        final TextView btnUpdate = (TextView) viewDialog.findViewById(R.id.tv_update);
        final Dialog builder = new Dialog(activity, R.style.transparentDialog);
        TextView versionNameText = (TextView) viewDialog.findViewById(R.id.tv_version_name);
        ImageButton btnCancel = (ImageButton) viewDialog.findViewById(R.id.btn_cancel);
        final TextView releaseNoteText = (TextView) viewDialog.findViewById(R.id.tv_release_notes);
        final RelativeLayout progress_layout = (RelativeLayout) viewDialog.findViewById(R.id.progress_layout);
        final RoundCornerProgressBar progress_bar = (RoundCornerProgressBar) viewDialog.findViewById(R.id.progress_bar);
        final TextView progress_tv = (TextView) viewDialog.findViewById(R.id.progress_tv);
        versionNameText.setText(versionName);
        releaseNoteText.setText(releaseNotes);
        builder.setContentView(viewDialog);
        builder.setCanceledOnTouchOutside(false);
        if(updateType == 1) {
            btnCancel.setVisibility(View.GONE);
            builder.setCancelable(false);
            final long[] mLastClick = {0};
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                        if (System.currentTimeMillis() - mLastClick[0] > MIN_CLICK_INTERVAL) {
                            mLastClick[0] = System.currentTimeMillis();
                            Toast.makeText(AppContextProvider.getInstance().getContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        }else if(activity!=null){
                            activity.finish();
                        }
                        return true;
                    } else {
                        return true;
                    }
                }
            });
        }
        final ContentObserver[] downloadObserver = new ContentObserver[1];
        final BroadcastReceiver[] receiver = new BroadcastReceiver[1];
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    return;
                }
                if (1 == Integer.parseInt(viewDialog.getTag().toString())) {
                    Toast.makeText(AppContextProvider.getInstance().getContext(), getContext().getString(R.string.update_downloading), Toast.LENGTH_SHORT).show();
                    return;
                }
                String path = "/whaleyvr/update";
                final String filename = getContext().getResources().getString(R.string.app_name) + "_" + versionName + ".apk";
                final DownloadManager manager = (DownloadManager) getContext().getSystemService(Service.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                request.setDestinationInExternalPublicDir(path, filename);
                request.setDescription(getContext().getString(R.string.update_label));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setMimeType("application/vnd.android.package-archive");
                request.allowScanningByMediaScanner();
                request.setVisibleInDownloadsUi(true);
                final long myDownloadReference = manager.enqueue(request);
                UpdateHelper.getInstance().setUpdateId(myDownloadReference);
                final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
                final Handler handler = new Handler(Looper.getMainLooper());
                downloadObserver[0] = new ContentObserver(null) {
                    @Override
                    public void onChange(boolean selfChange) {
                        super.onChange(selfChange);
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(myDownloadReference);
                        Cursor c = manager.query(query);
                        if (c != null && c.moveToFirst()) {
                            int fileSizeIdx =
                                    c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                            int bytesDLIdx =
                                    c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                            float fileSize = (float) (c.getInt(fileSizeIdx));
                            float bytesDL = (float) (c.getInt(bytesDLIdx));
                            final int progress = (int) (bytesDL / fileSize * 100);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progress_bar.setProgress(progress);
                                    progress_tv.setText(progress + "%");
                                }
                            });
                        }
                        if (c != null) {
                            c.close();
                        }
                    }
                };
                getContext().getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver[0]);
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                viewDialog.setTag(1);
                btnUpdate.setVisibility(View.INVISIBLE);
                progress_layout.setVisibility(View.VISIBLE);
                receiver[0] = new BroadcastReceiver() {
                    @Override
                    public void onReceive(final Context context, Intent intent) {
                        long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        if (myDownloadReference == reference) {
                            Toast.makeText(AppContextProvider.getInstance().getContext(), context.getString(R.string.update_done), Toast.LENGTH_SHORT).show();
                            builder.cancel();
                            File file = new File(AppFileStorage.getUpdatePath() + "/" + filename);
                            if (android.os.Build.VERSION.SDK_INT >= 23) {
                                installApk(context, file);
                            } else {
                                String path = file.getAbsolutePath();
                                Uri downloadFileUri = Uri.parse("file://" + path);
                                installApkWithUri(context, downloadFileUri);
                            }
                            if (receiver[0] != null) {
                                AppContextProvider.getInstance().getContext().unregisterReceiver(receiver[0]);
                            }
                            UpdateHelper.getInstance().setUpdateId(-1);
                        }
                    }
                };
                AppContextProvider.getInstance().getContext().registerReceiver(receiver[0], filter);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (downloadObserver[0] != null) {
                    getContext().getContentResolver().unregisterContentObserver(downloadObserver[0]);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
                if(onClickListener!=null){
                    onClickListener.onClick(v);
                }
            }
        });
        if (activity != null && !activity.isFinishing()) {
            builder.show();
        }
    }

    public static Context getContext(){
        return AppContextProvider.getInstance().getContext();
    }

    public static void installApk(final Context mContext, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            installApkWithUriWithAndroidN(mContext, file);
        } else {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            String type = "application/vnd.android.package-archive";
            intent.setDataAndType(Uri.fromFile(file), type);
            mContext.startActivity(intent);
        }
    }

    public static void installApkWithUriWithAndroidN(final Context mContext, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri contentUri = FileProvider.getUriForFile(mContext, CommonConstants.APPLICATION_ID + ".provider", file);
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public static void installApkWithUri(final Context mContext, Uri path) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(path, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(install);
    }

}
