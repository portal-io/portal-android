package com.whaley.biz.program.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.ui.search.presenter.SearchPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/8/24.
 */

@Route(path = ProgramRouterPath.SEARCH)
public class SearchFragment extends BaseMVPFragment<SearchPresenter> implements SearchView  {

    @BindView(R2.id.layout_container)
    FrameLayout layoutContainer;
    @BindView(R2.id.et_search)
    EditText etSearch;
    @BindView(R2.id.btn_clear)
    ImageView btnClear;
    @BindView(R2.id.layout_clear)
    RelativeLayout layoutClear;
    @BindView(R2.id.tv_search)
    Button tvSearch;

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        etSearch.addTextChangedListener(mTextWatcher);
        watchSearch();
        gotoHistory();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    private void gotoHistory() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment fragment = getChildFragmentManager().findFragmentByTag("SearchHistoryFragment");
        if (null != fragment) {
            ft.remove(fragment);
        }
        ft.add(R.id.layout_container, SearchHistoryFragment.newInstance(), "SearchHistoryFragment")
                .commitAllowingStateLoss();
    }

    private void gotoResult(String str) {
        SearchResultFragment fragment = (SearchResultFragment)getChildFragmentManager().findFragmentByTag("SearchResultFragment");
        if(fragment == null){
            fragment = SearchResultFragment.newInstance(str);
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            if (!fragment.isAdded()) {
                ft.replace(R.id.layout_container, fragment, "SearchResultFragment")
                        .commitAllowingStateLoss();
            }
        }else{
            fragment.search(str);
        }
    }

    @OnClick(R2.id.layout_clear)
    void onClear(){
        etSearch.setText("");
    }

    @OnClick(R2.id.tv_search)
    void onSearch(){
        hideSoftKeyboard();
        if("取消".equals(tvSearch.getText().toString())){
            getActivity().finish();
        }else{
            tvSearch.setText("取消");
            getPresenter().onSearchClick(etSearch.getText().toString());
        }
    }

    public void searchHistory(String str){
        etSearch.setText(str);
        etSearch.setSelection(etSearch.getText().length());
        hideSoftKeyboard();
        tvSearch.setText("取消");
        getPresenter().searchHistory(str);
    }

    public void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }
        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            if(TextUtils.isEmpty(str)){
                tvSearch.setText("取消");
                layoutClear.setVisibility(View.GONE);
            }else{
                tvSearch.setText("搜索");
                layoutClear.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onSearch(String str) {
        gotoResult(str);
    }

    public void watchSearch() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((InputMethodManager) etSearch.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity()
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    if(!TextUtils.isEmpty(etSearch.getText().toString())){
                        hideSoftKeyboard();
                        tvSearch.setText("取消");
                        getPresenter().onSearchClick(etSearch.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
    }

}
