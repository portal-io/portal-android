package com.whaley.biz.setting.ui.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.whaley.biz.common.ui.BaseMVPActivity;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.setting.constant.SettingConstants;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.util.QRcodeUtil;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.ui.presenter.QRcodePresenter;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.ReflexUtil;
import com.whaley.core.widget.titlebar.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/7.
 */

@Route(path = SettingRouterPath.QRCODE)
public class QRcodeActivity extends BaseMVPActivity<QRcodePresenter> implements QRcodeView {

    @BindView(R2.id.titlebar)
    TitleBar titlebar;
    @BindView(R2.id.ck_light)
    CheckBox ckLight;
    @BindView(R2.id.tv_light)
    TextView tv_light;

    private boolean light = false;
    private final static int REQUEST_IMAGE = 0;
    private CodeUtils.AnalyzeCallback analyzeCallback;
    private CaptureFragment captureFragment;

    public static void goPage(Starter starter) {
        Intent intent = new Intent(starter.getAttatchContext(), QRcodeActivity.class);
        starter.startActivityForResult(intent, SettingConstants.CODE_RESULT_FOR_URL);
    }

    @Override
    public void finish() {
        if (getPresenter().getQRcodeRepository().getScanResult() == QRcodeUtil.RESULT_CANCEL) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, QRcodeUtil.RESULT_CANCEL);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            bundle.putString(QRcodePresenter.STR_CALLBACK_ID, getPresenter().getQRcodeRepository().getCallbackId());
            resultIntent.putExtras(bundle);
            QRcodeActivity.this.setResult(RESULT_OK, resultIntent);
        }
        super.finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_qrcode;
    }

    @Override
    protected void setViews(Bundle savedInstanceState) {
        super.setViews(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(DisplayUtil.convertDIP2PX(20), 0, DisplayUtil.convertDIP2PX(20), 0);
        textView.setText("二维码导入");
        textView.setTextColor(getResources().getColor(R.color.color12));
        titlebar.setCenterView(textView);
        titlebar.hideBottomLine();
        titlebar.setLeftIcon(R.drawable.btn_back);
        titlebar.getContainer().setBackgroundResource(0);
        titlebar.setTitleBarListener(new SimpleTitleBarListener());
        /**
         * 执行扫面Fragment的初始化操作
         */
        captureFragment = new CaptureFragment();

        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.layout_qr_scan);
        initAnalyzeCallback();
        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    @Override
    protected void onDestroy() {
        if (captureFragment != null) {
            Object object = ReflexUtil.getField(CaptureFragment.class.getName(), captureFragment, "mediaPlayer");
            if (object != null && object instanceof MediaPlayer) {
                MediaPlayer mediaPlayer = (MediaPlayer) object;
                mediaPlayer.release();
            }
            captureFragment.setAnalyzeCallback(null);
            captureFragment.onPause();
            captureFragment.onDestroy();
            captureFragment = null;
        }
        super.onDestroy();
    }

    private void initAnalyzeCallback() {
        analyzeCallback = new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                getPresenter().getQRcodeRepository().setScanResult(QRcodeUtil.RESULT_SUCCESS);
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, QRcodeUtil.RESULT_SUCCESS);
                bundle.putString(CodeUtils.RESULT_STRING, result);
                bundle.putString(QRcodePresenter.STR_CALLBACK_ID, getPresenter().getQRcodeRepository().getCallbackId());
                resultIntent.putExtras(bundle);
                QRcodeActivity.this.setResult(RESULT_OK, resultIntent);
                QRcodeActivity.this.finish();
            }

            @Override
            public void onAnalyzeFailed() {
                getPresenter().getQRcodeRepository().setScanResult(QRcodeUtil.RESULT_FAILED);
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, QRcodeUtil.RESULT_FAILED);
                bundle.putString(CodeUtils.RESULT_STRING, "");
                bundle.putString(QRcodePresenter.STR_CALLBACK_ID, getPresenter().getQRcodeRepository().getCallbackId());
                resultIntent.putExtras(bundle);
                QRcodeActivity.this.setResult(RESULT_OK, resultIntent);
                QRcodeActivity.this.finish();
            }
        };
    }

    @OnClick(R2.id.btn_gallery)
    public void clickGalleryBtn() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
        ;
    }

    @OnClick(R2.id.btn_light)
    public void clickLightBtn() {
        CodeUtils.isLightEnable(light = !light);
        ckLight.setChecked(light);
        tv_light.setSelected(light);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    String scheme = uri.getScheme();
                    String path = uri.getPath();
                    if (!"file".equals(scheme)) {
                        if (Build.VERSION.SDK_INT < 23) {
                            path = SettingUtil.getRealPathFromUri_Api11To23(this, uri);
                        } else {
                            path = SettingUtil.getRealPathFromUri_AboveApi24(this, uri);
                        }
                    }
                    if (analyzeCallback != null)
                        CodeUtils.analyzeBitmap(path, analyzeCallback);
                } catch (Exception e) {
                    //
                }
            }
        }
    }


}
