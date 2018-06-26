package com.whaley.biz.setting.ui.view;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.john.waveview.WaveView;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.framerate.frametest.FrameTestImpl;
import com.whaley.biz.framerate.frametest.IFrameTest;
import com.whaley.biz.framerate.frametest.TestResult;
import com.whaley.biz.framerate.frametest.renderutils.AutoHeightTextureView;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.ui.presenter.PlayerDetectPresenter;
import com.whaley.biz.setting.widget.ConfirmDialog;
import com.whaley.biz.setting.widget.DetectCompleteDialog;
import com.whaley.core.appcontext.AppContextProvider;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/9/4.
 */

@Route(path = SettingRouterPath.PLAYER_DETECT)
public class PlayerDetectFragment extends BaseMVPFragment<PlayerDetectPresenter> implements PlayerDetectView, TextureView.SurfaceTextureListener {

    @BindView(R2.id.textureView)
    AutoHeightTextureView textureView;
    @BindView(R2.id.wave_view)
    WaveView waveView;
    @BindView(R2.id.tv_download_tip)
    TextView tvDownloadTip;
    @BindView(R2.id.layout_detect_detail)
    LinearLayout layoutDetectDetail;
    @BindView(R2.id.btn_start)
    TextView btnStart;
    @BindView(R2.id.tv_progress)
    TextView tvProgress;
    @BindView(R2.id.iv_cover_img)
    ImageView ivCoverImg;
    private int width, height;
    private IFrameTest iFrameTest;
    private boolean isSurfaceTextureAvailabled = false;
    Object lock = new Object();

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText("测试播放环境");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_player_detect;
    }

    @OnClick(R2.id.layout_detect_detail)
    public void clickDetial() {
        ConfirmDialog dialog = new ConfirmDialog(getActivity(),
                getString(R.string.text_detect_detail), getString(R.string.text_clear_level),
                null, false, true, new ConfirmDialog.ConfirmListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {
            }
        });
        dialog.show();
    }

    @OnClick(R2.id.btn_start)
    public void clickStart() {
        getPresenter().onclickStartButton();
    }

    @Override
    public void showCautionFlowDialog() {
        DialogUtil.showDialogCustomConfirm(getActivity(),
                getString(R.string.tip_caution_network_flow),
                null, getString(R.string.text_continue), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPresenter().onCautionDialogConfirm();
                    }
                }, null);
    }

    @Override
    public void showDetectCompleteDialog(int level) {
        DetectCompleteDialog dialog = new DetectCompleteDialog(getActivity(), level, new DetectCompleteDialog.ConfirmListener() {
            @Override
            public void onConfirm() {
                getPresenter().finish();
            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show();
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            btnStart.setVisibility(View.GONE);
            waveView.setVisibility(View.VISIBLE);
            tvProgress.setVisibility(View.VISIBLE);
            ivCoverImg.setVisibility(View.VISIBLE);
        } else {
            waveView.setVisibility(View.GONE);
            tvProgress.setVisibility(View.GONE);
            ivCoverImg.setVisibility(View.GONE);
        }
    }

    @Override
    public void detect(String path) {
        if(textureView==null||!textureView.isAvailable()){
            return;
        }
        synchronized (lock) {
            if (!isSurfaceTextureAvailabled) {
                Toast.makeText(AppContextProvider.getInstance().getContext(), "请准备好后重试", Toast.LENGTH_SHORT).show();
                return;

            } else if (iFrameTest != null && iFrameTest.isPlaying()) {
                Toast.makeText(AppContextProvider.getInstance().getContext(), "测试正在进行中", Toast.LENGTH_SHORT).show();
                return;
            }
            showProgress(false);
            iFrameTest = new FrameTestImpl(textureView.getSurfaceTexture(), width, height);
            iFrameTest.setMediaUrl(path);
            iFrameTest.startFrameTest(getActivity());
            iFrameTest.setProgressUpdate(new FrameTestImpl.ProgressUpdate() {
                @Override
                public void onProgress(int percent) {
                }
                @Override
                public void onCompleted(TestResult testResult) {
                    getPresenter().onDetectCompleted((int) testResult.getFrameRate());
                    ivCoverImg.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void showProgress(final int progress) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (waveView != null && tvProgress != null) {
                        waveView.setProgress(progress);
                        tvProgress.setText(String.valueOf(progress) + "%");
                    }

                }
            });
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (!isSurfaceTextureAvailabled)
            isSurfaceTextureAvailabled = true;
        this.width = width;
        this.height = height;
        getPresenter().onSurfaceTextureAvailable();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onPause() {
        super.onPause();
        synchronized (lock) {
            if (iFrameTest != null) {
                iFrameTest.shutTest();
                iFrameTest = null;
            }
        }
    }

}
