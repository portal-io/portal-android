package com.whaleyvr.biz.hybrid.mvp.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.common.widget.keyboardHelper.OnKeyBoradChangeListener;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareListener;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;
import com.whaley.core.widget.titlebar.ITitleBar;
import com.whaleyvr.biz.hybrid.R;
import com.whaleyvr.biz.hybrid.model.WebPayload;
import com.whaleyvr.biz.hybrid.model.user.ActivityShareUserModel;
import com.whaleyvr.biz.hybrid.model.user.UserModel;
import com.whaleyvr.biz.hybrid.mvp.repository.WebViewRepository;
import com.whaleyvr.biz.hybrid.mvp.view.WebView;


/**
 * Created by YangZhi on 2016/10/31 18:54.
 */
public abstract class WebViewPresenter<MVPVIEW extends WebView> extends BasePagePresenter<MVPVIEW> implements OnKeyBoradChangeListener {

    public static final String WEBVIEW_URL = "webview_url";
    public static final String WEBVIEW_TITLE = "webview_title";
    public static final String WEBVIEW_IS_SINGLE_PAGE = "webview_is_single_page";
    public static final String WEBVIEW_PRESENTER = "webview_presenter";
    public static final String WEBVIEW_SHARE_MODEL = "webview_share_model";
    public static final String WEBVIEW_CACHE_POLICE = "webview_cache_police";


    public static final String URL_HTTP = "http://";
    public static final String URL_HTTPS = "https://";
    public static final String URL_FILE = "file://";

    public static final String ROOT_NEWS_DETAIL = "newsDetail";//资讯详情页

    @Repository
    WebViewRepository repository;

    public WebViewRepository getWebViewRepository() {
        return repository;
    }

    int totalScrollY;

    public WebViewPresenter(MVPVIEW view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            getWebViewRepository().setTitleBarModel((WebPayload.TitleBarModel) arguments.getSerializable(WEBVIEW_TITLE));
            getWebViewRepository().setUrl(arguments.getString(WEBVIEW_URL));
            getWebViewRepository().setCachePolice(arguments.getInt(WEBVIEW_CACHE_POLICE));
            ShareModel shareModel = arguments.getParcelable(WEBVIEW_SHARE_MODEL);
            if (shareModel != null) {
                Router.getInstance().buildExecutor("/share/service/sharemodel").putObjParam(shareModel).notTransParam()
                        .callback(new Executor.Callback<ShareParam.Builder>() {
                            @Override
                            public void onCall(ShareParam.Builder builder) {
                                getWebViewRepository().setBuilder(builder);
                            }

                            @Override
                            public void onFail(Executor.ExecutionError executionError) {

                            }
                        }).notTransCallbackData().excute();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getTitleBarModel() != null & getTitleBarModel().getType() == 4) {
            if (requestCode == CommonConstants.SHARE_REQUEST_CODE && data.getBooleanExtra(CommonConstants.SHARE_PARAM_OUTSIDE, false)) {
                Router.getInstance().buildExecutor("/program/service/activitysharecall").putObjParam(data.getIntExtra("type", 0)).excute();
            }
        }
    }

    public boolean isBelowStatusBar() {
        return false;
    }

    public boolean isResizeByLayoutChanged() {
        return true;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        setUpTitleBar();
        if (getUIView().getWebView() != null)
            onWebViewCreated();
        updateTitleShare();
    }

    /**
     * 更新TitleBar 分享配置
     */
    protected void updateTitleShare() {
        if (getWebViewRepository().getBuilder() == null) {
            return;
        }
        ITitleBar titleBar = getUIView().getTitleBar();
        if (titleBar == null) {
            return;
        }
        titleBar.setRightViewVisibility(View.VISIBLE);
        if (getTitleBarModel().getType() != 4) {
            titleBar.setRightIcon(R.drawable.title_share);
        }
        titleBar.setTitleBarListener(new SimpleTitleBarListener() {
            @Override
            public void onRightClick(View view) {
                super.onRightClick(view);
                ShareParam.Builder builder = getWebViewRepository().getBuilder();
                if (builder != null && getUIView() != null) {
                    builder.setContext(getUIView().getAttachActivity());
                    final ShareParam shareParam = builder.build();
                    ShareUtil.share(shareParam);
                    return;
                }
//                ShareParam shareParam = getWebViewRepository().getShareParam();
//                if (shareParam != null) {
//                    ShareModel shareModel = ShareModel.createBuilder().setUrl(shareParam.getUrl())
//                            .setTitle(shareParam.getTitle())
//                            .setDes(shareParam.getDes())
//                            .setVideo(false)
//                            .setType(ShareConstants.TYPE_ALL)
//                            .setShareType(shareParam.getShareType() > 0 ? shareParam.getShareType() : ShareTypeConstants.TYPE_SHARE_NEWS)
//                            .build();
//
//                }
//                ShareParam.Builder builder = ShareParam.createBuilder()
//                        .setContext(getUIView().getAttachActivity())
//                        .setUrl(shareParam.getUrl())
//                        .setTitle(shareParam.getTitle())
//                        .setDes(shareParam.getDes())
//                        .setVideo(false)
//                        .setType(ShareConstants.TYPE_ALL)
//                        .setShareType(shareParam.getShareType() > 0 ? shareParam.getShareType() : ShareTypeConstants.TYPE_SHARE_NEWS)
//                        .setFrom(TextUtils.isEmpty(shareParam.getFrom()) ? ROOT_NEWS_DETAIL : shareParam.getFrom());
//                ShareUtil.share(builder.build());
            }
        });
    }


    protected String getAppendUrl() {
        return null;
    }


    public void onWebViewCreated() {
        loadInitUrl();
    }

    /**
     * 加载初始网址
     */
    public void loadInitUrl() {
        if (getUIView() != null) {
            if (StrUtil.isEmpty(getWebViewRepository().getUrl())) {
                getUIView().getEmptyDisplayView().showError(new Throwable("空地址.."));
                return;
            }
            String url = getWebViewRepository().getUrl() + "#?" + getAppendUrl();
            getUIView().loadInitUrl(url);
        }
    }


    public WebPayload.TitleBarModel getTitleBarModel() {
        return getWebViewRepository().getTitleBarModel();
    }


    public ITitleBar getTitleBar() {
        if (getUIView() != null)
            return getUIView().getTitleBar();
        return null;
    }


    /**
     * 更新TitleBar
     */
    protected void setUpTitleBar() {
        if (getTitleBar() == null)
            return;
        getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        WebPayload.TitleBarModel titleBarModel = getTitleBarModel();
        if (titleBarModel != null) {
            switch (titleBarModel.getType()) {
                case 0:
                    getTitleBar().setVisibility(View.GONE);
                    if (getUIView().getAttachActivity() instanceof TitleBarActivity) {
                        TitleBarActivity activity = (TitleBarActivity) getUIView().getAttachActivity();
                    }
                    break;
                case 2:
                case 3:
                    getTitleBar().getContainer().setAlpha(0f);
                    getTitleBar().setTitleText(titleBarModel.getTitle());
                    getTitleBar().hideBottomLine();
                    break;
                case 4:
//                    getTitleBar().getContainer().setBackground();
                    getTitleBar().setRightIcon(R.drawable.share_white_selector);
                    getTitleBar().setLeftIcon(R.drawable.back_white_selector);
                    getTitleBar().hideBottomLine();
                    getTitleBar().getCenterView().setVisibility(View.GONE);
                    getTitleBar().setPaddingStatus(true);
                    getUIView().setTransparentFullStatusBar();
//                    ITitleBar titleBar = getUIView().getTitleBar();
//                    titleBar.setRightIcon(R.drawable.title_share);
//                    titleBar.setTitleBarListener(new SimpleTitleBarListener() {
//                        @Override
//                        public void onRightClick(View view) {
//                            super.onRightClick(view);
//                            ShareParam.Builder builder = getWebViewRepository().getBuilder();
//                            if (builder != null && getUIView() != null) {
//                                builder.setContext(getUIView().getAttachActivity());
//                                ShareUtil.share(builder.build());
//                            }
//                        }
//                    });
                    break;
                default:
                    getTitleBar().setTitleText(titleBarModel.getTitle());
                    break;
            }
        }
    }


    /**
     * 当滑动改变时
     *
     * @param dx
     * @param dy
     */
    public void onScrollChanged(int dx, int dy) {
        WebPayload.TitleBarModel titleBarModel = getTitleBarModel();
        if (titleBarModel != null && titleBarModel.getType() == 3) {
            final int height = titleBarModel.getScrollDistance();
            totalScrollY = totalScrollY + dy;
            int headerSroll = totalScrollY;
            if (headerSroll <= 0) {
                headerSroll = 0;
            }
            float ratio;
            if (height != 0) {
                ratio = (float) (headerSroll) / height;
            } else {
                ratio = 0;
            }
            float alpha = Math.min(1.0f, ratio);
            getTitleBar().getContainer().setAlpha(alpha);
        }
    }


    /**
     * 网页加载时当进度改变时
     *
     * @param view
     * @param newProgress
     */
    public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int newProgress) {
        Log.i(TAG, "onProgressChanged newProgress=" + newProgress);
//        if (newProgress == 100) {
//            onProgressIsCompleted();
//        }
    }

    /**
     * 获取的html title信息
     *
     * @param view
     * @param title
     */
    public void onReceivedTitle(com.tencent.smtt.sdk.WebView view, String title) {
        WebPayload.TitleBarModel titleBarModel = getTitleBarModel();
        if (titleBarModel != null && titleBarModel.getType() != 2 && titleBarModel.getType() != 3 && StrUtil.isEmpty(titleBarModel.getTitle())) {
            if (getTitleBar() != null)
                getTitleBar().setTitleText(title);
        }
    }

    public void onReceivedIcon(com.tencent.smtt.sdk.WebView view, Bitmap icon) {
    }


    /**
     * 网页进度加载到100(代表完成加载)
     */
    protected void onProgressIsCompleted() {
        onCompleteLoadingPage();
    }

    protected void onCompleteLoadingPage() {
        //如果外部URL地址则监听progressChange到100的时候移除loading
        getUIView().getAttachActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getUIView() != null && getUIView().getEmptyDisplayView() != null)
                    getUIView().getEmptyDisplayView().showContent();
            }
        });
    }


    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Override
    public void onHideKeyboard() {

    }

    @Override
    public void onShowKeyboard(int keyboardHeight) {

    }

    public void onFileChoosed(String path) {

    }
}
