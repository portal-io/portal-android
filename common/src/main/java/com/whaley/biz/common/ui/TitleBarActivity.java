package com.whaley.biz.common.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.R;
import com.whaley.biz.common.R2;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.uiframe.ContainerActivity;
import com.whaley.core.widget.titlebar.TitleBar;

import butterknife.BindView;

/**
 * Created by yangzhi on 16/8/15.
 */
public class TitleBarActivity extends CommonActivity {

    protected static final String BELOW_TITLE = "below_title";
    @BindView(R2.id.frame_content)
    FrameLayout flContent;
    @BindView(R2.id.titlebar)
    TitleBar titlebar;
    @BindView(R2.id.layout_rootview)
    FrameLayout layoutRootview;


    public static void goPage(Starter starter, Class fragmentClazz) {
        goPage(starter, -1, fragmentClazz, false);
    }

    public static void goPage(Starter starter, int requestCode, Class fragmentClazz) {
        goPage(starter, requestCode, fragmentClazz, false);
    }

    public static void goPage(Starter starter, int requestCode, Class fragmentClazz, boolean isBelowTitle) {
        Intent intent = createIntent(starter, fragmentClazz.getName(), (String) null, isBelowTitle);
        goPage(starter, requestCode, intent);
    }

    public static void goPage(Starter starter, String fragmentPath) {
        goPage(starter, -1, fragmentPath, false);
    }

    public static void goPage(Starter starter, int requestCode, String fragmentPath) {
        goPage(starter, requestCode, fragmentPath, false);
    }

    public static void goPage(Starter starter, int requestCode, String fragmentPath, boolean isBelowTitle) {
        Intent intent = createIntent(starter, (String) null, fragmentPath, isBelowTitle);
        goPage(starter, requestCode, intent);
    }

    public static Intent createIntent(Starter starter, Class fragmentClazz) {
        return createIntent(starter, fragmentClazz.getName(), null, false);
    }

    public static Intent createIntent(Starter starter, Class fragmentClazz, boolean isBelowTitle) {
        return createIntent(starter, fragmentClazz.getName(), null, isBelowTitle);
    }


    public static Intent createIntent(Starter starter, String fragmentClazzName, String fragmentClassPath, boolean isBelowTitle) {
        Intent intent = new Intent(starter.getAttatchContext(), TitleBarActivity.class);
        intent.putExtra(STR_FRAGMENT_CLASS_NAME, fragmentClazzName);
        intent.putExtra(STR_FRAGMENT_PATH, fragmentClassPath);
        intent.putExtra(BELOW_TITLE, isBelowTitle);
        return intent;
    }


    //    @BindView(R.id.)
//    protected View layout_rootview;
//
//    @BindView(R.id.titlebar)
//    protected TitleBar titlebar;
//
//    @BindView(R.id.fl_content)
//    protected FrameLayout flContent;

//    @BindView(R.id.titleLine)
//    protected View titleLine;

    //    @Override


    @Override
    protected void setViews(Bundle savedInstanceState) {
        super.setViews(savedInstanceState);
        if (getIntent().getBooleanExtra(BELOW_TITLE, false)) {
            FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) flContent.getLayoutParams();
            contentParams.topMargin = 0;
            flContent.setLayoutParams(contentParams);
        }
        if (baseFragment != null && baseFragment instanceof BaseMVPFragment) {
            BaseMVPFragment baseMVPFragment = (BaseMVPFragment) baseFragment;
            baseMVPFragment.setTitlebar(titlebar);

        }
        setTopPadding();
    }

    public void setNotTopPadding() {
        layoutRootview.setPadding(0, 0, 0, 0);
    }

    protected void setTopPadding() {
        layoutRootview.setPadding(0, getSystemBarManager().getConfig().getStatusBarHeight(), 0, 0);
    }


    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setWhiteStatusBar();
        setContentView(getLayoutId());
        super.initView(savedInstanceState);
    }

    public void setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtil.setWhiteFullStatusBar(getWindow(), getSystemBarManager());

//        ViewGroup mContentView = (ViewGroup) getWindow().findViewById(Window.ID_ANDROID_CONTENT);
//        //获取父布局
//        View mContentChild = mContentView.getChildAt(0);
//        ViewCompat.setFitsSystemWindows(mContentChild, false);
//        ViewGroup.MarginLayoutParams layoutParams=(ViewGroup.MarginLayoutParams)mContentChild.getLayoutParams();
//        layoutParams.topMargin=getSystemBarManager().getConfig().getStatusBarHeight();
//        mContentChild.setLayoutParams(layoutParams);
    }

//    public void hideTitleLine(){
//        titleLine.setVisibility(View.GONE);
//    }

//    public TitleBar getTitlebar() {
//        return titlebar;
//    }

//    public View getTitleLine() {
//        return titleLine;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_titlebar;
    }

    @Override
    protected int getFragmentContentID() {
        return R.id.frame_content;
    }


}
