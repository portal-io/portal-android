package com.whaley.biz.setting.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTextChangedListener;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.setting.R;
import com.whaley.biz.setting.R2;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.ui.presenter.LocalImportByUrlPresenter;
import com.whaley.core.appcontext.Starter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/7.
 */

public class LocalImportByUrlFragment extends BaseMVPFragment<LocalImportByUrlPresenter> implements LocalImportByUrlView {

    @BindView(R2.id.suggestion)
    EditText suggestion;
    @BindView(R2.id.btn_import)
    View btnImport;
    @BindView(R2.id.tv_size)
    TextView tvSize;
    @BindView(R2.id.pb_size)
    ProgressBar pbSize;

    public static void goPage(Fragment fragment, int requestCode) {
        Intent intent = TitleBarActivity.createIntent((Starter)fragment, LocalImportByUrlFragment.class.getName(), null, false);
        fragment.startActivityForResult(intent, requestCode);
    }

    @OnClick(R2.id.btn_import)
    void clickBtnImport() {
        getPresenter().importByUrl();
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        if(getTitleBar()!=null) {
            getTitleBar().setTitleText("链接导入");
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        suggestion.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                getPresenter().onUrlChanged(s.toString());
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_import_by_url;
    }

    @Override
    public void setSize(long total, long leave) {
        tvSize.setText(String.format("手机存储空间：总空间%1$s / 剩余%2$s", SettingUtil.formatFileSize(total), SettingUtil.formatFileSize(leave)));
        pbSize.setProgress((int)((total - leave) * 1.0f*100/total));
    }

}
