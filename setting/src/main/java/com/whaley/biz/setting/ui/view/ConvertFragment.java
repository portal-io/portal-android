package com.whaley.biz.setting.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.common.utils.StatusBarUtil;
import com.whaley.biz.common.widget.SimpleTextChangedListener;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.router.SettingRouterPath;
import com.whaley.biz.setting.ui.presenter.ConvertPresenter;
import com.whaley.biz.setting.util.FixInputMethodManagerLeakUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.utils.KeyboardUtil;
import com.whaley.core.widget.viewholder.IViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/4.
 */

@Route(path = SettingRouterPath.CONVERT, extras = RouterConstants.EXTRA_LOGIN)
public class ConvertFragment extends BaseMVPFragment<ConvertPresenter> implements ConvertView {

    @BindView(R2.id.tv_redemption)
    TextView tvRedemption;
    @BindView(R2.id.edit_redemption)
    EditText editRedemption;

    private SystemBarTintManager systemBarManager;
    private TextWatcher textWatcher;
    private Runnable runnable;

    @OnClick(R2.id.tv_redemption)
    void redemptionOnClick() {
        getPresenter().userDoRedeem();
    }

    @OnClick(R2.id.view_convert)
    void back() {
        onBackPressed();
    }

    public static void goPage(Starter starter, IViewHolder viewHolder) {
        Intent intent = CommonActivity.createIntent(starter, ConvertFragment.class.getName(), null);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) starter.getAttatchContext(),
                Pair.create(viewHolder.getItemView(), starter.getAttatchContext().getString(R.string.convert_name)));
        ((Activity) starter.getAttatchContext()).startActivityForResult(intent, 0, options.toBundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setWhiteStatusBar();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setWhiteStatusBar() {
        systemBarManager = new SystemBarTintManager(getActivity());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtil.setWhiteFullStatusBar(getActivity().getWindow(), systemBarManager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_convert;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        textWatcher = new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onRedemptionChanged(s.toString());
            }
        };
        editRedemption.addTextChangedListener(textWatcher);
        editRedemption.setFocusable(true);
        editRedemption.setFocusableInTouchMode(true);
        editRedemption.requestFocus();
        runnable = new Runnable() {
            public void run() {
                KeyboardUtil.showKeyBoard(editRedemption);
            }
        };
        editRedemption.postDelayed(runnable, 200);
    }

    @Override
    public void showBtn() {
        tvRedemption.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeBtn() {
        tvRedemption.setVisibility(View.GONE);
    }

    @Override
    public EditText getEditText() {
        return editRedemption;
    }

    @Override
    public boolean onBackPressed() {
        if(editRedemption!=null) {
            KeyboardUtil.hideKeyBoard(editRedemption);
        }
        ActivityCompat.finishAfterTransition(getActivity());
        return true;
    }

    @Override
    public void finish() {
        FixInputMethodManagerLeakUtil.fixInputMethodManagerLeak(getActivity());
        FixInputMethodManagerLeakUtil.fixTextLineCacheLeak();
        super.finish();
    }

    @Override
    public void onDestroyView() {
        if(editRedemption!=null){
            if(textWatcher!=null) {
                editRedemption.removeTextChangedListener(textWatcher);
            }
            if(runnable!=null){
                editRedemption.removeCallbacks(runnable);
            }
        }
        super.onDestroyView();
    }

}
