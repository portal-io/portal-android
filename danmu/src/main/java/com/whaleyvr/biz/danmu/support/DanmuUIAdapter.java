package com.whaleyvr.biz.danmu.support;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.KeyboardUtil;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;
import com.whaleyvr.biz.danmu.DanMu;
import com.whaleyvr.biz.danmu.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.internal.DebouncingOnClickListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;

/**
 * Created by yangzhi on 2017/9/4.
 */

public class DanmuUIAdapter extends ControlUIAdapter<DanmuController> {

    ViewGroup layoutDanmu;

    RecyclerView rlDanmu;

    DanMuAdapter danMuAdapter;

    TextView tvDanMuTop;

    ViewStub viewStub;

    View editLayout;

    EditText editText;

    Button btnSend;

    TextWatcher textWatcher;
    TextView tvDanmuSingle;
    View viewDanmu;

    @Override
    protected View initView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_player_danmu, parent, false);
        return view;
    }

    @Override
    protected void onViewCreated(View view) {
        layoutDanmu = (ViewGroup) view;
        rlDanmu = (RecyclerView) view.findViewById(R.id.rl_danmu);
        tvDanMuTop = (TextView) view.findViewById(R.id.tv_danmu_top);
        viewStub = (ViewStub) view.findViewById(R.id.vs_edit);
        tvDanmuSingle = (TextView) view.findViewById(R.id.tv_danmu_single);
        viewDanmu = view.findViewById(R.id.view_danmu);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rlDanmu.getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setRecycleChildrenOnDetach(true);
        rlDanmu.setLayoutManager(linearLayoutManager);
        danMuAdapter = new DanMuAdapter();
//        danMuAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(IViewHolder iViewHolder, int i) {
//
//            }
//        });
        rlDanmu.setAdapter(danMuAdapter);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (editText != null) {
            editText.removeTextChangedListener(textWatcher);
        }
    }

    @Override
    public void show(boolean anim) {
        if(anim) {
            startAnim(0f, 0f, 1f);
        }else{
            getRootView().setAlpha(1f);
        }
    }

    @Override
    public void hide(boolean anim) {
        if(anim) {
            startAnim(0f, 0f, 0f);
        }else{
            getRootView().setAlpha(0f);
        }
    }


    public void sendMsg(List<DanMu> danMuList) {
        if (danMuList.size() > 0) {
            rlDanmu.setVisibility(View.VISIBLE);
        }
        danMuAdapter.setData(danMuList);
        rlDanmu.smoothScrollToPosition(danMuList.size() - 1);
    }


    public void changeTopMsg(DanMu danMu) {
        tvDanMuTop.setVisibility(View.VISIBLE);
        tvDanMuTop.setTextColor(Color.parseColor(danMu.getColor()));
        tvDanMuTop.setText(danMu.getContent());
        tvDanMuTop.setBackgroundResource(R.drawable.bg_danmu_top);
    }

    public void clearTopMsg() {
        tvDanMuTop.setVisibility(View.GONE);
        tvDanMuTop.setText("");
        tvDanMuTop.setBackgroundResource(R.color.transparent);
    }


    public void showEdit() {
        if (editLayout == null) {
            editLayout = viewStub.inflate();
            editText = (EditText) editLayout.findViewById(R.id.et_input);
            btnSend = (Button) editLayout.findViewById(R.id.btn_send);
            Observable<Editable> observable = Observable
                    .create(new ObservableOnSubscribe<Editable>() {
                        @Override
                        public void subscribe(@NonNull final ObservableEmitter<Editable> e) throws Exception {
                            textWatcher = new TextWatcher() {

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    if (!e.isDisposed()) {
                                        e.onNext(s);
                                    }
                                }
                            };
                            editText.addTextChangedListener(textWatcher);
                        }
                    })
                    .debounce(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread());
            getController().setEditTextObservable(observable);
            btnSend.setOnClickListener(new DebouncingOnClickListener() {
                @Override
                public void doClick(View v) {
                    getController().onSendDanmuClick();
                }
            });
        }
        viewDanmu.setVisibility(View.GONE);
        editLayout.setVisibility(View.VISIBLE);
        editText.requestFocus();
        KeyboardUtil.showKeyBoard(editText);
    }


    public void hideEdit() {
        if (editLayout == null)
            return;
        editLayout.setVisibility(View.GONE);
        KeyboardUtil.hideKeyBoard(editText);
        if (viewDanmu != null) {
            viewDanmu.setVisibility(View.VISIBLE);
        }
    }

    public boolean isEditVisible() {
        if (editLayout == null)
            return false;
        return editLayout.getVisibility() == View.VISIBLE;
    }

    public void changeSendBtnEnable(boolean isEnable) {
        btnSend.setEnabled(isEnable);
    }

    public void updateEditText(CharSequence text) {
        editText.setText(text);
        editText.setSelection(text.length());
    }


    public String getEditText() {
        return editText.getText().toString();
    }

    public int getDanmuLayoutHeight() {
        int height = layoutDanmu.getVisibility() == View.VISIBLE ? DisplayUtil.convertDIP2PX(136) : 0;
        return height;
    }

    public void sendSingleMsg(DanMu danMu) {
        tvDanmuSingle.setText(danMu.contentSpannable);
    }

    public void showSingleDanmu(int height) {
        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) tvDanmuSingle.getLayoutParams();
        param.bottomMargin = height + 10;
        tvDanmuSingle.requestLayout();
        tvDanmuSingle.setVisibility(View.VISIBLE);
    }

    public void hideSingleDanmu() {
        tvDanmuSingle.setVisibility(View.GONE);
        tvDanmuSingle.setText("");
    }

    static class DanMuAdapter extends RecyclerViewAdapter<DanMu, ViewHolder> {

        @Override
        public ViewHolder onCreateNewViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_player_danmu, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, DanMu danMu, int position) {
            TextView tv_content = viewHolder.getView(R.id.tv_content);
            tv_content.setText(danMu.getContentSpannable());
        }

    }
}
