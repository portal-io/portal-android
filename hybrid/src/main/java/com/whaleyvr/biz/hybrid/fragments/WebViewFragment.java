package com.whaleyvr.biz.hybrid.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.common.widget.emptylayout.EmptyDisplayLayout;
import com.whaley.biz.common.widget.keyboardHelper.KeyBoardHelper;
import com.whaley.biz.common.widget.keyboardHelper.OnKeyBoradChangeListener;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.uiframe.view.EmptyDisplayView;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.biz.hybrid.CachePolicy;
import com.whaleyvr.biz.hybrid.R;
import com.whaleyvr.biz.hybrid.model.WebPayload;
import com.whaleyvr.biz.hybrid.mvp.presenter.NormalJSBridgePresenter;
import com.whaleyvr.biz.hybrid.mvp.presenter.WebViewPresenter;
import com.whaleyvr.biz.hybrid.widget.VRWebView;

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * 通用Web页 Fragment
 * Created by YangZhi on 2016/10/8 16:03.
 */
@Route(path = "/hybrid/ui/web")
public class WebViewFragment extends BaseMVPFragment<WebViewPresenter> implements com.whaleyvr.biz.hybrid.mvp.view.WebView {

    static final int REQUEST_OPEN_FILE_CHOOSE = 4371;

    public static IntentBuilder createBuilder(Starter starter) {
        return new IntentBuilder().setStarter(starter);
    }

    public static class IntentBuilder {
        private Starter starter;
        private String url;
        private WebPayload.TitleBarModel titleBarModel;
        private ShareModel shareEntity;
        private Class fragmentClass;
        private Class presenterClass;
        private int cachePolicy = CachePolicy.CACHE_POLICY_NONE;

        public IntentBuilder setStarter(Starter starter) {
            this.starter = starter;
            return this;
        }

        public IntentBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public IntentBuilder setTitleBarModel(WebPayload.TitleBarModel titleBarModel) {
            this.titleBarModel = titleBarModel;
            return this;
        }

        public IntentBuilder setFragmentClass(Class fragmentClass) {
            this.fragmentClass = fragmentClass;
            return this;
        }

        public IntentBuilder setShareEntity(ShareModel shareEntity) {
            this.shareEntity = shareEntity;
            return this;
        }

        public IntentBuilder setPresenterClass(Class presenterClass) {
            this.presenterClass = presenterClass;
            return this;
        }


        public IntentBuilder setCachePolicy(int cachePolicy) {
            this.cachePolicy = cachePolicy;
            return this;
        }

        public Intent build() {
            if (titleBarModel == null) {
                titleBarModel = WebPayload.TitleBarModel.createTitleBarModel("");
            }
            switch (titleBarModel.getType()) {
                case 0:
                case 2:
                case 3:
                case 4:
                    titleBarModel.setBelow(true);
                    break;
                default:
                    titleBarModel.setBelow(false);
                    break;
            }
            if (fragmentClass == null) {
                fragmentClass = WebViewFragment.class;
            }
            Intent intent = TitleBarActivity.createIntent(starter, fragmentClass, titleBarModel.isBelow());
            intent.putExtra(WebViewPresenter.WEBVIEW_URL, url);
            if (titleBarModel != null) {
                intent.putExtra(WebViewPresenter.WEBVIEW_TITLE, (Serializable) titleBarModel);
            }
            if (shareEntity != null) {
                intent.putExtra(WebViewPresenter.WEBVIEW_SHARE_MODEL, shareEntity);
            }
            if (presenterClass == null)
                presenterClass = NormalJSBridgePresenter.class;
            intent.putExtra(WebViewPresenter.WEBVIEW_PRESENTER, presenterClass.getName());
            intent.putExtra(WebViewPresenter.WEBVIEW_IS_SINGLE_PAGE, true);
            intent.putExtra(WebViewPresenter.WEBVIEW_CACHE_POLICE, cachePolicy);
            return intent;
        }
    }

    public static void goPage(Starter starter, String url, WebPayload.TitleBarModel titleBarModel) {
        goPage(starter, url, titleBarModel, WebViewFragment.class);
    }

    public static void goPage(Starter starter, String url, WebPayload.TitleBarModel titleBarModel, ShareModel shareModel) {
        goPage(starter, url, titleBarModel, shareModel, WebViewFragment.class);
    }


    public static void goPage(Starter starter, String url) {
        goPage(starter, url, null, WebViewFragment.class);
    }

    public static void goPage(Starter starter, String url, Class fragmentClass) {
        goPage(starter, url, null, fragmentClass);
    }

    public static void goPage(Starter starter, String url, WebPayload.TitleBarModel titleBarModel, ShareModel shareModel, Class fragmentClass) {
        goPage(starter, url, titleBarModel, shareModel, fragmentClass, NormalJSBridgePresenter.class);
    }

    public static void goPage(Starter starter, String url, WebPayload.TitleBarModel titleBarModel, Class fragmentClass) {
        goPage(starter, url, titleBarModel, fragmentClass, NormalJSBridgePresenter.class);
    }

    public static void goPage(Starter starter, String url, WebPayload.TitleBarModel titleBarModel, Class fragmentClass, Class presenterClass) {
        Intent intent = createIntent(starter, url, titleBarModel, null, fragmentClass, presenterClass);
        starter.startActivityForResult(intent, 0);
    }

    public static void goPage(Starter starter, String url, WebPayload.TitleBarModel titleBarModel, ShareModel shareModel, Class fragmentClass, Class presenterClass) {
        Intent intent = createIntent(starter, url, titleBarModel, shareModel, fragmentClass, presenterClass);
        starter.startActivityForResult(intent, 0);
    }

    public static void goPage(Starter starter, Intent intent) {
        starter.startActivityForResult(intent, 0);
    }

    public static Intent createIntent(Starter starter, String url, WebPayload.TitleBarModel titleBarModel, ShareModel shareModel, Class fragmentClass, Class presenterClass) {
        return createBuilder(starter)
                .setUrl(url)
                .setFragmentClass(fragmentClass)
                .setPresenterClass(presenterClass)
                .setShareEntity(shareModel)
                .setTitleBarModel(titleBarModel)
                .build();
    }


    protected static String getTag(FragmentManager fm, WebView webView, String url) {
        return fm.hashCode() + webView.hashCode() + url;
    }

    VRWebView webView;

    EmptyDisplayLayout emptyLayout;

    EmptyDisplayView emptyDisplayView;

    private KeyBoardHelper keyBoardHelper;

    private VRWebView.OnScrollChangedCallback onScrollChangedCallback = new VRWebView.OnScrollChangedCallback() {
        @Override
        public void onScroll(int dx, int dy) {
            getPresenter().onScrollChanged(dx, dy);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected WebViewPresenter onCreatePresenter() {
        if (getArguments() == null)
            return null;
        String className = getArguments().getString(WebViewPresenter.WEBVIEW_PRESENTER);
        if (StrUtil.isEmpty(className)) {
            className = NormalJSBridgePresenter.class.getName();
        }
        WebViewPresenter presenter = null;
        try {
//            presenter = (WebViewPresenter) Class.forName(className).newInstance();
            Class clazz = Class.forName(className);
            Constructor constructor = clazz.getConstructor(com.whaleyvr.biz.hybrid.mvp.view.WebView.class);
            presenter = (WebViewPresenter) constructor.newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return presenter;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (getLayoutId() == R.layout.fragment_webview) {
            webView = (VRWebView) view.findViewById(R.id.webview);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setVerticalScrollBarEnabled(false);
            emptyLayout = (EmptyDisplayLayout) view.findViewById(R.id.emptyLayout);
        }
        return view;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getActivity() != null && Build.VERSION.SDK_INT > 11) {
            getActivity().getWindow()
                    .setFlags(
                            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        webView.setPadding(0, 0, 0, DisplayUtil.getBottomStatusHeight());
        setWebView(getWebView());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            webView.resumeTimers();
        }
        if (keyBoardHelper != null)
            keyBoardHelper.setResize(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
            webView.pauseTimers();
        }
        if (keyBoardHelper != null)
            keyBoardHelper.setResize(false);
    }


    @Override
    public void setTransparentFullStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtil.setTransparentFullStatusBar(getActivity().getWindow(), getSystemBarTintManager(), false);
        ((TitleBarActivity) getActivity()).setNotTopPadding();
    }

    private SystemBarTintManager getSystemBarTintManager() {
        return ((TitleBarActivity) getActivity()).getSystemBarManager();
    }

    protected void setWebView(VRWebView webView) {
        if (webView == null)
            return;
        WebView.setWebContentsDebuggingEnabled(true);
        this.webView = webView;
        this.webView.setStarter(this);
        this.webView.setOnScrollChangedCallback(onScrollChangedCallback);
        //监听软键盘
        //加OnGlobalLayoutListener是为了适配横屏时监听
        webView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyBoardHelper != null)
                    keyBoardHelper.destory();
                if (getActivity() != null) {
                    keyBoardHelper = KeyBoardHelper.assistActivity(getActivity(), getPresenter().isBelowStatusBar(), getPresenter().isResizeByLayoutChanged(), new OnKeyBoradChangeListener() {
                        @Override
                        public void onShowKeyboard(int keyboardHeight) {
                            getPresenter().onShowKeyboard(keyboardHeight);
                        }

                        @Override
                        public void onHideKeyboard() {
                            getPresenter().onHideKeyboard();
                        }
                    });
                }

                getWebView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        setEmptyLayout(getEmptyDisplayView());
        setUpWebView();
    }

    protected void setUpWebView() {
        webView.setEmptyLayout(getEmptyDisplayView());
        setWebViewSetting();
    }

    protected void setWebViewSetting() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                getPresenter().onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getPresenter().onReceivedTitle(view, title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                getPresenter().onReceivedIcon(view, icon);
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "choose files"), REQUEST_OPEN_FILE_CHOOSE);
                } catch (android.content.ActivityNotFoundException ex) {

                }
                super.openFileChooser(valueCallback, s, s1);

            }
        });


    }

    @Override
    public void skipLongClick() {
        if (getWebView() != null)
            getWebView().getView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
    }


    @Override
    public void loadWebUrl(String url) {
        try {
            if (webView != null) {
//                webView.stopLoading();
                webView.loadUrl(url);
                Log.e("WebView 加载URL url=" + url);
            }
        } catch (Exception e) {
            Log.e(e, "WebViewFragment loadWebUrl");
        }

    }

    @Override
    public void loadInitUrl(String url) {
        if (webView != null) {
//            webView.stopLoading();
            webView.loadInitUrl(url);
        }
    }


    public VRWebView getWebView() {
        return webView;
    }


    public void setEmptyLayout(EmptyDisplayView emptyLayout) {
        this.emptyDisplayView = emptyLayout;
        if (this.emptyDisplayView != null) {
            this.emptyDisplayView.setOnRetryListener(new EmptyDisplayLayout.OnRetryListener() {
                @Override
                public void onRetry() {
                    getPresenter().loadInitUrl();
                }
            });
        }
    }

    @Override
    public EmptyDisplayView getEmptyDisplayView() {
        return new EmptyDisplayView() {
            @Override
            public void showEmpty() {
                if (emptyLayout != null) {
                    emptyLayout.showEmpty();
                }
            }

            @Override
            public void showLoading(String loadtext) {
                if (emptyLayout != null) {
                    emptyLayout.showLoading(loadtext);
                }
            }

            @Override
            public void showError(Throwable error) {
                if (emptyLayout != null) {
                    emptyLayout.showError(error);
                }
            }

            @Override
            public void showContent() {
                if (emptyLayout != null) {
                    emptyLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (emptyLayout != null) {
                                emptyLayout.showContent();
                                ViewGroup parent = (ViewGroup) emptyLayout.getParent();
                                if (parent != null) {
                                    parent.removeView(emptyLayout);
                                }
                            }
                        }
                    }, 100);

                }
            }

            @Override
            public void setOnRetryListener(EmptyDisplayLayout.OnRetryListener listener) {
                if (emptyLayout != null) {
                    emptyLayout.setOnRetryListener(listener);
                }
            }
        };
    }

    @Override
    public boolean onBackPressed() {
        if (getPresenter().onBackPressed())
            return true;
        if (webView != null && webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onBackPressed();
    }


    @Override
    public void onDestroyView() {
        destory();
        super.onDestroyView();
    }

    public void destory() {
        if (keyBoardHelper != null)
            keyBoardHelper.destory();
        emptyLayout = null;
        emptyDisplayView = null;
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    public void destoryWebView() {
        if (webView != null) {
            webView.destroy();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_FILE_CHOOSE) {
            Log.i("fileChooser", "intent is" + data.toString());
            Log.i("fileChooser", "bundle is" + data.getDataString());
            String path = data.getDataString();
            getPresenter().onFileChoosed(path);
            return;
        }
        if (webView != null)
            webView.onActivityResult(requestCode, resultCode, data);
    }
}
