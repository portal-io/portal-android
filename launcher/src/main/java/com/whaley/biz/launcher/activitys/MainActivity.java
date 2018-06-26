package com.whaley.biz.launcher.activitys;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.orhanobut.logger.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.snailvr.manager.R;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.ui.BaseMVPActivity;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.launcher.App;
import com.whaley.biz.launcher.AppLike;
import com.whaley.biz.launcher.adapter.MainFragmentAdapter;
import com.whaley.biz.launcher.event.ConnectEvent;
import com.whaley.biz.launcher.model.NoticeModel;
import com.whaley.biz.launcher.event.AdvertiseImageEvent;
import com.whaley.biz.launcher.presenter.MainPresenter;
import com.whaley.biz.launcher.util.FixInputMethodManagerLeakUtil;
import com.whaley.biz.launcher.util.UserUtil;
import com.whaley.biz.launcher.view.MainView;
import com.whaley.biz.launcher.widget.PlayerContainer;
import com.whaley.biz.launcher.widget.RoundProgressBar;
import com.whaley.biz.playerui.PlayerView;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PlayerViewProviderEvent;
import com.whaley.biz.program.playersupport.component.liveplayer.initblurbackground.InitBlurBackgroundUIAdapter;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageLoaderCallback;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.image.OnResourceLoadCallback;
import com.whaley.core.image.Transformation;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.FastBlur;
import com.whaley.core.utils.StrUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */

public class MainActivity extends BaseMVPActivity<MainPresenter> implements MainView {

    static final String EVENT_UPDATE_PLAYER_POSITION = "event_update_player_position";

    private final static int MIN_CLICK_INTERVAL = 1500;
    public final static String TYPE_TAB = "tab";
    public final static String TYPE_VIDEO_TYPE = "video_type";
    public final static String TYPE_NOTICE = "notice";
    public final static String TYPE_SECONDARY_TAB = "secondary_tab";
    @BindView(R.id.tv_recommend)
    TextView tvRecommend;
    @BindView(R.id.tv_live)
    TextView tvLive;
    @BindView(R.id.tv_discover)
    TextView tvDiscover;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_follow_notice)
    TextView tvFollowNotice;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.line)
    View line;

    @BindView(R.id.rl_recommend)
    RelativeLayout rlRecommend;
    @BindView(R.id.rl_live)
    RelativeLayout rlLive;
    //    @BindView(R.id.rl_launcher)
//    RelativeLayout rlLauncher;
    @BindView(R.id.rl_discover)
    RelativeLayout rlDiscover;
    @BindView(R.id.rl_user)
    RelativeLayout rlUser;


    @BindView(R.id.layout_bottom_btns)
    LinearLayout layoutBottomBtns;
    @BindView(R.id.layout_rootview)
    RelativeLayout layoutRootview;

    @BindView(R.id.playerContainer)
    PlayerContainer playerContainer;

    @BindView(R.id.vs_event_poster)
    ViewStub vsEventPoster;

    @BindView(R.id.vs_recommended_content)
    ViewStub vsRecommendedContent;

    @BindView(R.id.vs_spring_festival)
    ViewStub vsSpringFestival;

    View eventPoster;
    View recommendedContent;
    View springFestival;

    ImageButton fabSpringFestival;

    ImageView ivBg;
    ImageView ivContent;
    ImageView ivClose;
    RoundProgressBar roundProgressBar;
    private Object lock = new Object();
    SystemBarTintManager systemBarManager;
    MainFragmentAdapter mainFragmentAdapter;
    private View[] mTabs;

    private PlayerView playerView;

    final Rect rect = new Rect();
    final Rect lastRect = new Rect();

    public static void launch(Starter starter) {
        Intent intent = new Intent(starter.getAttatchContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        starter.startActivity(intent);
    }

    @Override
    protected void setViews(Bundle savedInstanceState) {
        super.setViews(savedInstanceState);
        registerComponentCallbacks(this);
        EventBus.getDefault().removeStickyEvent(AdvertiseImageEvent.class);
        EventBus.getDefault().register(this);
        String notice = getIntent().getStringExtra(TYPE_NOTICE);
        if (!StrUtil.isEmpty(notice)) {
            getPresenter().processNotice(notice);
        }
        initSystemBarManager();
        mTabs = new View[]{rlRecommend, rlDiscover, rlLive, rlUser};
        mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        if (savedInstanceState != null) {
            mainFragmentAdapter.onRestoreInstanceState(savedInstanceState);
            String tag = mainFragmentAdapter.getmCurrentTag();
            String[] tags = mainFragmentAdapter.getTabTags();
            int i = 0;
            for (String tabTag : tags) {
                if (tag.equals(tabTag)) {
                    mTabs[i].performClick();
                    break;
                }
                i++;
            }
        } else {
            rlRecommend.performClick();
        }

        PlayerViewProviderEvent playerViewProviderEvent = new PlayerViewProviderEvent("", new PlayerViewProviderEvent.PlayerViewProvider() {
            @Override
            public PlayerView getPlayerView(int width, int height) {

                return getOrAddPlayerView(width, height);
            }
        });
        EventBus.getDefault().postSticky(playerViewProviderEvent);

        layoutRootview.getViewTreeObserver().addOnScrollChangedListener(onScrollChangedListener);
        getPresenter().showNotice();
    }


    ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {

        @Override
        public void onScrollChanged() {
            scrollPlayer();
        }
    };

    @Subscribe
    public void onUpdatePlayerPositionEvent(ModuleEvent moduleEvent) {
        String eventName = moduleEvent.getEventName();
        if (eventName.equals(EVENT_UPDATE_PLAYER_POSITION)) {
            if (playerView != null) {
                playerView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollPlayer();
                    }
                });
                playerView.requestLayout();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case "program/event/switchbannertofullscreen":
                changeToFullScreenPlayer();
                break;
            case "program/event/switchfullscreentobanner":
                changeToBannerPlayer();
                break;
            case "login_success":
                UserUtil.loginUser();
                getPresenter().showNotice();
                break;
            case "sign_out":
                UserUtil.signOut();
            case "follow_notice_update":
                hideNotice();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ConnectEvent event) {
        getPresenter().showNotice();
    }

    boolean isPlayerFullScreen;

    int lastWitdh;

    int lastHeight;

    private void changeToFullScreenPlayer() {
        isPlayerFullScreen = true;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.playerView.getLayoutParams();
        lastWitdh = layoutParams.width;
        lastHeight = layoutParams.height;
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        playerView.requestLayout();
        for (int i = 0, j = layoutRootview.getChildCount() - 1; i <= j; i++) {
            View view = layoutRootview.getChildAt(i);
            if (view != playerContainer) {
                view.setVisibility(View.GONE);
            }
        }
        scrollPlayer();
        content.setPadding(0, 0, 0, 0);
        int[] size = getScreenSize();
        updatePlayerViewSize(size);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void changeToBannerPlayer() {
        isPlayerFullScreen = false;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.playerView.getLayoutParams();
        layoutParams.width = lastWitdh;
        layoutParams.height = lastHeight;
        playerView.requestLayout();
        for (int i = 0, j = layoutRootview.getChildCount() - 1; i <= j; i++) {
            View view = layoutRootview.getChildAt(i);
            if (view != eventPoster && view != recommendedContent&&view!=vsEventPoster&&view!=vsRecommendedContent&&view!=vsSpringFestival)
                view.setVisibility(View.VISIBLE);
        }
        scrollPlayer();
        setStatusBar();
        int[] size = new int[]{ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT};
        updatePlayerViewSize(size);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isPlayerFullScreen && playerView != null) {
            int[] size = getScreenSize();
            updatePlayerViewSize(size);
        }
    }

    private void updatePlayerViewSize(int[] size) {
        View realPlayerView = playerView.getRealPlayerView();
        ViewGroup.LayoutParams layoutParams = realPlayerView.getLayoutParams();
        if (layoutParams.width != size[0] || layoutParams.height != size[1]) {
            layoutParams.width = size[0];
            layoutParams.height = size[1];
            realPlayerView.requestLayout();
        }
    }

    public static int[] getScreenSize() {
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        WindowManager windowManager = (WindowManager) AppContextProvider.getInstance().getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class c = Class.forName("android.view.Display");
            Method e = c.getMethod("getRealMetrics", new Class[]{DisplayMetrics.class});
            e.invoke(display, new Object[]{displayMetrics});
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
        } catch (Exception var7) {
            Logger.e(var7, "getDpi", new Object[0]);
        }
        int[] size = new int[]{width, height};
        return size;
    }

    private PlayerView getOrAddPlayerView(final int width, final int height) {
        if (playerView == null) {
            Router.getInstance().buildExecutor("/program/service/providebannerplayer")
                    .notTransParam()
                    .notTransCallbackData()
                    .putObjParam(this)
                    .callback(new Executor.Callback<PlayerView>() {
                        @Override
                        public void onCall(PlayerView playerView) {
                            MainActivity.this.playerView = playerView;
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
                            playerView.setLayoutParams(layoutParams);
                            playerContainer.setPlayerView(playerView);

                        }

                        @Override
                        public void onFail(Executor.ExecutionError executionError) {

                        }
                    })
                    .excute();
        } else if (!isPlayerFullScreen) {
            boolean shouldRequestLayout = false;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.playerView.getLayoutParams();
            if (layoutParams.width != width) {
                layoutParams.width = width;
                shouldRequestLayout = true;
            }
            if (layoutParams.height != height) {
                layoutParams.height = height;
                shouldRequestLayout = true;
            }
            if (shouldRequestLayout) {
                playerView.requestLayout();
            }
        }

        return playerView;
    }


    public void scrollPlayer() {
        if (playerView == null) {
            return;
        }
        if (isPlayerFullScreen) {
            if (playerView.getVisibility() != View.VISIBLE) {
                playerView.setVisibility(View.VISIBLE);
            }
            playerContainer.resetPosition();
            return;
        }
        View tagView = (View) playerView.getTag();
        if (tagView == null) {
            if (playerView.getVisibility() == View.VISIBLE) {
                playerView.setVisibility(View.GONE);
            }
            return;
        }
        if (playerView.getVisibility() != View.VISIBLE) {
            playerView.setVisibility(View.VISIBLE);
        }
        getTagVisibleRect(tagView, rect);
        playerContainer.setRect(rect);
    }

    private int[] location = new int[2];

    private int[] rootViewLocation = new int[2];

    private void getTagVisibleRect(View targetView, Rect rect) {
        layoutRootview.getLocationOnScreen(rootViewLocation);
        targetView.getLocationOnScreen(location);

        rect.left = location[0];
        rect.top = location[1] - rootViewLocation[1];
        rect.right = rect.left + targetView.getMeasuredWidth();
        rect.bottom = rect.top + targetView.getMeasuredHeight();
    }


    private void initSystemBarManager() {
        systemBarManager = new SystemBarTintManager(this);
    }

    @OnClick({R.id.rl_recommend, R.id.rl_live, R.id.rl_discover, R.id.rl_user})
    public void onClick(View view) {
        synchronized (lock) {
            switch (view.getId()) {
                case R.id.rl_recommend:
                    mainFragmentAdapter.switchPage(MainFragmentAdapter.TAB_RECOMMEND);
                    setStatusBar();
                    getPresenter().onRecommendClick();
                    break;
                case R.id.rl_live:
                    mainFragmentAdapter.switchPage(MainFragmentAdapter.TAB_LIVE);
                    setStatusBar();
                    getPresenter().onLiveClick();
                    break;
                case R.id.rl_discover:
                    mainFragmentAdapter.switchPage(MainFragmentAdapter.TAB_DISCOVER);
                    setStatusBar();
                    getPresenter().onDiscoverClick();
                    break;
//                case R.id.rl_launcher:
//                    getPresenter().startLauncher();
//                    break;
                case R.id.rl_user:
                    mainFragmentAdapter.switchPage(MainFragmentAdapter.TAB_USER);
                    setStatusBar();
                    getPresenter().onUserClick();
                    break;
            }
//            if (view.getId() != R.id.rl_launcher) {
            toggleButton(view.getId());
//            }
        }

    }

    private void toggleButton(int id) {
        for (View view : mTabs) {
            if (view.getId() != id) {
                view.setSelected(false);
            } else {
                view.setSelected(true);
            }
        }
    }

    @Override
    public SystemBarTintManager getSystemBarManager() {
        return systemBarManager;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void unBindViews() {
        if (layoutRootview != null)
            layoutRootview.getViewTreeObserver().removeOnScrollChangedListener(onScrollChangedListener);
        super.unBindViews();
        this.playerView = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private long mLastClick = 0;

    Runnable backRunable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - mLastClick > MIN_CLICK_INTERVAL) {
                mLastClick = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
                return;
            }
            finish();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (playerView == null || playerView.getVisibility() != View.VISIBLE || !isPlayerFullScreen) {
                backRunable.run();
                return false;
            }
            BackPressEvent backPressEvent = new BackPressEvent();
            playerView.getPlayerController().getEventBus().post(backPressEvent);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = mainFragmentAdapter.getCurrentFragment();
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void finish() {
        unregisterComponentCallbacks(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        EventBus.getDefault().removeAllStickyEvents();
        if (playerView != null) {
            playerView.destory();
            playerView = null;
        }
        if (playerContainer != null) {
            playerContainer.destroy();
        }
        ImageLoader.onTrimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);
        FixInputMethodManagerLeakUtil.fixInputMethodManagerLeak(this);
        FixInputMethodManagerLeakUtil.fixTextLineCacheLeak();
        Router.getInstance().buildExecutor("/program/service/close")
                .excute();
        Router.getInstance().buildExecutor("/user/service/clearRouter").excute();
        super.finish();
    }

    @Override
    public void showNotice(String numNotice) {
        tvFollowNotice.setVisibility(View.VISIBLE);
        tvFollowNotice.setText(numNotice);
    }

    @Override
    public void hideNotice() {
        tvFollowNotice.setVisibility(View.GONE);
    }

    @Override
    public void showEventPoster(String path) {
        if (eventPoster == null) {
            eventPoster = vsEventPoster.inflate();
            ivBg = (ImageView) eventPoster.findViewById(R.id.iv_image_bg);
            ivClose = (ImageView) eventPoster.findViewById(R.id.iv_close);
            ivContent = (ImageView) eventPoster.findViewById(R.id.iv_event_poster);
            ivBg.setOnClickListener(null);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventPoster.setVisibility(View.GONE);
                    getPresenter().onClose(true, true);
                }
            });
            ivContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onEventPoster();
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (eventPoster != null) {
                                eventPoster.setVisibility(View.GONE);
                                getPresenter().showRecommendedContent(false);
                            }
                        }
                    }, 200);
                }
            });
            final long startTime = System.currentTimeMillis();
            ImageLoader.with(this)
                    .load(path)
                    .corner(24)
                    .into(ivContent, null, new ImageLoaderCallback() {
                        @Override
                        public void onSuccess(String s, Bitmap bitmap, File file) {
                            if ((System.currentTimeMillis() - startTime) >= 200) {
                                getPresenter().onClose(true, false);
                                return;
                            }
                            eventPoster.setVisibility(View.VISIBLE);
                            getPresenter().saveSplashTime(true);
                        }

                        @Override
                        public void onFailue(Throwable throwable) {

                        }

                        @Override
                        public void onProgressChanged(double v, double v1) {

                        }
                    });
        }
    }

    @Override
    public void showRecommendedContent(String path, final boolean isStart) {
        if (recommendedContent == null) {
            recommendedContent = vsRecommendedContent.inflate();
            ivBg = (ImageView) recommendedContent.findViewById(R.id.iv_image_bg);
            ivClose = (ImageView) recommendedContent.findViewById(R.id.iv_close);
            ivContent = (ImageView) recommendedContent.findViewById(R.id.iv_event_poster);
            roundProgressBar = (RoundProgressBar) recommendedContent.findViewById(R.id.rp_bar);
            roundProgressBar.setResultListener(new RoundProgressBar.ResultListener() {
                @Override
                public void onFinish() {
                    recommendedContent.setVisibility(View.GONE);
                }
            });
            ivBg.setOnClickListener(null);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recommendedContent.setVisibility(View.GONE);
                    getPresenter().onClose(false, true);
                }
            });
            ivContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onRecommendedContent();
                    recommendedContent.setVisibility(View.GONE);
                }
            });
            final long startTime = System.currentTimeMillis();
            ImageLoader.with(this)
                    .load(path)
                    .big()
                    .setScaleType(ImageView.ScaleType.FIT_XY)
                    .into(ivContent, null, new ImageLoaderCallback() {
                        @Override
                        public void onSuccess(String s, Bitmap bitmap, File file) {
                            if ((System.currentTimeMillis() - startTime) >= 200) {
                                getPresenter().onClose(false, false);
                                return;
                            }
                            getPresenter().saveSplashTime(false);
                            recommendedContent.setVisibility(View.VISIBLE);
                            if (isStart) {
                                roundProgressBar.strat();
                            }
                        }

                        @Override
                        public void onFailue(Throwable throwable) {

                        }

                        @Override
                        public void onProgressChanged(double v, double v1) {

                        }
                    });
        }
    }

    @Override
    public void showSpringFestival() {
        if (springFestival == null) {
            springFestival = vsSpringFestival.inflate();
            fabSpringFestival = (ImageButton) springFestival.findViewById(R.id.fab_spring_festival);
            fabSpringFestival.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().onClickFestival();
                }
            });
        } else if (springFestival.getVisibility() != View.VISIBLE) {
            springFestival.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideSpringFestival() {
        if (springFestival != null && springFestival.getVisibility() != View.GONE) {
            springFestival.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent && null != intent.getExtras()) {
            String notice = intent.getStringExtra("notice");
            if (!StrUtil.isEmpty(notice)) {
                getPresenter().processNotice(notice);
                return;
            }
            String type = intent.getStringExtra(TYPE_TAB);
            if (type == null)
                return;
            switch (type) {
                case MainFragmentAdapter.TAB_LIVE:
                    mainFragmentAdapter.setSecondaryTab(intent.getStringExtra(TYPE_SECONDARY_TAB));
                    rlLive.performClick();
                    break;
                case MainFragmentAdapter.TAB_DISCOVER:
                    rlDiscover.performClick();
                    break;
                case MainFragmentAdapter.TAB_USER:
                    rlUser.performClick();
                    break;
                default:
                    rlRecommend.performClick();
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mainFragmentAdapter != null) {
            mainFragmentAdapter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (mainFragmentAdapter != null) {
            mainFragmentAdapter.onTrimMemory(level);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatusBar();
        if (recommendedContent != null && recommendedContent.getVisibility() == View.VISIBLE) {
            roundProgressBar.strat();
        }
    }


    private void setStatusBar() {
        if (mainFragmentAdapter != null && mainFragmentAdapter.getmCurrentTag() != null && mainFragmentAdapter.getmCurrentTag().equals(MainFragmentAdapter.TAB_USER)) {
            StatusBarUtil.changeStatusBar(getWindow(), false, true);
            content.setPadding(0, 0, 0, 0);
        } else {
            StatusBarUtil.setWhiteFullStatusBar(getWindow(), getSystemBarManager());
            content.setPadding(0, getSystemBarManager().getConfig().getStatusBarHeight(), 0, 0);
        }
    }


    static class BlurTransformation implements Transformation {

        final String url;

        public BlurTransformation(String url) {
            this.url = url;
        }

        @Override
        public Bitmap transform(Bitmap resource, int outHeigt, int outWidth) {
            if (resource.isRecycled())
                return resource;
            Bitmap bitmap = resource;
            Bitmap blurBitmap = FastBlur.doBlur(bitmap, 8, false);
            if (blurBitmap != null) {
                return blurBitmap;
            }
            return resource;
        }

        @Override
        public String getKey() {
            return url + "_Blur_size_+" + 100 + "_radio_" + 8;
        }
    }
}
